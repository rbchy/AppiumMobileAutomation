package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

public class ProductPage {
    AppiumDriver driver;

    // Confirmed from detail page source XML
    By addToCartBtn = By.id("com.saucelabs.mydemoapp.android:id/cartBt");
    By productTitle = By.id("com.saucelabs.mydemoapp.android:id/productTV");
    By productPrice = By.id("com.saucelabs.mydemoapp.android:id/priceTV");

    // Quantity controls (addToCartLL contains these)
    By minusButton  = By.id("com.saucelabs.mydemoapp.android:id/minusIV");
    By plusButton   = By.id("com.saucelabs.mydemoapp.android:id/plusIV");
    By quantityText = By.id("com.saucelabs.mydemoapp.android:id/noTV");

    public ProductPage(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * Taps "Add to Cart". Scrolls down and retries if the button is below the fold —
     * some products have more content so cartBt may not be immediately visible.
     */
    public void tapAddToCart() {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn));
                break; // visible — exit retry loop
            } catch (Exception e) {
                if (attempt < 2) scrollDown(); // scroll and try again
            }
        }
        // Final reliable wait before click
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn));
        driver.findElement(addToCartBtn).click();
    }

    /** Swipes up on screen to scroll the page down (reveals content below the fold). */
    private void scrollDown() {
        Dimension size = driver.manage().window().getSize();
        int x      = size.getWidth() / 2;
        int startY = (int)(size.getHeight() * 0.70);
        int endY   = (int)(size.getHeight() * 0.30);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0);
        swipe.addAction(finger.createPointerMove(Duration.ZERO,
            PointerInput.Origin.viewport(), x, startY));
        swipe.addAction(finger.createPointerDown(0));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(800),
            PointerInput.Origin.viewport(), x, endY));
        swipe.addAction(finger.createPointerUp(0));
        driver.perform(Collections.singletonList(swipe));
    }

    /** Waits for productTV and returns the product name on the detail page. */
    public String getProductTitle() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.visibilityOfElementLocated(productTitle));
        return driver.findElement(productTitle).getText();
    }

    /** Returns price text on the detail page, e.g. "$ 29.99". */
    public String getProductPrice() {
        return driver.findElement(productPrice).getText();
    }

    /** Taps the + button to increase quantity. */
    public void tapPlus() {
        driver.findElement(plusButton).click();
    }

    /** Taps the - button to decrease quantity (minimum stays at 1). */
    public void tapMinus() {
        driver.findElement(minusButton).click();
    }

    /** Returns current quantity value as text, e.g. "1", "2", "3". */
    public String getQuantity() {
        return driver.findElement(quantityText).getText();
    }
}