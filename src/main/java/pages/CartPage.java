package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class CartPage {
    AppiumDriver driver;

    // Cart icon in header (tap to open cart)
    By cartIcon       = By.id("com.saucelabs.mydemoapp.android:id/cartRL");
    By cartCountBadge = By.id("com.saucelabs.mydemoapp.android:id/cartTV");

    // Cart page header — productTV shows "My Cart" on the cart page
    // (confirmed from test failure observation)
    By cartPageHeader = By.id("com.saucelabs.mydemoapp.android:id/productTV");

    // Cart item elements — IDs confirmed from cart_page_source.xml
    By cartItems        = By.id("com.saucelabs.mydemoapp.android:id/productRV");
    // "Remove Item" button ID confirmed from cart_page_source.xml
    By removeItemButton = By.id("com.saucelabs.mydemoapp.android:id/removeBt");
    By itemQuantity     = By.id("com.saucelabs.mydemoapp.android:id/noTV");
    By itemName         = By.id("com.saucelabs.mydemoapp.android:id/titleTV");
    By itemPrice        = By.id("com.saucelabs.mydemoapp.android:id/priceTV");

    // "Proceed To Checkout" button — resource-id confirmed from cart_page_source.xml
    // The cart page reuses cartBt for "Proceed To Checkout" (same ID as product "Add to Cart")
    By proceedButton = By.id("com.saucelabs.mydemoapp.android:id/cartBt");

    // Empty cart message
    By emptyCartMsg = By.xpath(
        "//*[contains(@text,'No Items') or contains(@text,'no items') or contains(@text,'empty')]"
    );

    public CartPage(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * Taps the cart icon and waits for the "My Cart" page header to appear.
     * productTV = "My Cart" on the cart page (confirmed from failure observation).
     * Also saves a page-source diagnostic on first successful navigation.
     */
    public void openCart() {
        driver.findElement(cartIcon).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.textToBe(cartPageHeader, "My Cart"));

        // Diagnostic: save cart page source so element IDs can be verified
        try {
            String pageSource = driver.getPageSource();
            Files.write(
                Paths.get("/Users/ranajitchowdhury/eclipse-workspace/AppiumMobileAutomation/cart_page_source.xml"),
                pageSource.getBytes()
            );
            System.out.println("INFO: Cart page source saved to cart_page_source.xml");
        } catch (Exception ignored) {}
    }

    /** Returns current cart badge count. Returns 0 if badge not visible. */
    public int getCartCount() {
        try {
            return Integer.parseInt(driver.findElement(cartCountBadge).getText());
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    /** Returns list of all item name elements in the cart. */
    public List<WebElement> getCartItemNames() {
        return driver.findElements(itemName);
    }

    /** Returns the quantity text of the first cart item. */
    public String getFirstItemQuantity() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(itemQuantity));
        return driver.findElements(itemQuantity).get(0).getText();
    }

    /** Removes the first item from the cart. */
    public void removeFirstItem() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(removeItemButton));

        int countBefore = driver.findElements(removeItemButton).size();
        driver.findElements(removeItemButton).get(0).click();

        // Wait for the button count to drop (item removed) or empty-cart msg to appear
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(d -> {
                int countNow = d.findElements(removeItemButton).size();
                boolean emptyMsgVisible = !d.findElements(emptyCartMsg).isEmpty();
                return countNow < countBefore || emptyMsgVisible;
            });
    }

    /**
     * Returns true if the cart appears to have no items.
     * Checks in order:
     *  1. An explicit empty-cart message is displayed
     *  2. No remove/delete buttons remain visible
     */
    public boolean isCartEmpty() {
        // Check for explicit empty message
        try {
            List<WebElement> msgs = driver.findElements(emptyCartMsg);
            if (!msgs.isEmpty() && msgs.get(0).isDisplayed()) return true;
        } catch (Exception ignored) {}

        // No remove buttons = no items
        return driver.findElements(removeItemButton).isEmpty();
    }

    /** Taps "Proceed To Checkout". */
    public void proceedToCheckout() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(proceedButton));
        driver.findElement(proceedButton).click();
    }
}
