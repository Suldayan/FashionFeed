package com.example.TiktokScraperService.service;

import com.example.TiktokScraperService.customErrorHandling.VideoScraperException;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/*
* TODO: Focus on the basic functionality first.
*  Right now, we just want to make sure we can properly grab the video needed
*
* */

@Service
public class VideoScraperService {

    private static final Logger logger = LoggerFactory.getLogger(VideoScraperService.class);

    private final Browser browser;
    private final Page page;
    private final String BASE_URL;

    public VideoScraperService(Browser browser, Page page, String baseUrl) {
        this.browser = browser;
        this.page = page;
        BASE_URL = baseUrl;
    }

    // Helper function for navigating
    public void navigateToBaseUrl() throws VideoScraperException {
        try {
            // First, we navigate to the base url
            logger.info("Attempting to navigate to url: {}", BASE_URL);
            logger.debug("Navigating to base url: {}", BASE_URL);

            Response response = page.navigate(BASE_URL);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);

            if (response.ok()) {
                logger.info("Successfully navigated to url: {}", BASE_URL);
            }
        } catch (PlaywrightException e) {
            throw new VideoScraperException("Failed to navigate to the base URL", e);
        }
    }

    // Helper function for checking if the element is visible
    public void isElementVisibleAndClickable(Locator element, String selector) throws VideoScraperException {
        try {
            logger.info("Looking for element: {}", element);
            logger.debug("Checking element visibility for: {}", element);

            page.waitForSelector(selector);
            logger.debug("Waiting for selector: {}", selector);
            if (element.isVisible()) {
                page.click(selector);
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);

                logger.info("Element, {}: found and clicked, url: {}", element, page.url());
                logger.debug("Navigating to new url: {} with element: {}", page.url(), element);
            }
            else {
                //TODO: add a retry mechanism
                logger.info("Unable to locate element: {}", element);
                logger.debug("Failing to locate element: {} at url: {}", element, page.url());
            }
        } catch (PlaywrightException e) {
            throw new VideoScraperException("The element is not visible in the DOM");
        }
    }

    // Helper function for clicking the first video
    public void clickFirstVideoOfBaseUrl() throws VideoScraperException {
        navigateToBaseUrl();

        try {
            logger.info("Attempting to click on the first video");
            logger.debug("Clicking on the first video of url: {}", BASE_URL);

            String selector = "div[data-e2e='challenge-item'][role='button']";
            String testSelector = "div[data-e2e='challenge-item-list'] div[data-e2e='challenge-item'] div[data-e2e='challenge-item-desc'] a[href]";
            Locator videoButton = page.locator(testSelector).first();

            isElementVisibleAndClickable(videoButton, selector);
        } catch (PlaywrightException e) {
            throw new VideoScraperException("Failed to click the first video", e);
        }
    }

    /*
    public void clickDownload() throws VideoScraperException {
        clickFirstVideo();

        try {
            logger.debug("Clicking the download button");
            Locator downloadButton = page.locator("");

            isElementVisible(downloadButton);
        } catch (PlaywrightException e) {
            throw new VideoScraperException("Failed to locate the download button");
        }
    }
    */
}
