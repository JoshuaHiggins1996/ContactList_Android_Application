package com.cm0573.contactlistw15016306;

/**
 * Created by Joshua Higgins w15016306 on 02/03/2017.
 *
 * Structure of a contact
 */

public class Contact {
    // Name of the table to store contacts
    public static final String TABLE = "contact";

    //Columns Names
    public static final String KEY_ID = "id";
    public static final String KEY_familyName = "familyName";
    public static final String KEY_firstName = "firstName";
    public static final String KEY_houseNumber = "houseNumber";
    public static final String KEY_street = "street";
    public static final String KEY_town = "town";
    public static final String KEY_county = "county";
    public static final String KEY_postcode = "postcode";
    public static final String KEY_telephoneNumber = "telephoneNumber";

    //Type that data is stored as
    public long contact_ID;
    public String familyName;
    public String firstName;
    public int houseNumber;
    public String street;
    public String town;
    public String county;
    public String postcode;
    public String telephoneNumber;
}
