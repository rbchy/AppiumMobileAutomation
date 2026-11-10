package unit;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.AppConfig;
import utils.Diagnostics;
import utils.Prices;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/** Device-free tests for the framework helpers — they run in CI on every push. */
public class UtilsTest {

    @Test
    public void parsesAppPriceLabels() {
        Assert.assertEquals(Prices.parse("$ 29.99"), new BigDecimal("29.99"));
        Assert.assertEquals(Prices.parse("$7.99"), new BigDecimal("7.99"));
        Assert.assertEquals(Prices.parse("$ 1,049.00"), new BigDecimal("1049.00"));
        Assert.assertEquals(Prices.parse("$ 15"), new BigDecimal("15"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void rejectsLabelWithoutAmount() {
        Prices.parse("$ .");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void rejectsNullLabel() {
        Prices.parse(null);
    }

    @Test
    public void detectsAscendingAndDescendingOrder() {
        List<BigDecimal> asc = Prices.parseAll(Arrays.asList("$ 7.99", "$ 9.99", "$ 9.99", "$ 49.99"));
        Assert.assertTrue(Prices.isAscending(asc));
        Assert.assertFalse(Prices.isDescending(asc));

        List<BigDecimal> desc = Prices.parseAll(Arrays.asList("$ 49.99", "$ 29.99", "$ 7.99"));
        Assert.assertTrue(Prices.isDescending(desc));
        Assert.assertFalse(Prices.isAscending(desc));
    }

    @Test
    public void comparesNumericallyNotAlphabetically() {
        // "$ 100.00" < "$ 9.99" as strings — must not count as ascending
        Assert.assertFalse(Prices.isAscending(Prices.parseAll(Arrays.asList("$ 100.00", "$ 9.99"))));
    }

    @Test
    public void pageSourceDirIsPortable() {
        Assert.assertFalse(Diagnostics.outputDir().isAbsolute(),
            "Default diagnostics dir must be project-relative, not a machine-specific absolute path");
        Assert.assertEquals(Diagnostics.sanitize("Cart Test/remove#1.xml"), "Cart_Test_remove_1.xml");
        Assert.assertEquals(Diagnostics.sanitize(" "), "page");
    }

    @Test
    public void configDefaultsMatchLocalEmulatorSetup() {
        if (System.getenv("APPIUM_URL") == null && System.getProperty("appium.url") == null)
            Assert.assertEquals(AppConfig.appiumUrl(), "http://127.0.0.1:4723");
        Assert.assertEquals(AppConfig.APP_PACKAGE, "com.saucelabs.mydemoapp.android");
    }

    @Test
    public void systemPropertyOverridesDefault() {
        System.setProperty("device.name", "Pixel_7_API_33");
        try {
            Assert.assertEquals(AppConfig.deviceName(), "Pixel_7_API_33");
        } finally {
            System.clearProperty("device.name");
        }
    }
}
