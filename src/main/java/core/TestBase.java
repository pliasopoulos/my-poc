package core;

import browser.BrowserComponents;
import browser.elements.BrowserElements;
import browser.elements.IBrowserElements;
import browser.navigation.BrowserNavigation;
import browser.navigation.IBrowserNavigation;
import browser.setup.BrowserSetup;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import common.page.header.PageHeaderComponent;
import core.users.User;
import login.LoginComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reporting.ExtentManager;
import standards.custom.OrderRequestComponent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * All the common functions used from all tests.
 * Each test class extents TestBase so we can keep the tests clean and hide complexity of the following repetitive hooks: *
 * @BeforeSuite, @AfterSuite, @BeforeClass, @AfterClass, @BeforeMethod, @AfterMethod
 */

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class TestBase {

    public static ThreadLocal<ExtentTest> parentTest;
    public static ThreadLocal<ExtentTest> childTest;
    protected static String LGC_BASE_ADDRESS;
    private static long suiteStartTime;
    private static String currentBrowser;
    private static ExtentReports extent;
    protected WebDriver driver;
    private int totalSuiteTestsRun;
    private int totalSuiteTestsPassed;
    private int totalSuiteTestsFailed;
    private int totalSuiteTestsSkipped;
    private int totalSuiteTestsWarning;
    private long suiteExecutionTime;
    public static String currentEnv;
    private String screenshotPath;
    public String currentBrowserMode;

    private static void setAllEnvironmentalVariables(String env) {
        switch (env) {
            case "dev":
                LGC_BASE_ADDRESS = "https://www.lgcstandards.dev/GB/en/";
                break;
            case "qa":
                LGC_BASE_ADDRESS = "https://www.lgcstandards.qa/GB/en/";
                break;
            case "prod":
                LGC_BASE_ADDRESS = "https://www.lgcstandards.com/GB/en/";
                break;
        }
        User.configureUsersPerEnv(env);
    }

    @BeforeSuite
    @Parameters({"env", "browserName", "browserMode"})
    public void beforeSuite(String env, String browserName, String browserMode) {
        suiteStartTime = System.nanoTime();

        extent = new ExtentReports();
        extent = ExtentManager.createInstance("test_results.html");
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("test_results.html");
        extent.attachReporter(htmlReporter);
        parentTest = new ThreadLocal<>();
        childTest = new ThreadLocal<>();

        currentEnv = env;
        currentBrowser = browserName;
        currentBrowserMode = browserMode;
        launchSeleniumGridLocally(browserMode);
    }

    @BeforeClass
    @Parameters({"env", "browserMode", "browserName"})
    public synchronized void beforeClass(String env, String browserMode, String browserName) {
        if (browserMode.equals("grid")) {
            currentThread().setName(getClass().getName());
            log.info("Thread: ({}) is running!", currentThread().getName());
        }

        ExtentTest parent = extent.createTest(getClass().getName());
        parentTest.set(parent);
    }

    @BeforeMethod
    public synchronized void beforeMethod(Method method) {
        ExtentTest child = parentTest.get().createNode(method.getName());
        childTest.set(child);
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result, Method method) throws IOException {
        String methodName = method.getName();
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenShotOnFailure(methodName);
            if (methodName.contains("JIRA-PROJECT-CODE") || result.getThrowable().toString().contains("Assertion")) {
                childTest.get().fail(MarkupHelper.createLabel(result.getName(), ExtentColor.RED)).fail(result.getThrowable()).addScreenCaptureFromPath(getScreenshotPath());
                log.error("*** GENUINE-BUG...: " + methodName);
            } else {
                childTest.get().warning(MarkupHelper.createLabel(result.getName(), ExtentColor.BLUE)).warning(result.getThrowable()).addScreenCaptureFromPath(getScreenshotPath());
                log.error("*** SELENIUM-FAILURE...: " + methodName);
            }
            log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else if (result.getStatus() == ITestResult.SKIP) {
            childTest.get().skip(result.getThrowable());
            log.info("*** TEST-SKIPPED....: " + methodName);
            result.getMethod().getDescription();
            log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            childTest.get().pass(MarkupHelper.createLabel(result.getName(), ExtentColor.GREEN));
            log.info("*** TEST-PASSED.....: " + methodName);
            log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
        extent.flush();
    }

    @AfterClass(alwaysRun = true)
    public synchronized void tearDown() {
        log.info("TEAR DOWN EXECUTED FROM TB");
        closeBrowserIfItExists(getDriver());
    }

    @AfterSuite
    @Parameters({"browserName", "browserMode"})
    public void killSeleniumGridHubIfItExists(String browserName, String browserMode, ITestContext iTestContext) throws Exception {
        String suiteName = iTestContext.getCurrentXmlTest().getSuite().getName();
        log.info("Suite name is..: " + suiteName);
        closeSeleniumGridIfItHasAlreadyStarted(browserName, browserMode);
        suiteExecutionTime = System.nanoTime() - suiteStartTime;
        convertExecutionTimeFromNanoTimeToMinAndSec(suiteExecutionTime, suiteName);

        totalSuiteTestsRun = extent.getStats().getChildCount();
        totalSuiteTestsPassed = extent.getStats().getChildCountPass();
        totalSuiteTestsFailed = extent.getStats().getChildCountFail();
        totalSuiteTestsSkipped = extent.getStats().getChildCountSkip();
        totalSuiteTestsWarning = extent.getStats().getChildCountWarning();
    }

    private void launchSeleniumGridLocally(String browserMode) {
        if (browserMode.equals("grid")) {
            SeleniumGridConfiguration seleniumGridConfiguration = new SeleniumGridConfiguration();
            seleniumGridConfiguration.hubConfig();
            seleniumGridConfiguration.nodeConfig();
        }
    }

    private void takeScreenShotOnFailure(String testName) {
        try {
            File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            log.info("testName inside screenshot is..: " + testName);
            FileUtils.copyFile(scrFile, new File("reporting/screenshots/" + testName + "_" + currentEnv + ".png"), true);
            setScreenshotPath("reporting/screenshots/" + testName + "_" + currentEnv + ".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeBrowserIfItExists(WebDriver driver) {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }

    private void closeSeleniumGridIfItHasAlreadyStarted(String browserName, String browserMode) throws Exception {
        if (browserName.equals("grid")) {
            BrowserSetup browserSetup = new BrowserSetup();
            WebDriver driver = browserSetup.browserInit(browserName, browserMode);

            HashMap<String, Object> browserComponentsMap = returnBrowserComponents(driver);
            BrowserComponents browserComponents = new BrowserComponents(browserComponentsMap);
            browserComponents.navigation().gotoPage("http://localhost:4444/lifecycle-manager/LifecycleServlet?action=shutdown", browserMode);
            driver.close();
            log.info("Successfully closed SeleniumGrid");
        }
    }

    private HashMap<String, Object> returnBrowserComponents(WebDriver driver) {
        HashMap<String, Object> browserComponents = new HashMap<>();
        IBrowserElements browserElements = new BrowserElements(driver);
        IBrowserNavigation IBrowserNavigation = new BrowserNavigation(driver);
        browserComponents.put("elements", browserElements);
        browserComponents.put("navigation", IBrowserNavigation);
        return browserComponents;
    }

    private synchronized void convertExecutionTimeFromNanoTimeToMinAndSec(long subjectMeasuredExecutionNanoTime, String subjectName) {
        int timeExecutionTotalInSeconds = (int) TimeUnit.SECONDS.convert(subjectMeasuredExecutionNanoTime, TimeUnit.NANOSECONDS);

        if (timeExecutionTotalInSeconds <= 100) {
            log.info("The {} took {} seconds to complete", subjectName, timeExecutionTotalInSeconds);
        } else {
            log.info("The {} took {} minutes and {} seconds to complete", subjectName, timeExecutionTotalInSeconds / 60, timeExecutionTotalInSeconds % 60);
        }
    }

    private HashMap<String, Object> returnCoreComponents(List<String> componentsNames, BrowserComponents browserComponents) {
        HashMap<String, Object> coreComponents = new HashMap<>();
        for (String name : componentsNames) {
            switch (name) {
                case "login":
                    LoginComponent loginComponent = new LoginComponent(browserComponents);
                    coreComponents.put("login", loginComponent);
                    break;
                case "pageHeader":
                    PageHeaderComponent pageHeaderComponent = new PageHeaderComponent(browserComponents);
                    coreComponents.put("pageHeader", pageHeaderComponent);
                    break;
                case "orderRequest":
                    OrderRequestComponent orderRequestComponent = new OrderRequestComponent(browserComponents);
                    coreComponents.put("orderRequest", orderRequestComponent);
                    break;
                case "excelHelper":
                    ExcelHelper excelHelper = new ExcelHelper(browserComponents);
                    coreComponents.put("excelHelper", excelHelper);
                    break;
            }
        }
        return coreComponents;
    }

    protected  HashMap<String, Object> returnInitComponentsMap (String env, String browserName, String browserMode, List<String> componentsNames) throws Exception {
        WebDriver driver = spinABrowser(browserName, browserMode);
        HashMap<String, Object> browserComponentMap = returnBrowserComponents(driver);
        BrowserComponents browserComponents = new BrowserComponents(browserComponentMap);
        HashMap<String, Object> initComponents = returnCoreComponents(componentsNames, browserComponents);
        setAllEnvironmentalVariables(env);
        return initComponents;
    }

    protected void assertEquals(Object expectedValue, Object actualValue, String errorMessage) throws AssertionError {
        try {
            Assert.assertEquals(expectedValue, actualValue);
        } catch (AssertionError e) {
            log.error(errorMessage);
            throw new AssertionError(errorMessage, e);
        }
    }

    protected void assertBoolean(boolean expectedValue, boolean actualValue, String errorMessage) throws AssertionError {
        if (expectedValue) {
            try {
                Assert.assertTrue(actualValue);
            } catch (java.lang.AssertionError e) {
                log.error(errorMessage);
                throw new AssertionError(errorMessage, e);
            }
        } else {
            try {
                Assert.assertFalse(actualValue);
            } catch (java.lang.AssertionError e) {
                log.error(errorMessage);
                throw new AssertionError(errorMessage, e);
            }
        }
    }

    private WebDriver spinABrowser(String browserName, String browserMode) throws Exception {
        BrowserSetup browserSetup = new BrowserSetup();
        setDriver(browserSetup.browserInit(browserName, browserMode));
        return getDriver();
    }
}

