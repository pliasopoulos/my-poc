package core.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private static final String STANDARD_USER_FIRST_NAME = "Joe";
    private static final String STANDARD_USER_LAST_NAME = "Bloggs";
    private static final String STANDARD_USER_ORGANISATION = "Acme INC";
    private static final String STANDARD_USER_EMAIL = "joe.bloggs@acme.com";
    public static String LOGIN_FULL_NAME_TWO;
    public static String LOGIN_USERNAME_TWO;
    public static String LOGIN_PASSWORD_TWO;
    public static String LOGIN_FULL_NAME;
    public static String LOGIN_USERNAME;
    public static String LOGIN_PASSWORD;
    public static String ADMIN_USERNAME;
    public static String ADMIN_PASSWORD;
    public static String ADMIN_FULL_NAME;
    private String email;
    private String organisation;
    private String firstName;
    private String lastName;
    private FullAddress fullAddress;

    public User(String firstName, String lastName, String organisation, String email, FullAddress fullAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.fullAddress = fullAddress;
        this.email = email;
    }

    public static void configureUsersPerEnv(String env) {
        switch (env) {
            case "dev":
                ADMIN_USERNAME = "testUser1";
                ADMIN_PASSWORD = "cWF0ZXN0ZXIxMjMh";
                ADMIN_FULL_NAME = "Qa1 Test1";
                break;
            case "qa":
                ADMIN_USERNAME = "testUser2";
                ADMIN_PASSWORD = "cWF0ZXN0ZXIxMjMh";
                ADMIN_FULL_NAME = "Qa2 Test2";
                break;
            case "prod":
                LOGIN_USERNAME = "leleliasos@hotmail.com";
                LOGIN_PASSWORD = "VGVzdEFjYzEyMw==";
                LOGIN_FULL_NAME = "Qa Test";
                LOGIN_USERNAME_TWO = "pliasopoulos@gmail.com";
                LOGIN_PASSWORD_TWO = "VGVzdEFjYzEyMw==";
                LOGIN_FULL_NAME_TWO = "Pantelis Lias";
                break;
        }
    }

    public static User createStandardUser() {
        FullAddress standardAddress = new FullAddress(FullAddress.STANDARD_USER_ADDRESS_NUMBER, FullAddress.STANDARD_USER_STREET, FullAddress.STANDARD_USER_TOWN, FullAddress.STANDARD_USER_COUNTRY, FullAddress.STANDARD_USER_POSTCODE);
        return new User(STANDARD_USER_FIRST_NAME, STANDARD_USER_LAST_NAME, STANDARD_USER_ORGANISATION, STANDARD_USER_EMAIL, standardAddress);
    }
}
