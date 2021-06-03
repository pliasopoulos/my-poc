package browser.elements;

import core.TestBase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@Slf4j
@Getter
public class BrowserElements extends TestBase implements IBrowserElements {

    private static final int WAIT_FOR_ELEMENT_PRESENT_TIMEOUT = 8;
    private static final int WAIT_FOR_ELEMENT_NOT_PRESENT_TIMEOUT = 2;

    private WebDriver webDriver;

    public BrowserElements(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public WebElement waitForElementFetchIfPresent(By by) {
        if (isElementPresent(by, WAIT_FOR_ELEMENT_PRESENT_TIMEOUT)) {
            return findElementBy(by);
        } else return null;
    }

    @Override
    public boolean isElementPresent(By by, int timeOut) {
        boolean isPresent;
        WebDriverWait wait = new WebDriverWait(getWebDriver(), WAIT_FOR_ELEMENT_PRESENT_TIMEOUT);
        try {
            isPresent = wait.until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
        } catch (TimeoutException timeoutException) {
            isPresent = false;
            log.info("Failed to verify that element {} was visible after {} sec", by, WAIT_FOR_ELEMENT_PRESENT_TIMEOUT);
        }
        return isPresent;
    }

    @Override
    public WebElement findElementBy(By by) {
        return getWebDriver().findElement(by);
    }

    @Override
    public void waitAndClickElement(By by) {
        WebElement element = waitForElementFetchIfPresent(by);
        element.click();
    }

    @Override
    public void clickElementClearFieldAndTypeText(By by, String textToType) {
        WebElement element = waitForElementFetchIfPresent(by);
        element.click();
        element.clear();
        element.sendKeys(textToType);
        log.debug("Typed..: {} on this element..: {}", textToType, by);
    }

    @Override
    public void clickElementClearFieldTypeTextAndHitEnter(By by, String textToType) {
        WebElement element = waitForElementFetchIfPresent(by);
        element.click();
        element.clear();
        element.sendKeys(textToType);
        element.sendKeys(Keys.ENTER);
        log.debug("Typed..: {} on this element..: {}", textToType, by);
    }

    @Override
    public void clickElementClearFieldAndTypeText(WebElement webElement, String textToType) {
        webElement.click();
        webElement.clear();
        webElement.sendKeys(textToType);
        log.debug("Typed..: {} on this element..: {}", textToType, webElement);
    }

    @Override
    public List<WebElement> waitForElementsFetchIfPresent(By by) {
        if (isElementPresent(by, WAIT_FOR_ELEMENT_PRESENT_TIMEOUT)) {
            return findElementsBy(by);
        } else return null;
    }

    @Override
    public List<WebElement> findElementsBy(By by) {
        return getWebDriver().findElements(by);
    }
}