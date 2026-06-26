package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    AppiumDriver driver;

    By usernameField  = By.id(APP_PACKAGE + ":id/nameET");
    By passwordField  = By.id(APP_PACKAGE + ":id/passwordET");
    By loginButton    = By.id(APP_PACKAGE + ":id/loginBtn");
    By errorMessage   = By.id(APP_PACKAGE + ":id/errorTV");
    By menuIcon       = By.id(APP_PACKAGE + ":id/menuIV");
    By menuLoginItem  = By.xpath("//android.widget.TextView[@text='Log In']");
    By menuLogoutItem = By.xpath("//android.widget.TextView[@text='Log Out']");
    By catalogHeader  = By.id(APP_PACKAGE + ":id/productTV");

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void openLoginScreen() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.elementToBeClickable(menuIcon)).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(menuLoginItem));
        driver.findElement(menuLoginItem).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    public void login(String username, String password) {
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    /**
     * Logs out via the navigation drawer.
     *
     * Root cause (confirmed by page-source dump = empty after click):
     * The Sauce Labs demo app terminates its own process on logout
     * (clears data / finishes all activities). Appium's driver session
     * survives but the app UI disappears, producing an empty page source.
     *
     * Fix: after clicking Log Out, terminate + re-activate the app so
     * Appium reconnects to a fresh launch, then wait for menuIV.
     */
    public void logout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 1. Open drawer → tap Log Out
        wait.until(ExpectedConditions.elementToBeClickable(menuIcon)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(menuLogoutItem));
        driver.findElement(menuLogoutItem).click();

        // 2. App process dies after logout — give it 2 s to finish
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        // 3. Terminate (in case still running) then reactivate
        try {
            ((AndroidDriver) driver).terminateApp(APP_PACKAGE);
            System.out.println("INFO: App terminated after logout");
        } catch (Exception e) {
            System.out.println("INFO: terminateApp skipped (" + e.getMessage() + ")");
        }
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        try {
            ((AndroidDriver) driver).activateApp(APP_PACKAGE);
            System.out.println("INFO: App reactivated after logout");
        } catch (Exception e) {
            System.out.println("WARN: activateApp failed: " + e.getMessage());
        }

        // 4. Wait for catalog / splash to settle (fresh launch, guest mode)
        new WebDriverWait(driver, Duration.ofSeconds(30))
            .until(ExpectedConditions.visibilityOfElementLocated(menuIcon));
        System.out.println("INFO: Logout complete — app back on catalog (guest)");
    }

    public String getErrorMessage() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return driver.findElement(errorMessage).getText();
    }

    public boolean isLoginFormDisplayed() {
        try { return driver.findElement(usernameField).isDisplayed(); }
        catch (NoSuchElementException e) { return false; }
    }

    public boolean isLoggedIn() {
        try {
            driver.findElement(menuIcon).click();
            boolean found = driver.findElement(menuLogoutItem).isDisplayed();
            driver.navigate().back();
            return found;
        } catch (NoSuchElementException e) { return false; }
    }

    public boolean isCatalogDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(catalogHeader));
            return driver.findElement(catalogHeader).getText().equals("Products");
        } catch (Exception e) { return false; }
    }
}
