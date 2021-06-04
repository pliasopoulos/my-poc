package browser.navigation;

import browser.elements.BrowserElements;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Random;

@Getter
@Slf4j
public class BrowserNavigation implements IBrowserNavigation {
    private static final By ALL_AVAILABLE_SELECTIONS_FROM_OPENED_DROPDOWN = By.xpath("//div[contains(@class,'select-dropdown') and @x-placement]//li[contains(@class,'select-dropdown__item')]");
    private final BrowserElements browserElements;
    private WebDriver webDriver;

    public BrowserNavigation(WebDriver driver) {
        this.webDriver = driver;
        browserElements = new BrowserElements(driver);
    }

    @Override
    public void gotoPage(String httpAddress, String browserMode) {
        if (browserMode.equals("headless") || browserMode.equals("grid")) {
            getWebDriver().manage().window().setSize(new Dimension(2555, 1385)); //needed for headless
        } else {
            getWebDriver().manage().window().maximize();
        }
        getWebDriver().get(httpAddress);
    }

    @Override
    public void navigateToElement(WebElement element) {
        try {
            Actions action = new Actions(getWebDriver());
            action.moveToElement(element).build().perform();
        } catch (StaleElementReferenceException sere) {
            Actions action = new Actions(getWebDriver());
            action.moveToElement(element).build().perform();
        }
    }

    @Override
    public void hoverOverAndClickElement(WebElement elementToBeHoveredAndClicked) {
        navigateToElement(elementToBeHoveredAndClicked);
        hoverOverElement(elementToBeHoveredAndClicked);
        elementToBeHoveredAndClicked.click();
    }

    @Override
    public void hoverOverElement(WebElement element) {
        String strJavaScript = "var element = arguments[0]; var mouseEventObj = document.createEvent('MouseEvents'); mouseEventObj.initEvent( 'mouseover', true, true ); element.dispatchEvent(mouseEventObj);";
        ((JavascriptExecutor) getWebDriver()).executeScript(strJavaScript, element);
    }

    @Override
    public String getPageTitle() {
        return getWebDriver().getTitle();
    }

    @Override
    public void clickRandomSelection(List<WebElement> dropdownSelections) {
        Random generateRandomArrayInteger = new Random();
        int randomArrayInteger = generateRandomArrayInteger.nextInt(dropdownSelections.size());
        hoverOverAndClickElement(dropdownSelections.get(randomArrayInteger));
        log.info("Successfully selected randomly: {}, from this element {}", dropdownSelections.get(randomArrayInteger).getText(), dropdownSelections.get(randomArrayInteger));
    }

    @Override
    public void clickRandomSelectionFromDropdownList(){
        List<WebElement> drodownList = browserElements.waitForElementsFetchIfPresent(ALL_AVAILABLE_SELECTIONS_FROM_OPENED_DROPDOWN);
        Random generateRandomArrayInteger = new Random();
        int randomArrayInteger = generateRandomArrayInteger.nextInt(drodownList.size());
        hoverOverAndClickElement(drodownList.get(randomArrayInteger));
        log.info("Successfully selected randomly: {}, from this element {}", drodownList.get(randomArrayInteger).getText());
    }
}
