package browser.setup;

import org.openqa.selenium.WebDriver;

public interface IBrowserSetup {
    WebDriver browserInit(String browserName, String browserMode);
}
