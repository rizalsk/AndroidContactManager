package com.example.rizmas.cobasatu;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT = 0, DELETE = 1;

    EditText nameTxt, phoneTxt, mailTxt, addressTxt;
    List<Contact> contacts = new ArrayList<Contact>();
    ListView contactListView;
    ImageView contactImageView;
    Uri ImageUri= Uri.parse("android.resource://com.example.rizmas.cobasatu/drawable/male.png");
    DatabaseHandler dbHandler;
    int longClickedItemIndex;
    ArrayAdapter<Contact> contactAdapter;
    int idcontact;
    String nama, tlp, eml, alm;
    Uri gambar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        mailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        contactListView = (ListView) findViewById(R.id.listV);
        contactImageView = (ImageView) findViewById(R.id.imageContactview) ;
        dbHandler = new DatabaseHandler(getApplicationContext());
        TabHost tbHost = (TabHost) findViewById(R.id.tabHost);
        TabHost.TabSpec tabspec;

        registerForContextMenu(contactListView);
        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });

        tbHost.setup();

        tabspec= tbHost.newTabSpec("creator");
        tabspec.setContent(R.id.tabCreator);
        tabspec.setIndicator("creator");
        tbHost.addTab(tabspec);


        tabspec = tbHost.newTabSpec("list");
        tabspec.setContent(R.id.tabList);
        tabspec.setIndicator("list");
        tbHost.addTab(tabspec);

        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Your Toast has been created", Toast.LENGTH_SHORT).show();
                Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(mailTxt.getText()), String.valueOf(addressTxt.getText()), ImageUri);

                if (!contactExists(contact)){
                    dbHandler.createContact(contact);
                    contacts.add(contact);
                    contactAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), nameTxt.getText().toString()+ " hass been added to your List", Toast.LENGTH_SHORT).show();
                    //nameTxt.setText("");phoneTxt.setText("");mailTxt.setText("");addressTxt.setText("");
                    //contactImageView.setImageURI(Uri.parse("android.resource://com.example.rizmas.cobasatu/drawable/male.png"));
                    //tbHost.setCurrentTab(2);
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " already exists, please use different name", Toast.LENGTH_SHORT).show();

                //contacts.add(new Contact( 0, nameTxt.getText().toString(), phoneTxt.getText().toString(), mailTxt.getText().toString(), addressTxt.getText().toString(), ImageUri));
                //populateListContact();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnAdd.setEnabled(String.valueOf(nameTxt.getText()).trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contactImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnt = new Intent();
                intnt.setType("image/*");
                intnt.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intnt, "Select contact image"), 1);
            }
        });

        /*List<Contact> addableContact = dbHandler.getAllContacts();
        int contactCount = dbHandler.getContactsCount();
        for (int i=0; i<contactCount; i++){
            contacts.add(addableContact.get(i));
        }
        */
        if (dbHandler.getContactsCount() != 0)
            contacts.addAll(dbHandler.getAllContacts());

//      if (!addableContact.isEmpty())
        populateListContact();
    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if (resCode == RESULT_OK){
            if (reqCode == 1)
                ImageUri = (Uri) data.getData();
                contactImageView.setImageURI(data.getData());
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.big_pencil);
        menu.setHeaderTitle("Contac options");
        menu.add(Menu.NONE, EDIT, menu.NONE, "Edit Contact");
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Contact");
    }

    public boolean onContextItemSelected(MenuItem item){

        switch(item.getItemId()){
            case EDIT:
                //TODO : Implement a editing contact
                final Contact data = dbHandler.getContact(longClickedItemIndex+1);
                idcontact = (Integer) data.get_id();
                nama = data.get_name();
                Toast.makeText(getApplicationContext(), String.valueOf(idcontact+" Nama :" +nama), Toast.LENGTH_SHORT).show();
                nameTxt.setText(data.get_name());
                phoneTxt.setText(data.get_phone());
                mailTxt.setText(data.get_email());
                addressTxt.setText(data.get_address());

                if (!data.get_imgUri().toString().equals(""))
                    contactImageView.setImageURI(data.get_imgUri());
                Button btnAdd = (Button) findViewById(R.id.btnAdd);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean a = true;
                        //Toast.makeText(getApplicationContext(), "Your Toast has been created", Toast.LENGTH_SHORT).show();
                        Contact contact = new Contact(data.get_id(), String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(mailTxt.getText()), String.valueOf(addressTxt.getText()), ImageUri);
                        if (a){
                            dbHandler.updateContact(contact);
                            //contacts.set(longClickedItemIndex, contact);
                            Toast.makeText(getApplicationContext(), nameTxt.getText().toString()+ " hass been update", Toast.LENGTH_SHORT).show();
                            //nameTxt.setText("");phoneTxt.setText("");mailTxt.setText("");addressTxt.setText("");
                            //contactImageView.setImageURI(Uri.parse("android.resource://com.example.rizmas.cobasatu/drawable/male.png"));
                            //tbHost.setCurrentTab(2);
                            contacts.set(longClickedItemIndex, contact);
                            contactAdapter.notifyDataSetChanged();
                            a = false;
                            //populateListContact();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " failed to update", Toast.LENGTH_SHORT).show();
                        //contacts.add(new Contact( 0, nameTxt.getText().toString(), phoneTxt.getText().toString(), mailTxt.getText().toString(), addressTxt.getText().toString(), ImageUri));

                    }
                });
                /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                contactAdapter.notifyDataSetChanged();*/
                break;
            case DELETE:
                Contact c = contacts.get(longClickedItemIndex);
                //Toast.makeText(getApplicationContext(), String.valueOf(" Nama :" +c.get_name()+". ID:"+String.valueOf(c.get_id())), Toast.LENGTH_SHORT).show();
                //dbHandler.deleteContact(contacts.get(longClickedItemIndex));
                dbHandler.deleteContact(c);
                contacts.remove(longClickedItemIndex);
                contactAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public boolean contactExists(Contact contact){
        String name = contact.get_name();
        int contactCount = contacts.size();
        for(int i=0; i<contactCount;i++){
            if (name.compareToIgnoreCase(contacts.get(i).get_name()) == 0)
                return true;
        }
        return false;
    }


    private void populateListContact(){
        contactAdapter = new contactListAdapter();
        contactListView.setAdapter(contactAdapter);
    }

   /* private void addContact(String name, String phone, String email, String address, Uri ImageUri){
        contacts.add(new Contact(name, phone, email, address, ImageUri));
    }*/
    private class contactListAdapter extends ArrayAdapter<Contact> {
        public contactListAdapter (){
            super(MainActivity.this, R.layout.listview_item, contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Contact currentContact = contacts.get(position);
            TextView name = (TextView) view.findViewById(R.id.contactName);
            name.setText(currentContact.get_name());
            TextView phone = (TextView) view.findViewById(R.id.contactPhone);
            phone.setText(currentContact.get_phone());
            TextView email = (TextView) view.findViewById(R.id.contactEmail);
            email.setText(currentContact.get_email());
            TextView address = (TextView) view.findViewById(R.id.contactAddress);
            address.setText(currentContact.get_address());
            ImageView ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
            ivContactImage.setImageURI(currentContact.get_imgUri());
            return view;
        }

    }

}

