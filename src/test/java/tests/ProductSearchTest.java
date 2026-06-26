package tests;

import base.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ProductSearchTest — covers product sorting on the catalog page.
 *
 * Uses noReset=true (keeps login state).
 * IMPORTANT: With noReset=true the app can resume on a non-catalog screen
 * (e.g. product detail) if a previous session ended there.
 * Each test calls homePage.waitForCatalogPage() first to navigate
 * back to the catalog regardless of starting screen.
 */
public class ProductSearchTest extends BaseTest {

    @Override
    protected boolean noReset() { return true; }

    @Test(priority = 1, description = "Sort by Name Ascending — products should appear A→Z")
    public void testSortByNameAscending() {
        HomePage homePage = new HomePage(driver);
        homePage.waitForCatalogPage(); // ensure we're on the catalog before sorting

        homePage.sortByNameAscending();

        List<WebElement> titles = homePage.getAllProductTitles();
        Assert.assertFalse(titles.isEmpty(), "Product list should not be empty");

        List<String> actual = toStringList(titles);
        List<String> sorted = new ArrayList<>(actual);
        Collections.sort(sorted);

        Assert.assertEquals(actual, sorted,
            "Products should be sorted alphabetically A→Z");
        System.out.println("PASS: Name ascending — first product: " + actual.get(0));
    }

    @Test(priority = 2, description = "Sort by Name Descending — products should appear Z→A")
    public void testSortByNameDescending() {
        HomePage homePage = new HomePage(driver);
        homePage.waitForCatalogPage();

        homePage.sortByNameDescending();

        List<WebElement> titles = homePage.getAllProductTitles();
        Assert.assertFalse(titles.isEmpty(), "Product list should not be empty");

        List<String> actual = toStringList(titles);
        List<String> sortedDesc = new ArrayList<>(actual);
        sortedDesc.sort(Collections.reverseOrder());

        Assert.assertEquals(actual, sortedDesc,
            "Products should be sorted alphabetically Z→A");
        System.out.println("PASS: Name descending — first product: " + actual.get(0));
    }

    @Test(priority = 3, description = "Sort by Price Ascending — cheapest product should appear first")
    public void testSortByPriceAscending() {
        HomePage homePage = new HomePage(driver);
        homePage.waitForCatalogPage();

        homePage.sortByPriceAscending();

        List<WebElement> prices = homePage.getAllProductPrices();
        Assert.assertFalse(prices.isEmpty(), "Product prices should not be empty");

        List<Double> priceValues = parsePrices(prices);
        for (int i = 0; i < priceValues.size() - 1; i++) {
            Assert.assertTrue(priceValues.get(i) <= priceValues.get(i + 1),
                "Price at index " + i + " should be ≤ price at index " + (i + 1));
        }
        System.out.println("PASS: Price ascending — cheapest first: $" + priceValues.get(0));
    }

    @Test(priority = 4, description = "Sort by Price Descending — most expensive product should appear first")
    public void testSortByPriceDescending() {
        HomePage homePage = new HomePage(driver);
        homePage.waitForCatalogPage();

        homePage.sortByPriceDescending();

        List<WebElement> prices = homePage.getAllProductPrices();
        Assert.assertFalse(prices.isEmpty(), "Product prices should not be empty");

        List<Double> priceValues = parsePrices(prices);
        for (int i = 0; i < priceValues.size() - 1; i++) {
            Assert.assertTrue(priceValues.get(i) >= priceValues.get(i + 1),
                "Price at index " + i + " should be ≥ price at index " + (i + 1));
        }
        System.out.println("PASS: Price descending — most expensive first: $" + priceValues.get(0));
    }

    @Test(priority = 5, description = "Product detail page should show same title and price as catalog")
    public void testProductDetailPageContent() {
        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        homePage.waitForCatalogPage();

        String catalogTitle = homePage.getFirstProductTitle();
        homePage.clickFirstProduct();

        String detailTitle = productPage.getProductTitle();
        String detailPrice = productPage.getProductPrice();

        Assert.assertEquals(detailTitle, catalogTitle,
            "Product title on detail page should match catalog card title");
        Assert.assertTrue(detailPrice.contains("$"),
            "Product price should contain a $ sign");
        System.out.println("PASS: Detail page — " + detailTitle + " at " + detailPrice);
    }

    // ─── helpers ────────────────────────────────────────────────────────────────

    private List<String> toStringList(List<WebElement> elements) {
        List<String> list = new ArrayList<>();
        for (WebElement e : elements) list.add(e.getText());
        return list;
    }

    private List<Double> parsePrices(List<WebElement> elements) {
        List<Double> list = new ArrayList<>();
        for (WebElement e : elements) {
            // Price format: "$ 29.99"
            String raw = e.getText().replace("$", "").trim();
            list.add(Double.parseDouble(raw));
        }
        return list;
    }
}
