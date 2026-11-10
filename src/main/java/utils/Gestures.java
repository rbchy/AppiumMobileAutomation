package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Scroll helpers. On smaller screens (e.g. the CI emulator) quantity controls,
 * lower checkout fields and buttons sit below the fold, so a plain findElement fails.
 */
public final class Gestures {

    private static final Duration DEFAULT_IMPLICIT = Duration.ofSeconds(10);

    private Gestures() {}

    /** Swipes up in the middle of the screen (content moves up, page scrolls down). */
    public static void scrollDown(WebDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int x = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.70);
        int endY = (int) (size.getHeight() * 0.35);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, startY));
        swipe.addAction(finger.createPointerDown(0));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x, endY));
        swipe.addAction(finger.createPointerUp(0));
        ((Interactive) driver).perform(Collections.singletonList(swipe));
    }

    /**
     * Returns the element once it is displayed, scrolling down up to {@code maxSwipes}
     * times. Falls back to a normal findElement (which throws a clear
     * NoSuchElementException) when it never appears.
     */
    public static WebElement scrollTo(WebDriver driver, By locator, int maxSwipes) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        try {
            for (int i = 0; i <= maxSwipes; i++) {
                List<WebElement> found = driver.findElements(locator);
                if (!found.isEmpty() && found.get(0).isDisplayed()) return found.get(0);
                if (i < maxSwipes) scrollDown(driver);
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT);
        }
        return driver.findElement(locator);
    }

    public static WebElement scrollTo(WebDriver driver, By locator) {
        return scrollTo(driver, locator, 4);
    }
}
