package com.cm0573.contactlistw15016306;

/**
 * Created by Joshua Higgins w15016306 on 02/03/2017.
 *
 *  Used to view, add and edit contact details
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetails extends AppCompatActivity {

    EditText editTextFirstName, editTextFamilyName, editTextHouseNumber, editTextStreet, editTextTown, editTextCounty, editTextPostcode, editTextTelephoneNumber;
    TextView newContactMes;
    private int _contact_Id=0;
    private boolean edit = false;
    private static final String TAG = "";


    @Override
    //Method adds the menu items to the tool bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_details_menu, menu);
        MenuItem delete = menu.findItem(R.id.delete);

        //Used to check if the contact details are editable
        if(edit)
        {
            delete.setVisible(true);
        }
        else
        {
            delete.setVisible(false);
        }

        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        //sets edit texts
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextFamilyName = (EditText) findViewById(R.id.editTextFamilyName);
        editTextHouseNumber = (EditText) findViewById(R.id.editTextHouseNumber);
        editTextStreet = (EditText) findViewById(R.id.editTextStreet);
        editTextTown = (EditText) findViewById(R.id.editTextTown);
        editTextCounty = (EditText) findViewById(R.id.editTextCounty);
        editTextPostcode = (EditText) findViewById(R.id.editTextPostcode);
        editTextTelephoneNumber = (EditText) findViewById(R.id.editTextTelephoneNumber);

        newContactMes = (TextView) findViewById(R.id.newContactMes);

        //enables toolbar
        Toolbar detailsToolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(detailsToolbar);
        getSupportActionBar().setTitle("New Contact");

        _contact_Id =0;
        Intent intent = getIntent();
        _contact_Id  =intent.getIntExtra("contact_Id", 0);
        ContactDatabase dbAccess = new ContactDatabase(this);
        Contact contact_infor;
        contact_infor = dbAccess.getContactById(_contact_Id );

        //checks if a contact is loaded

        newContactMes.setVisibility(View.VISIBLE);

        if (_contact_Id != 0) {
            newContactMes.setVisibility(View.GONE);
            //enables delete button on toolbar
            edit = true;

            //prevents editTextFirstName loading null
            editTextFirstName.setText(String.valueOf(contact_infor.firstName));
            editTextHouseNumber.setText(String.valueOf(contact_infor.houseNumber));
            // Sets toolbar title to include contact name
            getSupportActionBar().setTitle(contact_infor.firstName + " " + contact_infor.familyName);
        }
        else
        {
            //prevents house number being set to 0
            editTextHouseNumber.setText(null);
        }
        editTextFamilyName.setText(contact_infor.familyName);
        editTextStreet.setText(contact_infor.street);
        editTextTown.setText(contact_infor.town);
        editTextCounty.setText(contact_infor.county);
        editTextPostcode.setText(contact_infor.postcode);
        editTextTelephoneNumber.setText(contact_infor.telephoneNumber);
    }


    @Override
    //Method controls action of menu item
    public boolean onOptionsItemSelected(MenuItem item) {
        ContactDatabase dbAccess = new ContactDatabase(this);
        boolean success = true;
        switch (item.getItemId()) {
            case R.id.save:

                //data validation, creates error messages within edit text
                //I have interpreted each field must be non-empty.

                if(TextUtils.isEmpty(editTextFirstName.getText().toString().trim()))
                {
                    editTextFirstName.setError("Please Enter First Name");
                    success = false;
                }
                if(TextUtils.isEmpty(editTextFamilyName.getText().toString().trim()))
                {
                    editTextFamilyName.setError("Please Enter Family Name");
                    success = false;
                }

                if(TextUtils.isEmpty(editTextHouseNumber.getText()))
                {
                    editTextHouseNumber.setError("Please Enter House Number");
                    success = false;
                }

                try //try required for when set to null
                {
                    if (Integer.parseInt(editTextHouseNumber.getText().toString()) < 1) {
                        editTextHouseNumber.setError("House Number must be greater than 0");
                        success = false;
                    }
                }
                catch(NumberFormatException e)
                {
                    Log.i(TAG, "NumberFormatException Occurred");
                    editTextHouseNumber.setError("Please Enter House Number");
                }

                if(TextUtils.isEmpty(editTextStreet.getText().toString().trim()))
                {
                    editTextStreet.setError("Please Enter Street");
                    success = false;
                }
                if(TextUtils.isEmpty(editTextTown.getText().toString().trim()))
                {
                    editTextTown.setError("Please Enter Town");
                    success = false;
                }
                if(TextUtils.isEmpty(editTextCounty.getText().toString().trim()))
                {
                    editTextCounty.setError("Please Enter County");
                    success = false;
                }
                if(TextUtils.isEmpty(editTextPostcode.getText()))
                {
                    editTextPostcode.setError("Please Enter Postcode");
                    success = false;
                }
                else if(!editTextPostcode.getText().toString().matches("^[a-zA-Z][a-zA-z][0-9]\\s[0-9][0-9][a-zA-z]$"))
                {
                    editTextPostcode.setError("Format Postcode as (AA1 11A)");
                    success = false;
                }
                if (TextUtils.isEmpty(editTextTelephoneNumber.getText()))
                {
                    editTextTelephoneNumber.setError("Please Enter Telephone Number");
                    success = false;
                }
                if (editTextTelephoneNumber.getText().toString().startsWith(" "))
                {
                    editTextTelephoneNumber.setError("Telephone Number must not start with space");
                    success = false;
                }

                if (success) {

                    //gets data from editText
                    Contact contact = new Contact();
                    contact.firstName = (editTextFirstName.getText().toString());
                    contact.familyName = editTextFamilyName.getText().toString();
                    contact.houseNumber = Integer.parseInt(editTextHouseNumber.getText().toString());
                    contact.street = editTextStreet.getText().toString();
                    contact.town = editTextTown.getText().toString();
                    contact.county = editTextCounty.getText().toString();
                    contact.postcode = editTextPostcode.getText().toString();
                    contact.telephoneNumber = editTextTelephoneNumber.getText().toString();
                    contact.contact_ID = _contact_Id;

                    //adds new contact if id 0
                    if (_contact_Id == 0) {
                        _contact_Id = dbAccess.contactInsert(contact);

                        Toast.makeText(this, contact.firstName + " " + contact.familyName + " Added", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Contact Added");
                        finish();
                    }

                    //updates existing user
                    else
                    {
                        dbAccess.contactUpdate(contact);
                        Toast.makeText(this, contact.firstName + " " + contact.familyName + " Updated", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Contact Updated");
                        finish();
                    }
                }

                else
                {
                    Toast.makeText(ContactDetails.this, "Please Check Fields", Toast.LENGTH_SHORT).show();
                }

                return true;

            //Calls dialog method on delete press
            case R.id.delete:
                confirmDelete();
                return true;

            case R.id.close:
                finish();
                return true;


            default:

                return super.onOptionsItemSelected(item);

        }
    }


    // method creates a confirmation dialog for contact deletion
    public void confirmDelete()
    {
        final ContactDatabase dbAccess = new ContactDatabase(this);
        new AlertDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Remove Contact?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface deleteConfirm, int option) {

                        //Deletes contact from database
                        dbAccess.contactDelete(_contact_Id);
                        Toast.makeText(ContactDetails.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Contact Deleted");
                        finish();
                    }})
                .setNegativeButton(android.R.string.cancel, null).show();
    }

}
