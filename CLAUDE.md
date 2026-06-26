# AppiumMobileAutomation

Mobile automation framework for **Sauce Labs My Demo App** (Android).
Built with Java 17, Appium 3.5.2, TestNG 7.10.2, Maven.

---

## Commands

```bash
# Step 1 — Start Android emulator (Terminal 1)
emulator -avd Pixel_7_API_34

# Step 2 — Start Appium server (Terminal 2, must be running before tests)
appium

# Step 3 — Verify emulator is connected
adb devices
# Expected output: emulator-5554   device

# Run full regression suite
mvn clean test

# Run smoke test only (fastest — just AddToCart)
mvn clean test -DsuiteXmlFile=src/test/resources/testng-smoke.xml

# Run a single test class
mvn clean test -Dtest=LoginTest
mvn clean test -Dtest=CartManagementTest
mvn clean test -Dtest=ProductSearchTest
mvn clean test -Dtest=NegativeTest
mvn clean test -Dtest=CheckoutTest
```

---

## Project Structure

```
src/
├── main/java/pages/
│   ├── HomePage.java         Catalog page — click product, sort, cart icon
│   ├── ProductPage.java      Product detail — title, price, qty, add to cart
│   ├── LoginPage.java        Login/logout via navigation drawer
│   ├── CartPage.java         Cart management — remove items, proceed to checkout
│   └── CheckoutPage.java     Checkout flow — shipping, payment, place order
│
└── test/java/
    ├── base/
    │   └── BaseTest.java     Driver init/teardown; override noReset() per test class
    └── tests/
        ├── AddToCartTest.java         Smoke — add first product, verify cart count
        ├── LoginTest.java             Login/logout with valid and invalid credentials
        ├── CartManagementTest.java    Quantity change, multiple items, remove item
        ├── ProductSearchTest.java     Sort by name/price, product detail content
        ├── NegativeTest.java          Empty fields, min quantity, cart without login
        └── CheckoutTest.java          Full E2E: login → cart → checkout → order confirm

test/resources/
├── testng.xml           Full regression suite (all 6 test classes)
└── testng-smoke.xml     Smoke suite (AddToCart only)
```

---

## App Details

| Key | Value |
|-----|-------|
| App Package | `com.saucelabs.mydemoapp.android` |
| App Activity | `.view.activities.SplashActivity` |
| Device | `emulator-5554` (Pixel 7, API 33) |
| Appium Server | `http://127.0.0.1:4723` |
| Test Username | `bob@example.com` |
| Test Password | `10203040` |

---

## Key Element IDs

All IDs are prefixed with `com.saucelabs.mydemoapp.android:id/`

### Catalog Page (HomePage)

| Element ID | Description | Clickable |
|------------|-------------|-----------|
| `productIV` | Product image — **only clickable element per card** | ✅ yes |
| `titleTV` | Product title text | ❌ no |
| `priceTV` | Product price text | ❌ no |
| `productRV` | RecyclerView containing all product cards | ❌ no |
| `sortIV` | Sort icon (opens sort dialog) | ✅ yes |
| `menuIV` | Hamburger menu icon (opens navigation drawer) | ✅ yes |
| `cartRL` | Cart icon container in header | ✅ yes |
| `cartIV` | Cart image inside cartRL | ❌ no |
| `cartTV` | Cart count badge (hidden when cart is empty) | ❌ no |
| `productTV` | Page header — shows text "Products" on catalog | ❌ no |

> ⚠️ **CRITICAL**: The product card `ViewGroup` is `clickable="false"`.
> Neither `titleTV` nor the parent container responds to click.
> **You must click `productIV` (the image) to navigate to the product detail page.**

### Product Detail Page (ProductPage)

| Element ID | Description | Clickable |
|------------|-------------|-----------|
| `productTV` | Product name (e.g. "Sauce Labs Backpack") | ❌ no |
| `productIV` | Product image (not clickable on detail page) | ❌ no |
| `priceTV` | Price (e.g. "$ 29.99") | ❌ no |
| `cartBt` | "Add to Cart" button | ✅ yes |
| `minusIV` | Decrease quantity button | ✅ yes |
| `plusIV` | Increase quantity button | ✅ yes |
| `noTV` | Current quantity number (default "1") | ❌ no |
| `colorRV` | Color selector RecyclerView | ❌ no |
| `colorIV` | Color swatch image inside each color item | ❌ no |
| `aroundIV` | Selected color indicator ring | ❌ no |
| `rattingLL` | Container for price + star ratings | ❌ no |
| `rattingV` | Star rating view group | ❌ no |
| `start1IV`–`start5IV` | Individual star images | ✅ yes |

### Navigation Drawer (via menuIV)

Sort dialog options are accessed by **text XPath** — no stable resource IDs:

```java
By.xpath("//android.widget.TextView[@text='Name - Ascending']")
By.xpath("//android.widget.TextView[@text='Name - Descending']")
By.xpath("//android.widget.TextView[@text='Price - Ascending']")
By.xpath("//android.widget.TextView[@text='Price - Descending']")

By.xpath("//android.widget.TextView[@text='Log In']")
By.xpath("//android.widget.TextView[@text='Log Out']")
```

### Login Screen (LoginPage)

| Element ID | Description |
|------------|-------------|
| `nameET` | Username / email field |
| `passwordET` | Password field |
| `loginBtn` | Login button |
| `errorTV` | Error message text |

---

## Architecture Decisions

### Page Object Model (POM)
Every screen is represented by a page class in `src/main/java/pages/`.
Tests never use `By` locators directly — always go through page methods.

### BaseTest — configurable noReset
```java
// Default: keeps login state (fast)
protected boolean noReset() { return true; }

// Override in test class that needs a fresh start:
@Override
protected boolean noReset() { return false; }
```

| noReset value | Behaviour | Used by |
|---------------|-----------|---------|
| `true` | App keeps login state, lands on catalog | AddToCart, CartManagement, ProductSearch |
| `false` | Full app reset, starts from splash/login | Login, Negative, Checkout |

### Waits strategy
- **Implicit wait**: 10 seconds — set globally in `BaseTest.setUp()`
- **Explicit wait**: 15 seconds — used in `ProductPage` for cross-page navigation
- Never use `Thread.sleep()` — always use `WebDriverWait`

---

## Known Issues & Fixes

### 1. Selenium must be pinned to 4.19.0
`java-client 9.2.2` declares version range `[4.19.0, 5.0)`.
Maven resolves this to `4.45.0` which has breaking API changes — causes **silent class loading failure** where TestNG reports 0 tests with no error.

**Fix:** pin via `<dependencyManagement>` in `pom.xml`:
```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-api</artifactId>
      <version>4.19.0</version>
    </dependency>
    <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-remote-driver</artifactId>
      <version>4.19.0</version>
    </dependency>
    <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-support</artifactId>
      <version>4.19.0</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```

### 2. Surefire must declare suiteXmlFiles
Without this in `pom.xml`, Surefire ignores `testng.xml` entirely:
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
  <version>3.2.5</version>
  <configuration>
    <suiteXmlFiles>
      <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
    </suiteXmlFiles>
  </configuration>
</plugin>
```

### 3. TestNG must be 7.10.2+
TestNG 7.4.0 has a class inheritance bug where `@BeforeMethod` in `BaseTest`
is not picked up by subclasses. Use `7.10.2`.

### 4. Cart count assertion must read initial count first
`noReset=true` keeps cart state between test runs.
**Never** assert `cartCount == 1`. Always read initial count then assert `initial + 1`.

### 5. CheckoutTest and NegativeTest element IDs need verification
`CheckoutPage.java` field IDs (`fullNameET`, `address1ET`, etc.) are educated
guesses based on the app's naming convention. If `CheckoutTest` fails, save
the page source and verify actual IDs:
```java
// Add to test temporarily:
Files.write(Paths.get("/path/to/page_source.xml"), driver.getPageSource().getBytes());
```

---

## Dependencies (pom.xml)

```
io.appium:java-client         9.2.2
org.testng:testng             7.10.2
Selenium (pinned)             4.19.0
maven-surefire-plugin         3.2.5
Java                          17
```

---

## Test Credentials

```
Username : bob@example.com
Password : 10203040
```

These are the only working credentials in the demo app.
Locked users or wrong credentials are used intentionally in `NegativeTest`.
