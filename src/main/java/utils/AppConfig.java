package utils;

/**
 * Run-time settings. Each value is read from a -D system property first, then an
 * environment variable, then a default that matches the original local setup.
 *
 *   appium.url   / APPIUM_URL   http://127.0.0.1:4723
 *   device.name  / DEVICE_NAME  emulator-5554
 *   app.path     / APP_PATH     (empty = use the app already installed on the device)
 */
public final class AppConfig {

    public static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";
    public static final String APP_ACTIVITY = ".view.activities.SplashActivity";

    private AppConfig() {}

    static String read(String prop, String env, String def) {
        String v = System.getProperty(prop);
        if (v == null || v.isBlank()) v = System.getenv(env);
        return (v == null || v.isBlank()) ? def : v.trim();
    }

    public static String appiumUrl()  { return read("appium.url", "APPIUM_URL", "http://127.0.0.1:4723"); }
    public static String deviceName() { return read("device.name", "DEVICE_NAME", "emulator-5554"); }
    public static String appPath()    { return read("app.path", "APP_PATH", ""); }
}
