package core.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullAddress {
    static final int STANDARD_USER_ADDRESS_NUMBER = 7;
    static final String STANDARD_USER_STREET = "Harp Lane";
    static final String STANDARD_USER_TOWN = "London";
    static final String STANDARD_USER_COUNTRY = "United Kingdom";
    static final String STANDARD_USER_POSTCODE = "EC3R 6DP";
    private int number;
    private String street;
    private String city;
    private String country;
    private String postCode;

    public FullAddress(int number, String street, String city, String country, String postCode) {
        this.number = number;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postCode = postCode;
    }
}
