package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;

/**
 * CheckoutTest — full end-to-end purchase flow.
 *
 * Uses noReset=false: always starts fresh (empty cart, logged out).
 *
 * NOTE: CheckoutPage element IDs (shipping/payment fields) are based on the
 * Sauce Labs demo app naming convention. If checkout fails after openCart()
 * succeeds, check checkout_shipping_source.xml and checkout_payment_source.xml
 * (written during the test) to verify actual field IDs.
 */
public class CheckoutTest extends BaseTest {

    @Override
    protected boolean noReset() { return false; }

    @Test(priority = 1, description = "Full checkout flow: login → add to cart → checkout → place order")
    public void testCompleteCheckout() {
        LoginPage loginPage     = new LoginPage(driver);
        HomePage homePage       = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage       = new CartPage(driver);
        CheckoutPage checkout   = new CheckoutPage(driver);

        // Step 1: Login
        loginPage.openLoginScreen();
        loginPage.login("bob@example.com", "10203040");
        Assert.assertTrue(loginPage.isCatalogDisplayed(),
            "Must be on catalog after login");

        // Step 2: Add first product to cart
        homePage.clickFirstProduct();
        String productName = productPage.getProductTitle();
        productPage.tapAddToCart();
        System.out.println("Added to cart: " + productName);

        // Step 3: Open cart — waits for "My Cart" header (productTV confirmed)
        cartPage.openCart();

        // Step 4: Proceed to checkout
        cartPage.proceedToCheckout();

        // Step 5: Fill shipping address
        checkout.fillShippingAddress(
            "Bob User",
            "123 Main Street",
            "San Francisco",
            "CA",
            "94102",
            "United States"
        );

        // Step 6: Fill payment details
        checkout.fillPaymentDetails(
            "4111111111111111",  // Test VISA card
            "12/25",
            "123",
            "Bob User"
        );

        // Step 7: Place order
        checkout.placeOrder();

        Assert.assertTrue(checkout.isOrderConfirmed(),
            "Order confirmation should be displayed after placing order");
        System.out.println("PASS: Order placed successfully for: " + productName);
    }
}
