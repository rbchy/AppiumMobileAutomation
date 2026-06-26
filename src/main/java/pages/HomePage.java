package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    AppiumDriver driver;

    // productIV (product image) is the only clickable element per catalog card.
    // The parent ViewGroup and titleTV are both clickable="false".
    By productImage = By.id("com.saucelabs.mydemoapp.android:id/productIV");
    By productTitle = By.id("com.saucelabs.mydemoapp.android:id/titleTV");
    By productPrice = By.id("com.saucelabs.mydemoapp.android:id/priceTV");

    // Header elements
    By sortIcon   = By.id("com.saucelabs.mydemoapp.android:id/sortIV");
    By cartIcon   = By.id("com.saucelabs.mydemoapp.android:id/cartIV");
    By cartCount  = By.id("com.saucelabs.mydemoapp.android:id/cartTV");
    By menuIcon   = By.id("com.saucelabs.mydemoapp.android:id/menuIV");

    // Catalog page header text
    By catalogHeader = By.id("com.saucelabs.mydemoapp.android:id/productTV");

    // Sort option items (text-based, appear in a dialog after tapping sortIV)
    By sortByNameAsc   = By.xpath("//android.widget.TextView[@text='Name - Ascending']");
    By sortByNameDesc  = By.xpath("//android.widget.TextView[@text='Name - Descending']");
    By sortByPriceAsc  = By.xpath("//android.widget.TextView[@text='Price - Ascending']");
    By sortByPriceDesc = By.xpath("//android.widget.TextView[@text='Price - Descending']");

    public HomePage(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * Waits until the catalog page is visible.
     * With noReset=true the app can resume on a non-catalog screen;
     * this method presses back up to 3 times until sortIV appears.
     */
    public void waitForCatalogPage() {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(sortIcon));
                return; // sortIV visible — we're on catalog
            } catch (Exception e) {
                // Not on catalog yet — press back and retry
                try {
                    driver.navigate().back();
                } catch (Exception ignored) {}
            }
        }
        // Final attempt with a longer wait to surface a proper error if still stuck
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(sortIcon));
    }

    /** Clicks the first product image to navigate to product detail. */
    public void clickFirstProduct() {
        driver.findElements(productImage).get(0).click();
    }

    /** Returns the title text of the first product in the catalog. */
    public String getFirstProductTitle() {
        return driver.findElements(productTitle).get(0).getText();
    }

    /** Returns all visible product title texts. */
    public List<WebElement> getAllProductTitles() {
        return driver.findElements(productTitle);
    }

    /** Returns all visible product price texts. */
    public List<WebElement> getAllProductPrices() {
        return driver.findElements(productPrice);
    }

    /** Returns cart count badge text. Throws NoSuchElementException if cart is empty. */
    public String getCartCount() {
        return driver.findElement(cartCount).getText();
    }

    /** Taps the cart icon to navigate to the Cart page. */
    public void clickCartIcon() {
        driver.findElement(cartIcon).click();
    }

    /** Taps Sort icon and selects "Name - Ascending". */
    public void sortByNameAscending() {
        driver.findElement(sortIcon).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(sortByNameAsc));
        driver.findElement(sortByNameAsc).click();
    }

    /** Taps Sort icon and selects "Name - Descending". */
    public void sortByNameDescending() {
        driver.findElement(sortIcon).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(sortByNameDesc));
        driver.findElement(sortByNameDesc).click();
    }

    /** Taps Sort icon and selects "Price - Ascending". */
    public void sortByPriceAscending() {
        driver.findElement(sortIcon).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(sortByPriceAsc));
        driver.findElement(sortByPriceAsc).click();
    }

    /** Taps Sort icon and selects "Price - Descending". */
    public void sortByPriceDescending() {
        driver.findElement(sortIcon).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(sortByPriceDesc));
        driver.findElement(sortByPriceDesc).click();
    }
}
