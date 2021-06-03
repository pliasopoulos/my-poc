package browser;

import browser.elements.BrowserElements;
import browser.navigation.BrowserNavigation;
import browser.setup.BrowserSetup;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class BrowserComponents {

    private final BrowserElements browserElements;
    private final BrowserNavigation browserNavigation;
    private final BrowserSetup browserSetup;

    public BrowserComponents(HashMap<String, Object> browserComponents){
        this.browserElements = (BrowserElements) browserComponents.get("elements");
        this.browserNavigation = (BrowserNavigation) browserComponents.get("navigation");
        this.browserSetup = (BrowserSetup) browserComponents.get("setup");
    }

    public BrowserElements elements(){
        return getBrowserElements();
    }

    public BrowserNavigation navigation(){
        return getBrowserNavigation();
    }

    public BrowserSetup browserSetup(){
        return getBrowserSetup();
    }
}
