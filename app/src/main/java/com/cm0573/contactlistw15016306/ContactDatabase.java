package com.cm0573.contactlistw15016306;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joshua Higgins w15016306 on 02/03/2017.
 *
 *  Holds the database structure and SQL used to manage contacts
 */


public class ContactDatabase extends SQLiteOpenHelper {

    //Database version
    private static final int DB_VERSION = 1;

    // Name of the database
    private static final String DB_NAME = "contacts.helper";


    public ContactDatabase(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    ////////////////////////////////////////////////////////////////////////
    //                        Database Structure                          //
    ////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(SQLiteDatabase helper) {

        //table created to hold contacts
        String CREATE_TABLE_CONTACT = "CREATE TABLE " + Contact.TABLE  + "("
                + Contact.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Contact.KEY_familyName + " TEXT, "
                + Contact.KEY_firstName + " TEXT, "
                + Contact.KEY_houseNumber + " INTEGER, "
                + Contact.KEY_street + " TEXT, "
                + Contact.KEY_town + " TEXT, "
                + Contact.KEY_county + " TEXT, "
                + Contact.KEY_postcode + " TEXT, "
                + Contact.KEY_telephoneNumber + " TEXT)";


        helper.execSQL(CREATE_TABLE_CONTACT);

    }


    @Override
    //removes old version of database if version is higher, prevents extra data being used on the device.
    public void onUpgrade(SQLiteDatabase dbase, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        dbase.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE);
        onCreate(dbase);

    }


    ////////////////////////////////////////////////////////////////////////
    //                                SQL                                 //
    ////////////////////////////////////////////////////////////////////////

    //Contact Insert
    public int contactInsert(Contact contact) {

        SQLiteDatabase dbase = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put(contact.KEY_familyName, contact.familyName);
        data.put(contact.KEY_firstName,contact.firstName);
        data.put(Contact.KEY_houseNumber, contact.houseNumber);
        data.put(Contact.KEY_street, contact.street);
        data.put(Contact.KEY_town, contact.town);
        data.put(Contact.KEY_county, contact.county);
        data.put(Contact.KEY_postcode, contact.postcode);
        data.put(Contact.KEY_telephoneNumber, contact.telephoneNumber);


        long contact_Id = dbase.insert(Contact.TABLE, null, data);
        dbase.close();
        return (int) contact_Id;
    }


    //Contact Delete
    public void contactDelete(int contact_Id) {
        //removes entry from database based in ID
        SQLiteDatabase dbase = getWritableDatabase();
        dbase.delete(Contact.TABLE, Contact.KEY_ID + "= ?", new String[] { String.valueOf(contact_Id) });
        dbase.close();
    }


    //Contact Update
    public void contactUpdate(Contact contact) {

        SQLiteDatabase dbase = getWritableDatabase();
        ContentValues data = new ContentValues();

        data.put(Contact.KEY_familyName, contact.familyName);
        data.put(Contact.KEY_firstName,contact.firstName);
        data.put(Contact.KEY_houseNumber, contact.houseNumber);
        data.put(Contact.KEY_street, contact.street);
        data.put(Contact.KEY_town, contact.town);
        data.put(Contact.KEY_county, contact.county);
        data.put(Contact.KEY_postcode, contact.postcode);
        data.put(Contact.KEY_telephoneNumber, contact.telephoneNumber);
        dbase.update(Contact.TABLE, data, Contact.KEY_ID + "= ?", new String[] {String.valueOf(contact.contact_ID) });
        dbase.close(); // Close connection to the database
    }


    public ArrayList<HashMap<String, String>> getContactList() {
        SQLiteDatabase dbase = getReadableDatabase();

        //SQL to select all contacts in alphabetical order
        String query =  "SELECT  " +
                Contact.KEY_ID + "," +
                Contact.KEY_familyName + "," +
                Contact.KEY_firstName + "," +
                Contact.KEY_houseNumber + "," +
                Contact.KEY_street + "," +
                Contact.KEY_town + "," +
                Contact.KEY_county + "," +
                Contact.KEY_postcode+ "," +
                Contact.KEY_telephoneNumber +
                " FROM " + Contact.TABLE +
                //Ordered alphabetically by first name then family name.
                " ORDER BY " + Contact.KEY_firstName +" COLLATE NOCASE, " + Contact.KEY_familyName +  " COLLATE NOCASE ASC ";


        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = dbase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> record = new HashMap<String, String>();
                //loads this information to list, not all information needs to loaded to the list view
                record.put("id", cursor.getString(cursor.getColumnIndex(Contact.KEY_ID)));
                record.put("firstName", cursor.getString(cursor.getColumnIndex(Contact.KEY_firstName)));
                record.put("familyName", cursor.getString(cursor.getColumnIndex(Contact.KEY_familyName)));
                contactList.add(record);

            } while (cursor.moveToNext());
        }

        cursor.close();
        dbase.close();
        // Close connection to the database
        return contactList;

    }


    public Contact getContactById(int Id){

        //SQL to select contact based on the ID
        SQLiteDatabase dbase = getReadableDatabase();
        String query =  "SELECT  " +
                Contact.KEY_ID + "," +
                Contact.KEY_familyName + "," +
                Contact.KEY_firstName + "," +
                Contact.KEY_houseNumber + "," +
                Contact.KEY_street + "," +
                Contact.KEY_town + "," +
                Contact.KEY_county + "," +
                Contact.KEY_postcode + "," +
                Contact.KEY_telephoneNumber +
                " FROM " + Contact.TABLE
                + " WHERE " +
                Contact.KEY_ID + "=?";

        Contact contact = new Contact();

        Cursor cursor = dbase.rawQuery(query, new String[] { String.valueOf(Id) } );

        //loads data to be used for all details
        if (cursor.moveToFirst()) {
            do {
                contact.contact_ID =cursor.getInt(cursor.getColumnIndex(Contact.KEY_ID));
                contact.familyName =cursor.getString(cursor.getColumnIndex(Contact.KEY_familyName));
                contact.firstName  =cursor.getString(cursor.getColumnIndex(Contact.KEY_firstName));
                contact.houseNumber =cursor.getInt(cursor.getColumnIndex(Contact.KEY_houseNumber));
                contact.street  =cursor.getString(cursor.getColumnIndex(Contact.KEY_street));
                contact.town  =cursor.getString(cursor.getColumnIndex(Contact.KEY_town));
                contact.county  =cursor.getString(cursor.getColumnIndex(Contact.KEY_county));
                contact.postcode  =cursor.getString(cursor.getColumnIndex(Contact.KEY_postcode));
                contact.telephoneNumber =cursor.getString(cursor.getColumnIndex(Contact.KEY_telephoneNumber));

            } while (cursor.moveToNext());
        }

        cursor.close();
        dbase.close();
        // Close connection to the database
        return contact;
    }

}