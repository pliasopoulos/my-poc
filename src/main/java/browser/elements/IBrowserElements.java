package browser.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface IBrowserElements {
    WebElement waitForElementFetchIfPresent(By by);
    boolean isElementPresent(By by, int timeOut);
    WebElement findElementBy(By by);
    void waitAndClickElement(By by);
    void clickElementClearFieldAndTypeText(By by, String textToType);
    List<WebElement> waitForElementsFetchIfPresent(By by);
    List<WebElement> findElementsBy(By by);
    void clickElementClearFieldAndTypeText(WebElement webElement, String textToType);
    void clickElementClearFieldTypeTextAndHitEnter(By by, String textToType);
}
