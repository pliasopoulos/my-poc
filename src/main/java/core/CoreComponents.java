package core;

import common.cart.CartComponent;
import common.notifications.NotificationsComponent;
import common.page.header.PageHeaderComponent;
import login.LoginComponent;
import standards.custom.OrderRequestComponent;
import standards.pharmaceutical.PharmaceuticalComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CoreComponents {

    public static final List<String> LOGIN = Arrays.asList(
            "login", "pageHeader"
    );
    public static final List<String> PHARMA = Arrays.asList(
            "login", "pharmaceutical", "cart", "productSearch", "notifications"
    );
    public static final List<String> COMMON = Arrays.asList(
            "login", "cart", "notifications", "pageHeader"
    );
    public static final List<String> ORDERS = Arrays.asList(
            "login", "notifications", "pageHeader", "orderRequest", "excelHelper"
    );
    public static final List<String> ALL = Arrays.asList(
            "login", "pharmaceutical", "cart", "productSearch", "notifications", "pageHeader"
    );
    private final HashMap<String, Object> coreComponentsMap;

    public CoreComponents(HashMap<String, Object> coreComponentsMap) {
        this.coreComponentsMap = coreComponentsMap;
    }

    public LoginComponent login() {
        return (LoginComponent) coreComponentsMap.get("login");
    }

    public PharmaceuticalComponent pharmaceuticalComponent() {
        return (PharmaceuticalComponent) coreComponentsMap.get("pharmaceutical");
    }

    public CartComponent cartComponent() {
        return (CartComponent) coreComponentsMap.get("cart");
    }

    public NotificationsComponent notificationsComponent() {
        return (NotificationsComponent) coreComponentsMap.get("notifications");
    }

    public PageHeaderComponent pageHeaderComponent() {
        return (PageHeaderComponent) coreComponentsMap.get("pageHeader");
    }

    public OrderRequestComponent orderRequestComponent(){
        return (OrderRequestComponent) coreComponentsMap.get("orderRequest");
    }

    public ExcelHelper excelHelper(){
        return (ExcelHelper) coreComponentsMap.get("excelHelper");
    }
}