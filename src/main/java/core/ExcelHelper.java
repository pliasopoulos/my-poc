package core;

import browser.BrowserComponents;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;

public class ExcelHelper {

    private static final By ADD_A_COMPONENT_PLUS_BUTTON = By.xpath("//div[@class='el-upload el-upload--text outline']");
    public static final String CUSTOM_ORGANIC_ORDER = "customOrganicOrder";
    public static final String CUSTOM_ORGANIC_ORDER_PATH = "src/main/resources/Orders/Requests/CustomQuoteUpload_15_CAS_Only.xls";
    private final BrowserComponents browserComponents;

    public ExcelHelper(BrowserComponents browserComponents) {
        this.browserComponents = browserComponents;
    }

    public void uploadExcelFile(String typeOfTestFile) throws AWTException, InterruptedException {
        browserComponents.elements().waitAndClickElement(ADD_A_COMPONENT_PLUS_BUTTON);
        StringSelection s = new StringSelection(getAbsolutePath(typeOfTestFile));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, null);
        Robot robot = new Robot();
        robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
        robot.keyPress(java.awt.event.KeyEvent.VK_V);
        Thread.sleep(1000);
        robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
    }

    private String getAbsolutePath(String typeOfFile) {
        File file = null;
        switch (typeOfFile) {
            case "customOrganicOrder":
                file = new File(CUSTOM_ORGANIC_ORDER_PATH);
                break;
        }
        assert file != null;
        return file.getAbsolutePath();
    }
}
