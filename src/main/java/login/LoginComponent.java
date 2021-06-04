package login;

import browser.BrowserComponents;
import core.TestBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;

@Getter
@Setter
@Slf4j
public class LoginComponent extends TestBase {

    // naming convention is NAME-TYPE-LOCATION (if needed)
    private static final By LOGIN_LINK = By.xpath("//a[@id='loginBt']");
    private static final By USERNAME_FIELD = By.xpath("//input[contains(@name,'username')]");
    private static final By PASSWORD_FIELD = By.xpath("//input[@data-vv-as='password']");
    private static final By ACCOUNT_BUTTON = By.xpath("(//button[contains(@class,'account')])[1]");
    private static final By FULL_NAME_ACCOUNT_DROPDOWN = By.xpath("(//span[contains(@class,'full-name')])[1]");
    private static final By LOGOUT_BUTTON = By.xpath("(//a[contains(@class,'LogOut')])[1]");
    private static final By LOGIN_SUBMIT_BUTTON = By.xpath("//button[@type='submit']");
    public static final String CUSTOM_SOLUTIONS_PAGE = "https://www.lgcstandards.com/GB/en/orderRequest";
    private final BrowserComponents browserComponents;

    public LoginComponent(BrowserComponents browserComponents) {
        this.browserComponents = browserComponents;
    }

    private void clickLoginLink() {
        browserComponents.elements().waitAndClickElement(LOGIN_LINK);
    }

    private void enterUserName(String username) {
        browserComponents.elements().clickElementClearFieldAndTypeText(USERNAME_FIELD, username);
    }

    private void enterPassword(String password) {
        browserComponents.elements().clickElementClearFieldAndTypeText(PASSWORD_FIELD, new String(Base64.decodeBase64(password)));
    }

    public void logInLGC(String baseHttpAdress, String username, String password, String browserMode) {
        browserComponents.navigation().gotoPage(baseHttpAdress, browserMode);
        clickLoginLink();
        enterUserName(username);
        enterPassword(password);
        clickLoginButton();
    }

    private void clickLoginButton() {
        browserComponents.elements().waitAndClickElement(LOGIN_SUBMIT_BUTTON);
    }

    private void clickAccountButton() {
        browserComponents.elements().waitAndClickElement(ACCOUNT_BUTTON);
    }

    private String returnAccountFullName() {
        return browserComponents.elements().waitForElementFetchIfPresent(FULL_NAME_ACCOUNT_DROPDOWN).getText();
    }

    void verifyUserSuccessfullyLoggedIn(String fullName) {
        clickAccountButton();
        String currentFullName = returnAccountFullName();
        assertEquals(fullName, currentFullName, "Failed to match user's full name, expected: " + fullName + ", but found: " + currentFullName);
        log.info("Successfully verified that user logged in with the following full name: [{}]", currentFullName);
    }

    void clickLogOutButton() {
        browserComponents.elements().waitAndClickElement(LOGOUT_BUTTON);
    }

    void verifyUserSuccessfullyLoggedOut() {
        assertBoolean(browserComponents.elements().isElementPresent(LOGIN_LINK, 2), true, "Failed to verify that user has successfully logged out");
    }

    public void visitCustomSolutionsPage(String page, String currentBrowserMode){
        browserComponents.navigation().gotoPage(CUSTOM_SOLUTIONS_PAGE, currentBrowserMode);
    }
}
