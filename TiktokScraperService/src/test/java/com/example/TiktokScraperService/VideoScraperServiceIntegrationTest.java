package com.example.TiktokScraperService;

import com.example.TiktokScraperService.customErrorHandling.VideoScraperException;
import com.example.TiktokScraperService.service.VideoScraperService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VideoScraperServiceIntegrationTest {

    @Autowired
    private VideoScraperService videoScraperService;
    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        browserContext = browser.newContext(new Browser.NewContextOptions()
                .setIgnoreHTTPSErrors(true)
                .setJavaScriptEnabled(true));
        page = browserContext.newPage();
        String BASE_URL = "https://www.tiktok.com/tag/dance";
        videoScraperService = new VideoScraperService(browser, page, BASE_URL);
    }

    @AfterEach
    void closePage() {
        if (page != null) {
            page.close();
        }
    }

    @Test
    void testNavigateToBaseUrl_ShouldNavigateToBaseURL() {
        assertDoesNotThrow(() -> {
            videoScraperService.navigateToBaseUrl();
        });
    }

    @Test
    void testClickFirstVideoOfBaseUrl_ShouldNavigateSuccessfully() {
        assertDoesNotThrow(() -> {
            videoScraperService.clickFirstVideoOfBaseUrl();
            System.out.println(page.url());
        });
    }
}
