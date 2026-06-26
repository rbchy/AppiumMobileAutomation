package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;
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

    @BeforeMethod
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("emulator-5554");
        options.setAppPackage("com.saucelabs.mydemoapp.android");
        options.setAppActivity(".view.activities.SplashActivity");
        options.setAutomationName("UiAutomator2");
        options.setNoReset(noReset());

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
