package com.example.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;


public class PlaywrightLocatorsTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
        browserContext = browser.newContext();
    }

    @BeforeEach
    public void setUp() {
        page = browser.newPage();
        page.navigate("https://practicesoftwaretesting.com/contact");
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }


    public static class CustomOptions implements OptionsFactory {
        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(false)
                    .setLaunchOptions(
                            new BrowserType.LaunchOptions()
                                    .setArgs(Arrays.asList("--no-sandbox",
                                            "--disable-gpu",
                                            "--disable-extensions"))
                    );
        }
    }


    @DisplayName("By id")
    @Test
    void locateTheFirstNameFieldById() {
        page.locator("#first_name").fill("Sarah-Jane");
        PlaywrightAssertions.assertThat(page.locator("#first_name")).hasValue("Sarah-Jane");
    }


    @DisplayName("By CSS class")
    @Test
    void locateTheSendButtonByCssClass() {
        page.locator("#first_name").fill("Sarah-Jane");
        page.locator(".btnSubmit").click();
        List<String> alertMessage = page.locator(".alert").allTextContents();

        Assertions.assertTrue(!alertMessage.isEmpty());


    }


    @DisplayName("By attribute")
    @Test
    void locateTheSendButtonByAttribute() {
        page.locator("[placeholder='Your last name *']").fill("Smith");
        PlaywrightAssertions.assertThat(page.locator("#last_name")).hasValue("Smith");
    }





}
