package common.page.header;

public enum PageHeaderDto {
    LANDING("LGC Standards: Reference Materials, Standards & Testing"),
    LOGIN("Login or Create an Account"),
    PHARMACEUTICAL("Pharmaceutical"),
    CUSTOM_SOLUTION("Custom solution quote request"),
    DISCOVER_PRODUCTS("Endosomal pH regulators");


    private String pageTitle;
    PageHeaderDto(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    public String getPageTitle() {
        return pageTitle;
    }
}