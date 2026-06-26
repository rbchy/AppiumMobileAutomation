package tests;
import org.testng.Assert;
import org.testng.annotations.Test;
public class SmokeTest {
    @Test
    public void sanityCheck() {
        System.out.println("=== SMOKE TEST PASSED ===");
        Assert.assertTrue(true);
    }
}
