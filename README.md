<div align="center">

# 📱 Appium Mobile Automation — Sauce Labs My Demo App (Java · Appium · TestNG)

### Android end-to-end tests for Sauce Labs' *My Demo App*: login, catalog sorting, cart, a full checkout, and negative cases. Page Objects, Appium 2 + UiAutomator2, Allure, and a real Android 13 emulator in GitHub Actions.

[![Typing SVG](https://readme-typing-svg.demolab.com/?font=Fira+Code&size=18&pause=1500&color=E2231A&center=true&vCenter=true&width=900&repeat=true&lines=Java+17+%C2%B7+Appium+2+%C2%B7+UiAutomator2+%C2%B7+TestNG+7.10;Login+%C2%B7+Sort+%C2%B7+Cart+%C2%B7+Checkout+%C2%B7+Negative;22+device+tests+%2B+8+unit+tests+green+in+CI)](https://git.io/typing-svg)

<!-- ============ STATUS BAR ============ -->
[![CI](https://github.com/rbchy/AppiumMobileAutomation/actions/workflows/ci.yml/badge.svg)](https://github.com/rbchy/AppiumMobileAutomation/actions/workflows/ci.yml)
![Tests](https://img.shields.io/badge/Device%20tests-22%20%2F%2022%20passed-22c55e?style=for-the-badge&logo=checkmarx&logoColor=white)
![Unit](https://img.shields.io/badge/Unit%20tests-8%20%2F%208-22c55e?style=for-the-badge)
![Device](https://img.shields.io/badge/Device-Android%2013%20emulator-3DDC84?style=for-the-badge&logo=android&logoColor=white)

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Appium](https://img.shields.io/badge/Appium-2%20%C2%B7%20java--client%209.2-662D91?style=for-the-badge&logo=appium&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.21-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.10-FF7F00?style=for-the-badge)
![Allure](https://img.shields.io/badge/Allure-2.27-F7B500?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

![Last Commit](https://img.shields.io/github/last-commit/rbchy/AppiumMobileAutomation?style=flat-square&color=e2231a&label=Last%20commit)
![Repo Size](https://img.shields.io/github/repo-size/rbchy/AppiumMobileAutomation?style=flat-square&color=155e75&label=Repo%20size)

</div>

---

## 📌 At a Glance

<table>
<tr>
<td width="50%" valign="top">

**🙋 What this is**

A Page Object framework that tests the Android build of **[Sauce Labs My Demo App](https://github.com/saucelabs/my-demo-app-android)** (`com.saucelabs.mydemoapp.android`), the native shopping app Sauce Labs publishes for automation practice. It covers:

- login, logout and invalid / empty credentials
- catalog sorting by name and price (A→Z, Z→A, low→high, high→low)
- product detail, quantity +/− and the cart badge
- removing items and an **end-to-end checkout** (shipping → payment → review → *Checkout Complete*)

Every push runs it. GitHub Actions boots an **Android 13 emulator** (KVM), starts **Appium 2 + UiAutomator2**, downloads the latest My Demo App APK, installs it, and runs the full regression suite. Allure results, failure screenshots, UI-hierarchy dumps and the Appium log are uploaded as artifacts.

</td>
<td width="50%" valign="top">

**📍 Quick facts**

| | |
|---|---|
| 🤖 **App** | Sauce Labs My Demo App 2.3.0 (native Android) |
| 📱 **Device** | Android 13 emulator (API 33, x86_64) |
| 🧪 **Device tests** | 22 in 6 classes |
| 🧩 **Unit tests** | 8 (no device; price parsing, config, diagnostics) |
| ✅ **CI** | 22 / 22 + 8 / 8 · suite ≈ 8 min |
| 🧱 **Pattern** | Page Object Model, one fresh driver per test |
| 📊 **Reports** | Allure · screenshot + UI XML on failure |
| 🔁 **Runs** | push · pull request · manual |

</td>
</tr>
</table>

---

## ✅ Latest Results (GitHub Actions, Android 13 emulator)

| Suite | Tests | ✅ Passed | ❌ Failed | Where it runs |
|---|---:|---:|---:|---|
| 🧩 Unit (`testng-unit.xml`) | 8 | 8 | 0 | plain JVM, ~1 s |
| 📱 Regression (`testng.xml`) | 22 | 22 | 0 | Android 13 emulator, ~8 min |
| **Total** | **30** | **30** | **0** | |

```mermaid
pie showData
    title Device tests by area (22)
    "Login (4)" : 4
    "Add to cart (1)" : 1
    "Cart management (4)" : 4
    "Sort & product detail (5)" : 5
    "Negative / edge cases (7)" : 7
    "End-to-end checkout (1)" : 1
```

```mermaid
pie showData
    title CI result: all 30 tests
    "Passed" : 30
    "Failed" : 0
```

```mermaid
xychart-beta
    title "Tests per class"
    x-axis ["Login", "AddToCart", "CartMgmt", "ProductSearch", "Negative", "Checkout", "Unit"]
    y-axis "Tests" 0 --> 10
    bar [4, 1, 4, 5, 7, 1, 8]
```

```mermaid
xychart-beta
    title "First emulator run vs now (device tests passed of 22)"
    x-axis ["Run 1 (before scroll fix)", "Run 2 (after fix)"]
    y-axis "Passed" 0 --> 22
    bar [18, 22]
```

> Run 1 failed 4 tests on the CI emulator's smaller screen: the quantity +/− buttons and the lower checkout fields were below the fold. `utils.Gestures.scrollTo` fixed all four.

---

## 🏗️ Architecture

```mermaid
flowchart LR
    subgraph Tests["🧪 TestNG tests (src/test/java/tests)"]
        LT[LoginTest]
        AT[AddToCartTest]
        CM[CartManagementTest]
        PS[ProductSearchTest]
        NT[NegativeTest]
        CT[CheckoutTest]
    end
    BT[BaseTest<br/>driver per test<br/>failure dump]
    subgraph Pages["📄 Page Objects (src/main/java/pages)"]
        LP[LoginPage]
        HP[HomePage]
        PP[ProductPage]
        CP[CartPage]
        KP[CheckoutPage]
    end
    subgraph Utils["🧰 utils"]
        AC[AppConfig<br/>-D / env]
        GS[Gestures<br/>scrollTo]
        PR[Prices<br/>BigDecimal]
        DG[Diagnostics<br/>page source]
    end
    Tests --> BT
    Tests --> Pages
    Pages --> Utils
    BT --> AC
    BT --> DG
    BT -->|W3C / HTTP| AP[Appium 2 server<br/>UiAutomator2 3.10]
    AP --> EM[📱 Android 13 emulator<br/>My Demo App APK]
```

### CI pipeline

```mermaid
flowchart TD
    P([push / PR / manual]) --> U[Job 1: Compile + unit tests<br/>mvn test -Dsuite=testng-unit]
    U -->|green| A[Job 2: Android]
    A --> K[Enable KVM]
    K --> I[npm i -g appium@2<br/>driver uiautomator2@3.10.0]
    I --> D[scripts/get-apk.sh<br/>latest saucelabs/my-demo-app-android release]
    D --> C{AVD cache hit?}
    C -- no --> S[Create AVD snapshot]
    C -- yes --> R
    S --> R[Boot emulator · adb install APK<br/>start Appium · mvn test]
    R --> SUM[Test summary<br/>+ UI dump of every failure]
    SUM --> ART[(Artifacts: allure-results,<br/>page-sources, surefire, appium.log)]
```

### One test, end to end

```mermaid
sequenceDiagram
    autonumber
    participant T as CheckoutTest
    participant B as BaseTest
    participant A as Appium 2
    participant E as Emulator (My Demo App)
    B->>A: new AndroidDriver(UiAutomator2Options, noReset=false)
    A->>E: clear app data, launch SplashActivity
    T->>E: menu → Log In → bob@example.com / 10203040
    E-->>T: catalog "Products"
    T->>E: tap first product → Add To Cart
    T->>E: cart → Proceed To Checkout
    T->>E: shipping form (scrollTo each field) → To Payment
    T->>E: card details → Review Order → Place Order
    E-->>T: "Checkout Complete"
    B->>A: quit() (dump page source + screenshot if failed)
```

### App screens the suite walks through

```mermaid
stateDiagram-v2
    [*] --> Catalog: launch (guest)
    Catalog --> Login: menu → Log In
    Login --> Login: empty / invalid input
    Login --> Catalog: valid login
    Catalog --> Catalog: sort (name / price)
    Catalog --> ProductDetail: tap product image
    ProductDetail --> ProductDetail: quantity + / −
    ProductDetail --> Cart: Add To Cart → cart icon
    Cart --> Cart: Remove Item
    Cart --> Shipping: Proceed To Checkout
    Shipping --> Payment: To Payment
    Payment --> Review: Review Order
    Review --> Complete: Place Order
    Complete --> [*]
    Catalog --> Catalog: Log Out (app restarts)
```

---

## 🧪 Test Cases

```mermaid
mindmap
  root((My Demo App<br/>22 device tests))
    Login 4
      valid credentials
      wrong password
      wrong username
      logout
    Cart 5
      add one product
      quantity 2
      minus at minimum
      add twice
      remove item
    Sort & detail 5
      name A→Z
      name Z→A
      price low→high
      price high→low
      detail matches catalog
    Negative 7
      empty username
      empty password
      both empty
      invalid email
      quantity floor
      no badge when empty
      login reachable as guest
    Checkout 1
      shipping → payment → review → complete
```

| Class | # | What it checks | Reset |
|---|---:|---|---|
| `LoginTest` | 4 | valid login reaches the catalog; wrong password / username leave the app on login or catalog (the demo app uses mock auth); logout returns to guest | full |
| `AddToCartTest` | 1 | badge count goes from *n* to *n + 1* | keeps state |
| `CartManagementTest` | 4 | quantity 2 → badge 2; minus at 1 never goes negative; two adds → 2; remove the only item → empty cart | full |
| `ProductSearchTest` | 5 | four sort orders verified on the visible list (prices compared as `BigDecimal`); detail title equals catalog title | keeps state |
| `NegativeTest` | 7 | empty / both-empty / malformed credentials; quantity floor; no "0" badge on an empty cart; login form reachable as guest | full |
| `CheckoutTest` | 1 | full purchase ends on *Checkout Complete* | full |
| `unit.UtilsTest` | 8 | price parsing (`$ 1,049.00`, rejects `$ .`), numeric not alphabetical ordering, portable dump folder, `-D` overrides | no device |

### Data the tests use

```mermaid
erDiagram
    USER ||--o{ CART_ITEM : adds
    PRODUCT ||--o{ CART_ITEM : "appears in"
    CART_ITEM }o--|| ORDER : "checked out in"
    ORDER ||--|| SHIPPING_ADDRESS : "ships to"
    ORDER ||--|| PAYMENT_CARD : "paid with"
    USER {
        string username "bob@example.com"
        string password "10203040 (public demo login)"
    }
    PRODUCT {
        string title "Sauce Labs Backpack"
        decimal price "29.99"
    }
    CART_ITEM {
        int quantity "1..n"
    }
    SHIPPING_ADDRESS {
        string fullName "Bob User"
        string city "San Francisco"
        string zip "94102"
    }
    PAYMENT_CARD {
        string number "4111 1111 1111 1111 (test VISA)"
        string expiry "12/30"
    }
```

> `bob@example.com / 10203040` is the **public demo login printed in the app itself**, not a real account.

---

## ▶️ Running Locally

**Prerequisites:** JDK 17, Maven 3.9+, Node 20, Android SDK with an emulator (or a device), Appium 2.

```bash
# 1. Appium 2 + the UiAutomator2 driver line that supports Appium 2
npm install -g appium@2
appium driver install uiautomator2@3.10.0
appium &                                   # listens on http://127.0.0.1:4723

# 2. Start an emulator, then get the app (or install it yourself from the Sauce Labs releases)
GH_TOKEN=<token> ./scripts/get-apk.sh      # downloads mda-*.apk next to pom.xml
adb install -r mda-*.apk

# 3. Run
mvn test                                   # full regression (testng.xml)
mvn test -Dsuite=testng-smoke              # AddToCartTest only
mvn test -Dsuite=testng-unit               # 8 device-free unit tests
mvn allure:serve                           # open the Allure report
```

| Setting | `-D` property | Env var | Default |
|---|---|---|---|
| Appium server | `appium.url` | `APPIUM_URL` | `http://127.0.0.1:4723` |
| Device | `device.name` | `DEVICE_NAME` | `emulator-5554` |
| APK to install | `app.path` | `APP_PATH` | *(empty: use the installed app)* |
| Dump folder | `page.source.dir` | | `target/page-sources` |

---

## 📁 Project Structure

```text
AppiumMobileAutomation/
├── .github/workflows/ci.yml       # unit job + Android 13 emulator job
├── scripts/get-apk.sh             # downloads the latest My Demo App APK
├── pom.xml                        # java-client 9.2.2 · TestNG 7.10.2 · surefire 3.2.5 · Allure
├── src/main/java/
│   ├── pages/                     # LoginPage · HomePage · ProductPage · CartPage · CheckoutPage
│   └── utils/                     # AppConfig · Gestures · Prices · Diagnostics
└── src/test/
    ├── java/base/BaseTest.java    # driver per test, failure screenshot + page source
    ├── java/tests/                # 6 device test classes (22 tests)
    ├── java/unit/UtilsTest.java   # 8 unit tests
    └── resources/                 # testng.xml · testng-smoke.xml · testng-unit.xml
```

---

## 🧹 Maintenance Log

| Area | Before | Now |
|---|---|---|
| Suite wiring | surefire 2.22 with **no `suiteXmlFiles`** → only the `assertTrue(true)` SmokeTest ran | surefire 3.2.5 runs `src/test/resources/${suite}.xml` (default: full regression) |
| TestNG | 7.4.0 (inherited `@BeforeMethod` bug) | 7.10.2 |
| Diagnostics | page sources written to a hard-coded `/Users/…/eclipse-workspace/…` path | `target/page-sources/`; screenshot + UI XML saved for every failed test |
| Capabilities | URL, device and app hard-coded | `-D` / env overrides; CI installs the APK via `APP_PATH` |
| Small screens | `findElement` on +/−, zip, country and buttons failed below the fold | `Gestures.scrollTo` swipes until visible |
| Prices | `Double.parseDouble` after stripping `$` | `utils.Prices` (`BigDecimal`, handles `1,049.00`, rejects junk) |
| Test card | expiry `12/25` (already in the past) | `12/30` |
| Repo hygiene | `test-output/`, 9 debug XML dumps and logs committed | removed; `.gitignore` added |
| README | listed test classes that don't exist, claimed "23/23" and saucedemo.com as the app | rewritten from the code and the CI run |
| CI | none | GitHub Actions: unit job + Android 13 emulator job |

---

## 📈 Roadmap

- [x] Android emulator CI on every push
- [x] Failure screenshots + UI-hierarchy dumps as artifacts
- [ ] Publish the Allure report to GitHub Pages
- [ ] iOS run of the same flows (My Demo App iOS build)
- [ ] Data-driven login cases from a CSV / `@DataProvider`
- [ ] Run on a cloud device grid (Sauce Labs / BrowserStack) via `APPIUM_URL`

---

## 🔗 Related Projects

| Repo | What it is |
|---|---|
| [WebdriverIO-Native-DemoApp-MobileTestAutomationJavaAppiumTestNG](https://github.com/rbchy/WebdriverIO-Native-DemoApp-MobileTestAutomationJavaAppiumTestNG) | Appium + TestNG on WebdriverIO's React Native demo app, same emulator CI |
| [NorthFace_AutomationExercise-BDD-Test-Automation](https://github.com/rbchy/NorthFace_AutomationExercise-BDD-Test-Automation) | Selenium + Cucumber BDD web suite |
| [Guru99TelTest](https://github.com/rbchy/Guru99TelTest) | Selenium + TestNG web tests |
| [AmazonSeleniumTest](https://github.com/rbchy/AmazonSeleniumTest) | Selenium web tests with bot-wall detection |
| [rbchy](https://github.com/rbchy/rbchy) | Profile and full project list |

---

## ⚠️ Disclaimer

Educational / portfolio project. *My Demo App* is published by Sauce Labs for test-automation practice; this repo is not affiliated with Sauce Labs. The login and the card number are public demo values.

---

## 👤 Author

<div align="center">

**Ranajit Baran Chowdhury (RB Chowdhury)**
QA Automation Engineer / SDET · West Norriton, Pennsylvania, USA

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rbchy/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/rbchy)
[![Website](https://img.shields.io/badge/Website-rbchy.github.io%2Frbchy-0d9488?style=for-the-badge&logo=githubpages&logoColor=white)](https://rbchy.github.io/rbchy/)
[![Portfolio](https://img.shields.io/badge/Portfolio-Website-blueviolet?style=for-the-badge&logo=wix&logoColor=white)](https://rbc6543.wixsite.com/rbc-portfolio)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:chyranajit@gmail.com)

⭐ *If this project helps you, a star on the repo is appreciated.*

</div>
