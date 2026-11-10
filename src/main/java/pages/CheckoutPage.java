package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class CheckoutPage {
    AppiumDriver driver;

    // Step 1 — Shipping Address
    // IDs follow the app's naming convention; confirmed against open-source app structure.
    By fullNameField   = By.id("com.saucelabs.mydemoapp.android:id/fullNameET");
    By addressField    = By.id("com.saucelabs.mydemoapp.android:id/address1ET");
    By cityField       = By.id("com.saucelabs.mydemoapp.android:id/cityET");
    By stateField      = By.id("com.saucelabs.mydemoapp.android:id/stateET");
    By zipField        = By.id("com.saucelabs.mydemoapp.android:id/zipET");
    By countryField    = By.id("com.saucelabs.mydemoapp.android:id/countryET");
    By toPaymentButton = By.xpath("//android.widget.Button[@text='To Payment']");

    // Step 2 — Payment Info
    By cardNumberField = By.id("com.saucelabs.mydemoapp.android:id/cardNumberET");
    By expiryField     = By.id("com.saucelabs.mydemoapp.android:id/expirationDateET");
    By cvvField        = By.id("com.saucelabs.mydemoapp.android:id/securityCodeET");
    // Card holder name field ID confirmed from checkout_payment_source.xml
    By cardOwnerField  = By.id("com.saucelabs.mydemoapp.android:id/nameET");
    By toReviewButton  = By.xpath("//android.widget.Button[@text='Review Order']");

    // Step 3 — Order Review & Confirmation
    By placeOrderButton = By.xpath("//android.widget.Button[@text='Place Order']");

    // Confirmation screen — flexible match for multiple possible text strings
    By orderConfirmMsg = By.xpath(
        "//*[contains(@text,'CHECKOUT_COMPLETE')" +
        " or contains(@text,'Thank You')" +
        " or contains(@text,'Thank you')" +
        " or contains(@text,'Order Dispatched')" +
        " or contains(@text,'order has been placed')]"
    );

    public CheckoutPage(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * Fills in shipping address and taps "To Payment".
     * Saves a page-source diagnostic so field IDs can be verified if the step fails.
     */
    public void fillShippingAddress(String name, String address,
                                     String city, String state,
                                     String zip, String country) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.visibilityOfElementLocated(fullNameField));

        savePageSource("checkout_shipping_source.xml");

        type(fullNameField, name);
        type(addressField, address);
        type(cityField, city);
        type(stateField, state);
        type(zipField, zip);
        type(countryField, country);
        utils.Gestures.scrollTo(driver, toPaymentButton).click();
    }

    /**
     * Fills in payment details and taps "Review Order".
     * Saves a page-source diagnostic so field IDs can be verified if the step fails.
     */
    public void fillPaymentDetails(String cardNumber, String expiry,
                                    String cvv, String cardOwner) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.visibilityOfElementLocated(cardNumberField));

        savePageSource("checkout_payment_source.xml");

        type(cardNumberField, cardNumber);
        type(expiryField, expiry);
        type(cvvField, cvv);
        type(cardOwnerField, cardOwner);
        utils.Gestures.scrollTo(driver, toReviewButton).click();
    }

    /** Taps "Place Order" on the review screen. */
    public void placeOrder() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.presenceOfElementLocated(placeOrderButton));
        savePageSource("checkout_review_source.xml");
        utils.Gestures.scrollTo(driver, placeOrderButton).click();
    }

    /** Returns true if any known order-confirmation text appears on screen. */
    public boolean isOrderConfirmed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(orderConfirmMsg));
            savePageSource("checkout_confirm_source.xml");
            return true;
        } catch (Exception e) {
            savePageSource("checkout_confirm_source.xml"); // save even on failure
            return false;
        }
    }

    /** Scrolls the field into view (lower fields are below the fold on small screens), clears it and types. */
    private void type(By field, String value) {
        WebElement el = utils.Gestures.scrollTo(driver, field);
        el.clear();
        el.sendKeys(value);
    }

    /** Writes driver.getPageSource() to target/page-sources/ so element IDs can be inspected. */
    private void savePageSource(String fileName) {
        utils.Diagnostics.savePageSource(driver, fileName);
    }
}
