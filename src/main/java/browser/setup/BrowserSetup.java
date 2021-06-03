package browser.setup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

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
    public WebDriver browserInit(String browserName, String browserMode) {
        WebDriver driver = null;
        if (browserName.equals("chrome") && browserMode.equals("local")) {
            WebDriverManager.chromedriver().setup();
            System.setProperty("webdriver.chrome.driver", WebDriverManager.chromedriver().getBinaryPath());

            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("start-maximized");
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--no-sandbox");
            driver = new ChromeDriver(chromeOptions);
        }
        return driver;
    }
}
