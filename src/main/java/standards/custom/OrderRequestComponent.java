package standards.custom;

import browser.BrowserComponents;
import core.TestBase;
import core.users.User;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;


@Slf4j
public class OrderRequestComponent extends TestBase {
    public static final String ORGANIC_CUSTOM_SOLUTION = "organic";
    public static final By CONTINUE_BUTTON = By.xpath("//button[contains(@id,'SelectCustomSolution')]");
    private static final By COMPONENTS_WITH_MULTIPLE_MATCHES_OR_WITHOUT_A_NAME = By.xpath("//input[contains(@placeholder,'multiple') or contains(@placeholder,'enter component name')]");
    private static final By COMPONENTS_DROPDOWN_LIST_SELECTION = By.xpath("//div[@class='el-scrollbar']//li[contains(@class,'select-dropdown__item')]");
    private static final By ADD_COMPONENTS_BUTTON = By.xpath("(//button[contains(@class,'add-all')])[1]");
    private static final By SET_CONCENTRATION_FOR_ALL_FIELD = By.xpath("(//input[@id='orderRequestTopAnalyteForm'])[1]");
    private static final By ORGANIC_PACKAGING_TYPES = By.xpath("//label[contains(@id,'cqOrganicPackaginType')]");
    private static final By INORGANIC_PACKAGING_TYPES = By.xpath("//label[contains(@id,'cqInorganicPackaginType')]");
    private static final By ORGANIC_DATA_ANALYTICAL_TECHNIQUES = By.xpath("//label[contains(@id,'cqOrganicDataAnalyticalTechnique')]");
    private static final By INORGANIC_DATA_ANALYTICAL_TECHNIQUES = By.xpath("//label[contains(@id,'cqAnalyticalTechniqueNotKnown')]");
    private static final By ORGANIC_SAVE_AND_CONTINUE_BUTTON = By.xpath("//button[@id='cqConfigureCustomSolutionTypeBtn']");
    private static final By INORGANIC_SAVE_AND_CONTINUE_BUTTON = By.xpath("//button[@id='cqConfigureCustomSolutionTypeBtn']");
    private static final By FURTHER_DETAILS_OR_REQUESTS_TEXT_AREA = By.xpath("//textarea[@id='cqAdditionalNotesTextArea']");
    private static final By SAVE_AND_CONTINUE_BUTTON_ADDITIONAL_NOTES = By.xpath("//button[@id='cqAddAdditionalNotesBtn']");
    private static final By CUSTOMER_TITLE_DROPDOWN = By.xpath("//input[@id='cqCustomerDetailsTitle']");
    private static final By CUSTOMER_FIRST_NAME = By.xpath("//input[@id='cqCustomerDetailsFirstName']");
    private static final By CUSTOMER_LAST_NAME = By.xpath("//input[@id='cqCustomerDetailsLastName']");
    private static final By CUSTOMER_ORGANISATION = By.xpath("//input[@id='cqCustomerDetailsOrganization']");
    private static final By CUSTOMER_COUNTRY = By.xpath("//input[@id='cqCustomerDetailsCountry']");
    private static final By CUSTOMER_BILLING_COUNTRY = By.xpath("//input[@id='cqBillingAddressCountry']");
    private static final By CUSTOMER_EMAIL = By.xpath("//input[@id='cqCustomerDetailsEmailAddress']");
    private static final By CUSTOMER_COUNTRY_CODE = By.xpath("//input[@id='cqCustomerDetailsCountryCode']");
    private static final By CUSTOMER_TELEPHONE_NUMBER = By.xpath("//input[@id='cqCustomerDetailsPhone']");
    private static final By CUSTOMER_JOB_ROLE = By.xpath("//input[@id='cqCustomerDetailsJobRole']");
    private static final By ACCEPT_TERMS_AND_CONDITIONS_CHECKBOX = By.xpath("//label[@id='cqCustomerDetailsPolicy']");
    private static final By SAVE_AND_CONTINUE_BUTTON_CUSTOMER_DETAILS = By.xpath("//button[@id='cqCustomerDetailsSaveBtn']");
    private static final By CUSTOMER_ADDRESS_LINE_1 = By.xpath("//input[@id='cqBillingAddressLine1']");
    private static final By CUSTOMER_CITY = By.xpath("//input[@id='cqBillingAddressCity']");
    private static final By CUSTOMER_POSTCODE = By.xpath("//input[@id='cqBillingAddressPostCode']");
    private static final By SAVE_AND_VIEW_SUMMARY = By.xpath("//button[@id='cqDeliveryInformationBtn']");
    private static final By SEND_QUOTE_REQUEST = By.xpath("//button[@id='cqStepFlowEditStep6']");
    private static final By INORGANIC_CUSTOM_SOLUTION_SELECTION_BUTTON = By.xpath("//label[@id='cqInorganicRadioBtn']");
    private static final By ADD_ELEMENTS_BUTTON = By.xpath("//div[@id='cqInorganicSelector']");
    private static final By ALL_AVAILABLE_AND_NOT_ALREADY_SELECTED_ELEMENTS = By.xpath("//li[contains(@id,'cqPeriodicElement') and not(contains(@class,'disabled')) and not(contains(@class,'selected'))]");
    private static final By ORGANIC_UNIT_VOLUME_DROPDOWN = By.xpath("//input[@id='cqOrganicDataUnitVolume']");
    private static final By INORGANIC_UNIT_VOLUME_DROPDOWN = By.xpath("//input[@id='cqInorganicDataUnitVolume']");
    private static final By ADD_ELEMENTS_COMPONENTS_LIST_BUTTON = By.xpath("//button[@id='cqSelectPeriodicTableElementsBtn']");
    private static String ORGANIC_OR_INORGANIC_CUSTOM_SOLUTION_TYPE_RADIO_BUTTON = "//label[@id='%sRadioBtn' and contains(@class,'checked')]";
    private static String EXPECTED_UPLOADED_COMPONENTS = "//div[contains(@aria-label,'Components (%s')]";
    private final BrowserComponents browserComponents;

    public OrderRequestComponent(BrowserComponents browserComponents) {
        this.browserComponents = browserComponents;
    }

    public void verifyOrganicOrInorganicIsSetAsTheDefaultOption(String organicOrInorganic) {
        assertBoolean(true, browserComponents.elements().isElementPresent(By.xpath(String.format(ORGANIC_OR_INORGANIC_CUSTOM_SOLUTION_TYPE_RADIO_BUTTON, organicOrInorganic)), 1), "Failed to verify that organic was the default option");
    }

    public void clickContinueButton() {
        browserComponents.elements().waitAndClickElement(CONTINUE_BUTTON);
    }

    public void verifyExpectedComponentsUploaded(int expectedUploadedComponents) {
        browserComponents.elements().waitForElementFetchIfPresent(By.xpath(String.format(EXPECTED_UPLOADED_COMPONENTS, expectedUploadedComponents)));
        log.info("Successfully verified {} components uploaded", expectedUploadedComponents);
    }

    public void fillInComponentNameOrComponentsWhichHaveMultipleMatches() {
        if (returnMissingComponentsIfAny()) {
            List<WebElement> missingComponents = browserComponents.elements().waitForElementsFetchIfPresent(COMPONENTS_WITH_MULTIPLE_MATCHES_OR_WITHOUT_A_NAME);
            for (WebElement missingComponent : missingComponents) {
                missingComponent.click();
                if (missingComponent.getAttribute("placeholder").contains("multiple")) {
                    selectRandomComponentFromDropbox();
                } else {
                    typeMissingComponentName(missingComponent, "TestComponent");
                }
            }
        }
    }

    private boolean returnMissingComponentsIfAny() {
        return browserComponents.elements().waitForElementsFetchIfPresent(COMPONENTS_WITH_MULTIPLE_MATCHES_OR_WITHOUT_A_NAME).size() > 0;
    }

    private void selectRandomComponentFromDropbox() {
        List<WebElement> dropdownSelection = browserComponents.elements().waitForElementsFetchIfPresent(COMPONENTS_DROPDOWN_LIST_SELECTION);
        browserComponents.navigation().clickRandomSelection(dropdownSelection);
    }

    private void typeMissingComponentName(WebElement componentElement, String componentName) {
        browserComponents.elements().clickElementClearFieldAndTypeText(componentElement, componentName);
    }

    public void clickOnAddComponentsListButton() {
        browserComponents.elements().waitAndClickElement(ADD_COMPONENTS_BUTTON);
    }

    public void setConcentrationForAllComponents(String desiredConcentration) {
        browserComponents.elements().clickElementClearFieldTypeTextAndHitEnter(SET_CONCENTRATION_FOR_ALL_FIELD, desiredConcentration);
    }

    public void selectRandomOrganicPackagingType() {
        List<WebElement> packackingChoices = browserComponents.elements().waitForElementsFetchIfPresent(ORGANIC_PACKAGING_TYPES);
        browserComponents.navigation().clickRandomSelection(packackingChoices);
    }

    public void selectRandomInorganicPackagingType() {
        List<WebElement> packackingChoices = browserComponents.elements().waitForElementsFetchIfPresent(INORGANIC_PACKAGING_TYPES);
        browserComponents.navigation().clickRandomSelection(packackingChoices);
    }

    public void selectOrganicUnitVolume() {
        browserComponents.elements().waitAndClickElement(ORGANIC_UNIT_VOLUME_DROPDOWN);
        browserComponents.navigation().clickRandomSelectionFromDropdownList();
    }

    public void selectInorganicUnitVolume() {
        browserComponents.elements().waitAndClickElement(INORGANIC_UNIT_VOLUME_DROPDOWN);
        browserComponents.navigation().clickRandomSelectionFromDropdownList();
    }

    public void selectRandomOrganicDataAnalyticalTechnique() {
        List<WebElement> dataAnalyticalTechniques = browserComponents.elements().waitForElementsFetchIfPresent(ORGANIC_DATA_ANALYTICAL_TECHNIQUES);
        browserComponents.navigation().clickRandomSelection(dataAnalyticalTechniques);
    }

    public void selectRandomInorganicDataAnalyticalTechnique() {
        List<WebElement> dataAnalyticalTechniques = browserComponents.elements().waitForElementsFetchIfPresent(INORGANIC_DATA_ANALYTICAL_TECHNIQUES);
        browserComponents.navigation().clickRandomSelection(dataAnalyticalTechniques);
    }

    public void clickOnOrganicSaveAndContinueButton() {
        browserComponents.elements().waitAndClickElement(ORGANIC_SAVE_AND_CONTINUE_BUTTON);
    }

    public void clickOnInorganicSaveAndContinueButton() {
        browserComponents.elements().waitAndClickElement(INORGANIC_SAVE_AND_CONTINUE_BUTTON);
    }

    public void addCommentOnFurtherDetailsAndRequests(String comment) {
        browserComponents.elements().clickElementClearFieldAndTypeText(FURTHER_DETAILS_OR_REQUESTS_TEXT_AREA, comment);
    }

    public void clickOnSaveAndContinueOnAdditionalNotes() {
        browserComponents.elements().waitAndClickElement(SAVE_AND_CONTINUE_BUTTON_ADDITIONAL_NOTES);
    }

    public void fillInCustomerDetailsAndClickSave(User user) {
        selectRandomTitle();
        browserComponents.elements().clickElementClearFieldAndTypeText(CUSTOMER_FIRST_NAME, user.getFirstName());
        browserComponents.elements().clickElementClearFieldAndTypeText(CUSTOMER_LAST_NAME, user.getLastName());
        browserComponents.elements().clickElementClearFieldAndTypeText(CUSTOMER_ORGANISATION, user.getOrganisation());
        browserComponents.elements().clickElementClearFieldTypeTextAndHitEnter(CUSTOMER_COUNTRY, user.getFullAddress().getCountry());
        browserComponents.elements().clickElementClearFieldAndTypeText(CUSTOMER_EMAIL, user.getEmail());
        selectRandomCountryCode();
        browserComponents.elements().clickElementClearFieldAndTypeText(CUSTOMER_TELEPHONE_NUMBER, "1234567890");
        selectRandomJobRole();
        acceptTsAndCs();
        clickOnSaveButtonOnCustomerDetailsPage();
    }

    private void selectRandomTitle() {
        browserComponents.elements().waitAndClickElement(CUSTOMER_TITLE_DROPDOWN);
        browserComponents.navigation().clickRandomSelectionFromDropdownList();
    }

    private void selectRandomCountryCode() {
        browserComponents.elements().waitAndClickElement(CUSTOMER_COUNTRY_CODE);
        browserComponents.navigation().clickRandomSelectionFromDropdownList();
    }

    private void selectRandomJobRole() {
        browserComponents.elements().waitAndClickElement(CUSTOMER_JOB_ROLE);
        browserComponents.navigation().clickRandomSelectionFromDropdownList();
    }

    private void acceptTsAndCs() {
        browserComponents.elements().waitAndClickElement(ACCEPT_TERMS_AND_CONDITIONS_CHECKBOX);
    }

    private void clickOnSaveButtonOnCustomerDetailsPage() {
        browserComponents.elements().waitAndClickElement(SAVE_AND_CONTINUE_BUTTON_CUSTOMER_DETAILS);
    }

    public void fillInBillingAddressAndClickSaveAndViewSummary(User user) {
        browserComponents.elements().clickElementClearFieldAndTypeText(CUSTOMER_ADDRESS_LINE_1, user.getFullAddress().getNumber() + " " + user.getFullAddress().getStreet());
        browserComponents.elements().clickElementClearFieldTypeTextAndHitEnter(CUSTOMER_BILLING_COUNTRY, user.getFullAddress().getCountry());
        browserComponents.elements().clickElementClearFieldAndTypeText(CUSTOMER_CITY, user.getFullAddress().getCity());
        browserComponents.elements().clickElementClearFieldAndTypeText(CUSTOMER_POSTCODE, user.getFullAddress().getPostCode());
        browserComponents.elements().waitAndClickElement(SAVE_AND_VIEW_SUMMARY);
    }

    public void verifyWeReachedQuotePageByLocatingSendQuoteRequestButton() {
        browserComponents.elements().isElementPresent(SEND_QUOTE_REQUEST, 3);
    }

    public void selectInorganic() {
        browserComponents.elements().waitAndClickElement(INORGANIC_CUSTOM_SOLUTION_SELECTION_BUTTON);
    }

    public void addRandomNumberOfElements() {
        clickOnAddElementsButton();
        List<WebElement> allAvailableAndNotAlreadySelectedElements = browserComponents.elements().waitForElementsFetchIfPresent(ALL_AVAILABLE_AND_NOT_ALREADY_SELECTED_ELEMENTS);
        Random generateRandomNumberOfElementsToBeSelected = new Random();
        int randomNumberOfElements = generateRandomNumberOfElementsToBeSelected.nextInt(allAvailableAndNotAlreadySelectedElements.size());
        for (int i = 0; i < randomNumberOfElements; i++) {
            Random generateRandomArrayInteger = new Random();
            int randomArrayInteger = generateRandomArrayInteger.nextInt(allAvailableAndNotAlreadySelectedElements.size());
            allAvailableAndNotAlreadySelectedElements.get(randomArrayInteger).click();
            allAvailableAndNotAlreadySelectedElements = browserComponents.elements().waitForElementsFetchIfPresent(ALL_AVAILABLE_AND_NOT_ALREADY_SELECTED_ELEMENTS);
        }
        clickOnElementsToComponentsListButton();
    }

    private void clickOnAddElementsButton() {
        browserComponents.elements().waitAndClickElement(ADD_ELEMENTS_BUTTON);
    }

    private void clickOnElementsToComponentsListButton() {
        browserComponents.elements().waitAndClickElement(ADD_ELEMENTS_COMPONENTS_LIST_BUTTON);
    }
}
