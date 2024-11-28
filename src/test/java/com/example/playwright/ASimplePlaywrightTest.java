package com.example.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


@UsePlaywright(HeadlessChromeOptions.class)
public class ASimplePlaywrightTest {


    @Test
    void shouldShowThePageTitle(Page page) {

        page.navigate("https://practicesoftwaretesting.com");
        String title = page.title();
        Assertions.assertTrue(title.contains("Practice Software Testing"));


    }

    @Test
    void shouldSearchByKeyword(Page page) {

        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("Pliers");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();


        int matchingSearchResults = page.locator(".card").count();
        Assertions.assertTrue(matchingSearchResults > 0);


    }


    @DisplayName("Search for pliers")
    @Test
    void searchForPliers(Page page) {
        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        page.waitForLoadState();
        assertThat(page.locator(".card")).hasCount(4);
        List<String> productName = page.getByTestId("product-name").allTextContents();
        System.out.println(productName);

        Assertions.assertTrue(productName.stream().allMatch(name -> name.contains("Pliers")));

        Locator outOfStockLocator = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");


        assertThat(outOfStockLocator).hasCount(1);
        assertThat(outOfStockLocator).hasText("Long Nose Pliers");


    }
}
