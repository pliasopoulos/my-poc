package custom;

import common.page.header.PageHeaderDto;
import core.CoreComponents;
import core.ExcelHelper;
import core.TestBase;
import core.users.User;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import standards.custom.OrderRequestComponent;

import java.awt.*;

@Slf4j
public class OrganicOrderRequest extends TestBase {

    private static final int expectedUploadedComponents = 17;
    private static final String desiredConcentrationForAll = "10";
    private static final String additionalNote = "Please deliver ASAP";
    private CoreComponents coreComponents;
    private User orderRequester;

    @Test
    @Parameters({"env", "browserName", "browserMode"})
    public void startBrowser(String env, String browserName, String browserMode) {
        coreComponents = new CoreComponents(returnInitComponentsMap(env, browserName, browserMode, CoreComponents.ORDERS));
        orderRequester = User.createStandardUser();
    }

    @Test(priority = 1, dependsOnMethods = "startBrowser")
    public void gotoCustomSolutions() {
        coreComponents.login().visitCustomSolutionsPage();
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
    public void clickContinue() {
        coreComponents.orderRequestComponent().clickContinueButton();
    }

    @Test(priority = 5, dependsOnMethods = "clickContinue")
    public void uploadOrganicOrderExcelFile() throws InterruptedException, AWTException {
        coreComponents.excelHelper().uploadExcelFile(ExcelHelper.CUSTOM_ORGANIC_ORDER);
    }

    @Test(priority = 6, dependsOnMethods = "uploadOrganicOrderExcelFile")
    public void verifyExpectedComponentsUploadedSuccessfully() {
        coreComponents.orderRequestComponent().verifyExpectedComponentsUploaded(expectedUploadedComponents);
    }

    @Test(priority = 7, dependsOnMethods = "verifyExpectedComponentsUploadedSuccessfully")
    public void fillInMissingComponentNameOrComponentsWithMultipleMatches() {
        coreComponents.orderRequestComponent().fillInComponentNameOrComponentsWhichHaveMultipleMatches();
    }

    @Test(priority = 8, dependsOnMethods = "fillInMissingComponentNameOrComponentsWithMultipleMatches")
    public void addComponents() {
        coreComponents.orderRequestComponent().clickOnAddComponentsListButton();
    }

    @Test(priority = 9, dependsOnMethods = "addComponents")
    public void setComponentConcentrationForAll() {
        coreComponents.orderRequestComponent().setConcentrationForAllComponents(desiredConcentrationForAll);
    }

    @Test(priority = 10, dependsOnMethods = "setComponentConcentrationForAll")
    public void selectPackagingType() {
        coreComponents.orderRequestComponent().selectRandomOrganicPackagingType();
    }

    @Test(priority = 11, dependsOnMethods = "selectPackagingType")
    public void selectUnitVolume() {
        coreComponents.orderRequestComponent().selectOrganicUnitVolume();
    }

    @Test(priority = 12, dependsOnMethods = "selectUnitVolume")
    public void selectDataAnalyticalTechnique() {
        coreComponents.orderRequestComponent(). selectRandomOrganicDataAnalyticalTechnique();
    }

    @Test(priority = 13, dependsOnMethods = "selectDataAnalyticalTechnique")
    public void clickOnSaveAndContinue() {
        coreComponents.orderRequestComponent().clickOnOrganicSaveAndContinueButton();
    }

    @Test(priority = 14, dependsOnMethods = "clickOnSaveAndContinue")
    public void addCommentOnFurtherDetailsAndRequestsSection() {
        coreComponents.orderRequestComponent().addCommentOnFurtherDetailsAndRequests(additionalNote);
    }

    @Test(priority = 15, dependsOnMethods = "clickOnSaveAndContinue")
    public void clickOnSaveAndContinueOnAdditionalNotes() {
        coreComponents.orderRequestComponent().clickOnSaveAndContinueOnAdditionalNotes();
    }

    @Test(priority = 16, dependsOnMethods = "clickOnSaveAndContinueOnAdditionalNotes")
    public void fillInCustomerDetails() {
        coreComponents.orderRequestComponent().fillInCustomerDetailsAndClickSave(orderRequester);
    }

    @Test(priority = 17, dependsOnMethods = "clickOnSaveAndContinueOnAdditionalNotes")
    public void fillInCustomerBillingAddress() {
        coreComponents.orderRequestComponent().fillInBillingAddressAndClickSaveAndViewSummary(orderRequester);
    }

    @Test(priority = 18, dependsOnMethods = "clickOnSaveAndContinueOnAdditionalNotes")
    public void verifyReachedQuotePage() {
        coreComponents.orderRequestComponent().verifyWeReachedQuotePageByLocatingSendQuoteRequestButton();
    }
}