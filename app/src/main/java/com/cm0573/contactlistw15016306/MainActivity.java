package com.cm0573.contactlistw15016306;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Joshua Higgins w15016306 on 02/03/2017.
 *
 *  Used to view the list of contacts
 */

public class MainActivity extends AppCompatActivity{

    TextView contact_Id, noContacts;
    private static final String TAG = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        //adds toolbar to activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);
        //loads the list of contacts

        noContacts = (TextView) findViewById(R.id.noContacts);
        loadList();
    }


    @Override
    //Method adds the menu items to the tool bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    //loads contacts to the Grid view (Grid view used over list view for flexibility)
    private void loadList()
    {

        noContacts.setVisibility(View.GONE);

        try {
            ContactDatabase dbAccess = new ContactDatabase(this);

            ArrayList<HashMap<String, String>> contactList = dbAccess.getContactList();

            GridView lv = (GridView) findViewById(R.id.gridview);

            Log.i(TAG, "Contacts Loaded");

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    contact_Id = (TextView) view.findViewById(R.id.contact_ID);
                    String Id = contact_Id.getText().toString();
                    Intent contactIntent = new Intent(getApplicationContext(), ContactDetails.class);
                    contactIntent.putExtra("contact_Id", Integer.parseInt(Id));
                    startActivity(contactIntent);
                }


            });

            ListAdapter contactAdapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.view_contact_entry, new String[]{"id", "firstName", "familyName"}, new int[]{R.id.contact_ID, R.id.contact_firstName, R.id.contact_familyName});
            lv.setAdapter(contactAdapter);

            if (contactList.size() == 0)
            {
                Log.i(TAG, "No Contacts To Load");
                noContacts.setVisibility(View.VISIBLE);
            }

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    @Override
    //Method controls action of menu item
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newContact:
                Intent contactIntent = new Intent(this, ContactDetails.class);
                contactIntent.putExtra("contact_Id", 0);
                startActivity(contactIntent);
                return true;


            default:

                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    //reloads list view when user returns to activity
    protected void onResume()
    {
        Log.i(TAG, "Contacts Refreshed");
        loadList();
        super.onResume();
    }
}
