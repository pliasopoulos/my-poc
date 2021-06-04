package browser.setup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BrowserSetup implements IBrowserSetup{

    private static final String hub_register_address = "http://localhost:4444/wd/hub";

    private ThreadLocal<RemoteWebDriver> driverGrid;
    private void initRemoteWebDriver(RemoteWebDriver driver) {
        driverGrid.set(driver);
    }
    private RemoteWebDriver getRemoteDriver() {
        return driverGrid.get();
    }

    @Override
    public WebDriver browserInit(String browserName, String browserMode) throws Exception {
        WebDriver driver = null;
        WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver", WebDriverManager.chromedriver().getBinaryPath());
        if (browserName.equals("chrome") && browserMode.equals("local")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("start-maximized");
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--no-sandbox");
            driver = new ChromeDriver(chromeOptions);
        }
        else if (browserName.equals("chrome") && browserMode.equals("headless")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--headless");
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            chromeOptions.setExperimentalOption("prefs", prefs);
            chromeOptions.setExperimentalOption("useAutomationExtension", false);
            chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            driver = new ChromeDriver(chromeOptions);
        } else if (browserName.equals("chrome") && browserMode.equals("grid")) {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setBrowserName("chrome");
            capabilities.setVersion("ANY");

            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--test-type");
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--no-sandbox");
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            chromeOptions.setExperimentalOption("prefs", prefs);
            chromeOptions.setExperimentalOption("useAutomationExtension", false);
            chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            driverGrid = new ThreadLocal<>();
            initRemoteWebDriver(new RemoteWebDriver(new URL(hub_register_address), capabilities));
            driver = getRemoteDriver();
        } else {
            throw new Exception("Failed to init a valid browser session! Check your setting on RegressionSuite.xml");
        }
        return driver;
    }
}
