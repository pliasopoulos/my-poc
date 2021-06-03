package browser.navigation;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface IBrowserNavigation {
    void gotoPage(String httpAddress);
    void navigateToElement(WebElement element);
    void hoverOverAndClickElement(WebElement elementToBeHoveredAndClicked);
    void hoverOverElement(WebElement element);
    String getPageTitle();
    void clickRandomSelection(List<WebElement> dropdownSelections);
    void clickRandomSelectionFromDropdownList();
}
