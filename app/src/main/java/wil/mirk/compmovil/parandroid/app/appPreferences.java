package wil.mirk.compmovil.parandroid.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.ContactsContract;
import android.widget.Toast;

public class appPreferences extends PreferenceActivity {
    Preference _agregarContactos;
    Preference _agregarMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

         _agregarContactos = findPreference("_agregarContactos");
         _agregarMail = findPreference("_agregarMails");


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
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Toast.makeText(getBaseContext(), name, Toast.LENGTH_LONG).show();
                // TODO Whatever you want to do with the selected contact name.
            }
        }
    }
}