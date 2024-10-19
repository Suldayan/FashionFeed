package com.example.TiktokScraperService.config;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaywrightConfig {

    private static final Logger logger = LoggerFactory.getLogger(PlaywrightConfig.class);

    @Value("${playwright.BASE_URL}")
    private String BASE_URL;

    @Value("${playwright.wait.timeout}")
    private int timeout;

    @Bean(destroyMethod = "close")
    public Playwright playwright() {
        try {
            return Playwright.create();
        } catch (BeanCreationException e) {
            logger.error("An error has occurred during bean creating for Playwright: {} - {}",
                    e.getMessage(), e.getBeanName());
            return null;
        }
    }

    @Bean(destroyMethod = "close")
    public Browser browser(Playwright playwright) {
        try {
            return playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setTimeout(timeout));
        } catch (BeanCreationException e) {
            logger.error("An error has occurred during bean creating for Browser: {} - {}",
                    e.getMessage(), e.getBeanName());
            return null;
        }
    }

    @Bean
    public BrowserContext browserContext(Browser browser) {
        try {
            return browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true));
        } catch (BeanCreationException e) {
            logger.error("An error has occurred during bean creating for Browser Context: {} - {}",
                    e.getMessage(), e.getBeanName());
            return null;
        }
    }

    @Bean
    public Page page(BrowserContext browserContext) {
        try {
            return browserContext.newPage();
        } catch (BeanCreationException e) {
            logger.error("An error has occurred during bean creating for Page: {} - {}",
                    e.getMessage(), e.getBeanName());
            return null;
        }
    }

    @Bean
    public String BASE_URL() {
        try {
            return BASE_URL;
        } catch (BeanCreationException e) {
            logger.error("An error has occurred during bean creating for Base URL: {} - {}",
                    e.getMessage(), e.getBeanName());
            return null;
        }
    }
}

