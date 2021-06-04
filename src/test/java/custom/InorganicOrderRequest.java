package custom;

import common.page.header.PageHeaderDto;
import core.CoreComponents;
import core.TestBase;
import core.users.User;
import login.LoginComponent;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import standards.custom.OrderRequestComponent;

@Slf4j
public class InorganicOrderRequest extends TestBase {

    private static final String desiredConcentrationForAll = "10";
    private static final String additionalNote = "Please deliver ASAP";
    private CoreComponents coreComponents;
    private User orderRequester;

    @Test
    @Parameters({"env", "browserName", "browserMode"})
    public void startBrowser(String env, String browserName, String browserMode) throws Exception {
        coreComponents = new CoreComponents(returnInitComponentsMap(env, browserName, browserMode, CoreComponents.ORDERS));
        orderRequester = User.createStandardUser();
    }

    @Test(priority = 1, dependsOnMethods = "startBrowser")
    public void gotoCustomSolutions() {
        coreComponents.login().visitCustomSolutionsPage(LoginComponent.CUSTOM_SOLUTIONS_PAGE, currentBrowserMode);
    }

    @Test(priority = 2, dependsOnMethods = "gotoCustomSolutions")
    public void verifyIntendedPageWasReachedByCustomTitle() {
        coreComponents.pageHeaderComponent().verifyCustomTitle(PageHeaderDto.CUSTOM_SOLUTION.getPageTitle());
    }

    @Test(priority = 3, dependsOnMethods = "verifyIntendedPageWasReachedByCustomTitle")
    public void verifyOrganicIsAlreadySelectedAsDefaultOption() {
        coreComponents.orderRequestComponent().verifyOrganicOrInorganicIsSetAsTheDefaultOption(OrderRequestComponent.ORGANIC_CUSTOM_SOLUTION);
    }

    @Test(priority = 4, dependsOnMethods = "verifyIntendedPageWasReachedByCustomTitle")
    public void selectInorganicAndClickContinue() {
        coreComponents.orderRequestComponent().selectInorganic();
        coreComponents.orderRequestComponent().clickContinueButton();
    }

    @Test(priority = 5, dependsOnMethods = "selectInorganicAndClickContinue")
    public void addElements() {
        coreComponents.orderRequestComponent().addRandomNumberOfElements();
    }

    @Test(priority = 6, dependsOnMethods = "addElements")
    public void setComponentConcentrationForAll() {
        coreComponents.orderRequestComponent().setConcentrationForAllComponents(desiredConcentrationForAll);
    }

    @Test(priority = 7, dependsOnMethods = "setComponentConcentrationForAll")
    public void selectInorganicPackagingType() {
        coreComponents.orderRequestComponent().selectRandomInorganicPackagingType();
    }

    @Test(priority = 8, dependsOnMethods = "selectInorganicPackagingType")
    public void selectInorganicUnitVolume() {
        coreComponents.orderRequestComponent().selectInorganicUnitVolume();
    }

    @Test(priority = 9, dependsOnMethods = "selectInorganicUnitVolume")
    public void selectInorganicDataAnalyticalTechnique() {
        coreComponents.orderRequestComponent(). selectRandomInorganicDataAnalyticalTechnique();
    }

    @Test(priority = 10, dependsOnMethods = "selectInorganicDataAnalyticalTechnique")
    public void clickOnInorganicSaveAndContinue() {
        coreComponents.orderRequestComponent().clickOnInorganicSaveAndContinueButton();
    }

    @Test(priority = 11, dependsOnMethods = "clickOnInorganicSaveAndContinue")
    public void addCommentOnFurtherDetailsAndRequestsSection() {
        coreComponents.orderRequestComponent().addCommentOnFurtherDetailsAndRequests(additionalNote);
    }

    @Test(priority = 12, dependsOnMethods = "clickOnInorganicSaveAndContinue")
    public void clickOnSaveAndContinueOnAdditionalNotes() {
        coreComponents.orderRequestComponent().clickOnSaveAndContinueOnAdditionalNotes();
    }

    @Test(priority = 13, dependsOnMethods = "clickOnSaveAndContinueOnAdditionalNotes")
    public void fillInCustomerDetails() {
        coreComponents.orderRequestComponent().fillInCustomerDetailsAndClickSave(orderRequester);
    }

    @Test(priority = 14, dependsOnMethods = "clickOnSaveAndContinueOnAdditionalNotes")
    public void fillInCustomerBillingAddress() {
        coreComponents.orderRequestComponent().fillInBillingAddressAndClickSaveAndViewSummary(orderRequester);
    }

    @Test(priority = 15, dependsOnMethods = "clickOnSaveAndContinueOnAdditionalNotes")
    public void verifyReachedQuotePage() {
        coreComponents.orderRequestComponent().verifyWeReachedQuotePageByLocatingSendQuoteRequestButton();
    }
}