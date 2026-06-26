package tests;

import base.BaseTest;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductPage;

public class AddToCartTest extends BaseTest {

    // noReset=true: keeps login state so app lands on product catalog
    @Override
    protected boolean noReset() { return true; }

    @Test
    public void testAddProductToCart() {
        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);

        int initialCount = 0;
        try {
            initialCount = Integer.parseInt(homePage.getCartCount());
        } catch (NoSuchElementException ignored) {
            // Cart badge not visible — cart is empty
        }
        System.out.println("Initial cart count: " + initialCount);

        homePage.clickFirstProduct();

        String title = productPage.getProductTitle();
        Assert.assertFalse(title.isEmpty(), "Product title should not be empty");
        System.out.println("Product: " + title);

        productPage.tapAddToCart();

        int updatedCount = Integer.parseInt(homePage.getCartCount());
        Assert.assertEquals(updatedCount, initialCount + 1,
            "Cart count should increase by exactly 1");
        System.out.println("Test Passed! Cart Count: " + updatedCount);
    }
}
