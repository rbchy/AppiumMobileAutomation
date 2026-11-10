package utils;

import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Writes page-source dumps for debugging locators.
 *
 * Files go to {@code target/page-sources/} (override with -Dpage.source.dir=...)
 * so the framework runs on any machine — earlier versions wrote to a hard-coded
 * /Users/... path that only existed on the author's laptop.
 */
public final class Diagnostics {

    private Diagnostics() {}

    public static Path outputDir() {
        return Paths.get(System.getProperty("page.source.dir", "target/page-sources"));
    }

    /** Safe file name: keeps letters, digits, dot, dash and underscore. */
    public static String sanitize(String name) {
        if (name == null || name.isBlank()) return "page";
        return name.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    /** Saves the current page source; never throws (diagnostics must not fail a test). */
    public static Path savePageSource(WebDriver driver, String fileName) {
        try {
            Path dir = outputDir();
            Files.createDirectories(dir);
            Path file = dir.resolve(sanitize(fileName));
            Files.write(file, driver.getPageSource().getBytes(StandardCharsets.UTF_8));
            return file;
        } catch (Exception e) {
            System.out.println("WARN: could not save page source " + fileName + ": " + e.getMessage());
            return null;
        }
    }
}
