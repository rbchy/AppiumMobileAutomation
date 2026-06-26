# 📱 AppiumMobileAutomation

> **End-to-end Mobile Test Automation Framework** for the Sauce Labs My Demo App (Android) using Appium, TestNG, Maven, and Allure Reports — following the Page Object Model design pattern.

---

## 🏆 Test Results

| Metric | Result |
|--------|--------|
| 📋 Total Tests | 23 |
| ✅ Passed | 23 |
| ❌ Failed | 0 |
| 📊 Pass Rate | **100%** |
| ⏱️ Duration | ~7 minutes |
| 📅 Last Run | 25 June 2026 |

---

## ✨ Key Features

| Feature | Detail |
|---------|--------|
| ✅ Page Object Model | Clean separation of locators, actions, and test logic |
| ✅ Allure Reports | Beautiful HTML dashboard with timeline, graphs, and suite breakdown |
| ✅ Explicit Waits | `WebDriverWait` throughout — no hardcoded `Thread.sleep()` |
| ✅ App Lifecycle Handling | `terminateApp()` + `activateApp()` for real-world logout scenarios |
| ✅ Full Test Coverage | Login, Cart, Sort, Checkout, Product Detail, Form Validation |
| ✅ 23/23 Tests Passing | 100% pass rate on Android emulator |

---

## 🛠️ Tech Stack

| Tool / Library | Version | Purpose |
|----------------|---------|---------|
| ☕ Java | 17 | Core language |
| 📱 Appium java-client | 9.2.2 | Mobile automation driver |
| 🌐 Selenium | 4.21.0 | WebDriver foundation |
| 🧪 TestNG | 7.4.0 | Test framework |
| 🏗️ Maven | 3.x | Build & dependency management |
| 📊 Allure Reports | 2.27.0 | HTML test reporting |
| 🔌 Maven Surefire | 2.22.2 | Test runner plugin |
| 💻 IDE | Eclipse | Development environment |

---

## 📁 Project Structure

```
AppiumMobileAutomation/
├── src/
│   ├── main/java/
│   │   ├── pages/
│   │   │   ├── LoginPage.java        # Login/logout actions & locators
│   │   │   ├── HomePage.java         # Catalog & navigation
│   │   │   ├── ProductPage.java      # Product detail actions
│   │   │   └── CartPage.java         # Cart operations
│   │   └── utils/
│   │       └── BaseTest.java         # Driver setup & teardown
│   └── test/java/
│       └── tests/
│           ├── LoginTest.java         # Login & logout scenarios
│           ├── LoginFormTest.java     # Form validation edge cases
│           ├── AddToCartTest.java     # Add to cart & quantity control
│           ├── CartTest.java          # Cart state management
│           ├── ProductDetailTest.java # Product info verification
│           ├── SortTest.java          # Sorting by name & price
│           ├── CheckoutTest.java      # End-to-end order flow
│           └── SmokeTest.java         # Quick sanity check
├── src/test/resources/
│   └── testng.xml                     # TestNG suite configuration
├── pom.xml                            # Maven dependencies & plugins
└── README.md
```

---

## 🧪 Test Coverage

### 🔐 Login Tests (`LoginTest.java`)
- ✅ Valid login with correct credentials → catalog displayed
- ✅ Invalid login with wrong password
- ✅ Invalid login with wrong username
- ✅ Logout → app terminates and restarts to guest catalog

### 📝 Login Form Validation (`LoginFormTest.java`)
- ✅ Empty username field blocked
- ✅ Empty password field blocked
- ✅ Both fields empty blocked
- ✅ Invalid email format handling

### 🛒 Add to Cart (`AddToCartTest.java`)
- ✅ Successfully add item to cart
- ✅ Cart count increments correctly
- ✅ Quantity control at minimum (0)

### 🛍️ Cart Tests (`CartTest.java`)
- ✅ Remove item → cart becomes empty
- ✅ Cart badge not visible when empty
- ✅ Cart not accessible without login

### 📦 Product Detail (`ProductDetailTest.java`)
- ✅ Product name and price displayed correctly
- ✅ Add to cart from product detail page

### 🔢 Sort Tests (`SortTest.java`)
- ✅ Sort by name A→Z (first: Sauce Labs Backpack)
- ✅ Sort by name Z→A (first: Test.allTheThings() T-Shirt)
- ✅ Sort by price low→high (cheapest: $7.99)
- ✅ Sort by price high→low (most expensive: $49.99)

### 💳 Checkout (`CheckoutTest.java`)
- ✅ Complete end-to-end order placement

---

## 📋 Prerequisites

Before running the tests, ensure the following are set up:

1. ☕ **Java 17** installed and `JAVA_HOME` configured
2. 🏗️ **Maven 3.x** installed
3. 📱 **Appium Server** installed and running on `http://127.0.0.1:4723`
   ```bash
   npm install -g appium
   appium
   ```
4. 🤖 **Android Emulator** running (`emulator-5554`)
   ```bash
   emulator -avd <your-avd-name>
   ```
5. 📲 **Sauce Labs My Demo App** installed on the emulator
   - Package: `com.saucelabs.mydemoapp.android`
   - Download: [Sauce Labs Demo Apps](https://github.com/saucelabs/my-demo-app-android/releases)

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/rbchy/AppiumMobileAutomation.git
cd AppiumMobileAutomation
```

### 2. Start Appium Server

```bash
appium
```

### 3. Start Android Emulator

```bash
emulator -avd <your-avd-name>
```

### 4. Run All Tests

```bash
mvn clean test
```

### 5. View Allure Report

```bash
mvn allure:serve
```

> Browser opens automatically with the full interactive dashboard.

---

## 📊 Allure Report Dashboard

After running `mvn allure:serve`, you get a rich visual report:

| Section | Description |
|---------|-------------|
| 🏠 Overview | Total pass/fail count and 100% pass ring chart |
| 📁 Suites | Tests grouped by test class |
| 📈 Graphs | Visual pass/fail distribution charts |
| ⏱️ Timeline | Which test ran when and for how long |
| 🏷️ Behaviors | Tests grouped by feature/story |
| 📦 Packages | Tests grouped by Java package |

---

## ⚙️ Configuration

**Test credentials and device settings in `BaseTest.java`:**

| Setting | Value |
|---------|-------|
| Email | `bob@example.com` |
| Password | `10203040` |
| Device UDID | `emulator-5554` |
| App Package | `com.saucelabs.mydemoapp.android` |
| Platform | Android |
| Appium URL | `http://127.0.0.1:4723` |

---

## 🏗️ Design Pattern

This framework uses **Page Object Model (POM)**:

```
Test Class  →  Page Class  →  Appium Driver  →  App
```

- **`pages/`** — UI locators + action methods (no assertions)
- **`tests/`** — Test logic and assertions only
- **`BaseTest.java`** — `@BeforeMethod` / `@AfterMethod` driver lifecycle
- Each test class overrides `noReset()` setting as needed

---

## 🔑 Technical Highlights

- **App Logout Handling** — The Sauce Labs demo app kills its own process on logout. Fixed using `terminateApp()` + `activateApp()` (AndroidDriver) rather than `navigate().back()`, which caused flakiness.
- **Selenium Version Pinning** — All Selenium modules locked to `4.21.0` via Maven `<dependencyManagement>` to prevent version mismatch with `java-client 9.2.2`.
- **Allure Results Path** — Surefire is configured with `allure.results.directory` system property pointing to `target/allure-results` for correct report generation.
- **Explicit Waits Only** — `WebDriverWait` with `ExpectedConditions` used throughout; `Thread.sleep()` used only for the app process restart delay post-logout.

---

## 🔮 Future Enhancements

- 🔒 Move hardcoded credentials to environment variables or a config file
- ☁️ Integrate with Sauce Labs cloud device farm for real-device testing
- 🔄 Set up CI/CD pipeline with GitHub Actions
- 🍏 Add iOS test suite alongside Android
- 📸 Add screenshot capture on test failure (Allure attachment)
- 🎯 Add `@Epic`, `@Feature`, `@Story` Allure annotations for richer BDD-style reporting
- 🤖 Parallel test execution across multiple emulators
- 📊 Data-driven testing with TestNG `@DataProvider`

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create your branch: `git checkout -b feature/improvement`
3. Commit your changes: `git commit -m 'Add improvement'`
4. Push to the branch: `git push origin feature/improvement`
5. Open a Pull Request

---

## 📄 License

This project is open-source under the **MIT License**. See [LICENSE](LICENSE) for details.

---

## 💬 Support & Feedback

If you found this project helpful:

- ⭐ **Star** the repository
- 🍴 **Fork** it for your own use
- 💬 **Open an issue** for bugs or suggestions
- 🤝 **Contribute** to improve the project

---

## ⚠️ Disclaimer

This project is designed for **educational and portfolio purposes**. The test credentials (`bob@example.com`) are public demo credentials provided by Sauce Labs for their open-source demo application. Do not use this framework against production apps without proper authorization.

---

> **Version:** 1.0.0 &nbsp;|&nbsp; **Last Updated:** June 2026 &nbsp;|&nbsp; **Author:** RB Chowdhury
