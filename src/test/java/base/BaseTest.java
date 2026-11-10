package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.AppConfig;
import utils.Diagnostics;

import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;

public class BaseTest {

    protected AppiumDriver driver;

    /**
     * Override in subclass to control app reset behavior:
     *   true  = keep login state between runs (faster)
     *   false = full app reset, always starts from login screen
     */
    protected boolean noReset() {
        return true;
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName(AppConfig.deviceName());
        options.setAppPackage(AppConfig.APP_PACKAGE);
        options.setAppActivity(AppConfig.APP_ACTIVITY);
        options.setAppWaitActivity("*");
        options.setAutomationName("UiAutomator2");
        options.setNoReset(noReset());
        options.setNewCommandTimeout(Duration.ofSeconds(120));
        options.setAdbExecTimeout(Duration.ofSeconds(60));
        options.setUiautomator2ServerInstallTimeout(Duration.ofSeconds(120));

        // APP_PATH set (CI) -> Appium installs the APK if it is missing on the device.
        String app = AppConfig.appPath();
        if (!app.isEmpty()) {
            options.setApp(new java.io.File(app).getAbsolutePath());
        }

        driver = new AndroidDriver(new URL(AppConfig.appiumUrl()), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver == null) return;
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                String base = result.getTestClass().getRealClass().getSimpleName()
                        + "_" + result.getMethod().getMethodName() + "_FAILED";
                Diagnostics.savePageSource(driver, base + ".xml");
                try {
                    byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Files.createDirectories(Diagnostics.outputDir());
                    Files.write(Diagnostics.outputDir().resolve(Diagnostics.sanitize(base + ".png")), png);
                } catch (Exception ignored) {}
            }
        } finally {
            driver.quit();
            driver = null;
        }
    }
}
