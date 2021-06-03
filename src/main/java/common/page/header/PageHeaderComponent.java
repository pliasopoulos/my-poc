package common.page.header;

import browser.BrowserComponents;
import core.TestBase;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

@Slf4j
public class PageHeaderComponent extends TestBase {

    private final BrowserComponents browserComponents;
    private static final By CUSTOM_TITLE = By.xpath("//h1");
    private static final By CUSTOM_SOLUTIONS_LINK = By.xpath("(//a[contains(.,'Custom Solutions')])[1]");

    public PageHeaderComponent(BrowserComponents browserComponents){
        this.browserComponents = browserComponents;
    }

    public void verifyPageTitle(String expectedPageTitle){
        String currentPageTitle = browserComponents.navigation().getPageTitle();
        assertEquals(expectedPageTitle, currentPageTitle, "Failed to verify page title, expected: "+expectedPageTitle+", but got instead: "+currentPageTitle);
        log.info("Successfully verified that we reached intended page by the following title: "+currentPageTitle);
    }

    public void verifyCustomTitle(String expectedCustomTitle){
        String currentCustomTitle = browserComponents.elements().waitForElementFetchIfPresent(CUSTOM_TITLE).getText();
        assertEquals(expectedCustomTitle, currentCustomTitle, "Failed to verify custom title, expected: "+expectedCustomTitle+", but got instead: "+currentCustomTitle);
        log.info("Successfully verified that we reached intended page by the following <CUSTOM> title: "+currentCustomTitle);
    }

    public void clickOnCustomSolutions() {
        browserComponents.elements().waitAndClickElement(CUSTOM_SOLUTIONS_LINK);
    }
}
