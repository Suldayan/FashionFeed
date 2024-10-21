package com.example.TiktokScraperService.service;

import com.example.TiktokScraperService.customErrorHandling.VideoScraperException;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoScraperService implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(VideoScraperService.class);
    private static final int INITIAL_TIMEOUT = 10000; // 10 seconds
    private static final int ELEMENT_TIMEOUT = 5000;  // 5 seconds
    private static final int SCROLL_TIMEOUT = 2000;   // 2 seconds

    private final Page page;
    private final String BASE_URL;

    public VideoScraperService(Page page, String baseUrl) {
        this.page = page;
        BASE_URL = baseUrl;
        configurePageTimeouts();
    }

    @Override
    public void close() {
        if (page != null) {
            page.close();
        }
    }

    private void configurePageTimeouts() {
        page.setDefaultTimeout(INITIAL_TIMEOUT);
        page.setDefaultNavigationTimeout(INITIAL_TIMEOUT);
    }

    // Helper function to help navigate to urls
    public void navigateToUrl(String url) throws VideoScraperException {
        try {
            logger.debug("Working on navigating to: {}", url);
            logger.info("Navigating to: {}", url);
            Response response = page.navigate(url, new Page.NavigateOptions()
                    .setWaitUntil(WaitUntilState.NETWORKIDLE));

            if (!response.ok()) {
                throw new VideoScraperException("Error navigating to url: " + url + " with response: " + response);
            }

            logger.info("Successfully navigated to: {}", url);
        } catch (PlaywrightException e) {
            throw new VideoScraperException("Unable to navigate to url: " + url);
        }
    }

    // Helper function for validating elements
    public void isElementPresent(String selector) {
        try {
            logger.info("Inspecting for selector: {}", selector);

            page.waitForSelector(selector,
                    new Page.WaitForSelectorOptions()
                            .setTimeout(ELEMENT_TIMEOUT)
                            .setState(WaitForSelectorState.ATTACHED));
        } catch (TimeoutError e) {
            logger.debug("Timeout occurred waiting for selector: {}", selector);
        }
    }

    // This is one of the main functions to navigate, validate, and extract the video link
    public Optional<ElementHandle[]> getAllVideoElementsOnCurrentDom() throws VideoScraperException {
        // First, we want to navigate to the base url (Configurable in the application.yml)
        navigateToUrl(BASE_URL);

        try {
            // Now, we want to configure the selectors to navigate through the dom and reach the href
            String selector = "div[class='css-x6y88p-DivItemContainerV2'] a[href]";
            isElementPresent(selector);

            return Optional.ofNullable(page.querySelectorAll(selector))
                    .map(elements -> elements.toArray(new ElementHandle[0]));

        } catch (PlaywrightException e) {
            throw new VideoScraperException("Unable to grab the video link");
        }
    }

    // Helper function for scrolling after grabbing elements
    public void scrollToLoadMoreDom() throws VideoScraperException {
        try {
            logger.info("Scrolling to load more dom");
            logger.debug("Attempting to scroll to load more dom");

            page.evaluate("window.scrollBy(0, window.innerHeight);");
            page.waitForTimeout(SCROLL_TIMEOUT);
        } catch (PlaywrightException e) {
            throw  new VideoScraperException("Unable to scroll down the current page");
        }
    }
}