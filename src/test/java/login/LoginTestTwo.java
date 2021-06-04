package login;

import common.page.header.PageHeaderDto;
import core.CoreComponents;
import core.TestBase;
import core.users.User;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Slf4j
public class LoginTestTwo extends TestBase {

    private CoreComponents coreComponents;
    private User loginTestUser;

    @Test
    @Parameters({"env", "browserName", "browserMode"})
    public void startBrowser(String env, String browserName, String browserMode) throws Exception {
        coreComponents = new CoreComponents(returnInitComponentsMap(env, browserName, browserMode, CoreComponents.LOGIN));
        loginTestUser = User.createStandardUser();
        currentBrowserMode=browserMode;
    }

    @Test(priority = 1, dependsOnMethods = "startBrowser")
    public void gotoHomePageAndAttemptToLogin() {
        coreComponents.login().logInLGC(LGC_BASE_ADDRESS, User.LOGIN_USERNAME_TWO, User.LOGIN_PASSWORD_TWO, currentBrowserMode);
    }

    @Test(priority = 2, dependsOnMethods = "gotoHomePageAndAttemptToLogin")
    public void verifyReachedLoginPage() {
        coreComponents.pageHeaderComponent().verifyPageTitle(PageHeaderDto.LANDING.getPageTitle());
    }

    @Test(priority = 3, dependsOnMethods = "verifyReachedLoginPage")
    public void verifyLoginViaFullName() {
        coreComponents.login().verifyUserSuccessfullyLoggedIn(User.LOGIN_FULL_NAME_TWO);
    }

    @Test(priority = 4, dependsOnMethods = "verifyReachedLoginPage")
    public void logOut() {
        coreComponents.login().clickLogOutButton();
    }

    @Test(priority = 5, dependsOnMethods = "logOut")
    public void verifyUserIsLoggedOut() {
        coreComponents.login().verifyUserSuccessfullyLoggedOut();
        log.info("Test user with first name: {} and last name: {} has successfully logged out.", loginTestUser.getFirstName(), loginTestUser.getLastName());
    }
}
