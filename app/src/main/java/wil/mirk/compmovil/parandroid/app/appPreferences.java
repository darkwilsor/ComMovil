package wil.mirk.compmovil.parandroid.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import java.util.HashSet;
import java.util.Set;

public class appPreferences extends PreferenceActivity {



    Set<String> _listSMS = new HashSet<String>();
    Set<String> _listMail = new HashSet<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference _agregarContactos = findPreference("_agregarContactos");
        Preference _agregarMail = findPreference("_agregarMails");





        _agregarContactos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);

                return false;
            }
        });

        _agregarMail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
                startActivityForResult(intent, 2);
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * requestCode : an integer code passed to the called activity set by caller activity
         * resultCode : an integer code returned from the called activity
         * data : an intent containing data set by the called activity
         */
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = getContentResolver().query(contactData, null, null, null, null);


            if (c.moveToFirst()) {
              /*  String _name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));*/
                String _number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

               _listSMS.add(_number);
            }
        }


        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = getContentResolver().query(contactData, null, null, null, null);


            if (c.moveToFirst()) {
/*                String _name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));*/
                String _mail = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

                _listMail.add(_mail);

            }
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences _contactos = PreferenceManager.getDefaultSharedPreferences(appPreferences.this);
        SharedPreferences _contacMail = PreferenceManager.getDefaultSharedPreferences(appPreferences.this);


        SharedPreferences.Editor editorSMS = _contactos.edit();
        SharedPreferences.Editor editorMail = _contacMail.edit();


        editorMail.putStringSet("SMS", _listSMS);
        editorSMS.putStringSet("MAIL", _listMail);

        editorMail.commit();
        editorSMS.commit();
    }
}