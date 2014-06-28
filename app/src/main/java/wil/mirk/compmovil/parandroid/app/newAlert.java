package wil.mirk.compmovil.parandroid.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class newAlert extends ActionBarActivity {

    String _receptor;
    String _nroreceptor;
    String _cuerpoMensaje;
    String _user;
    String _password;
    EditText _cuerpo;
    Button _botonSiguiente;
    Button _botonFoto;
    CheckBox _smsCheck, _mailCheck, _gpsCheck;
    Long _tiempo;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alert);


        _receptor = "darkwilsor@gmail.com";//quien recibe el mail
        _user = "darkwilsor@gmail.com";//quien lo manda
        _password = "" ;//ingresar password del mail
        _nroreceptor = "01164209786";

        _mailCheck = (CheckBox) findViewById(R.id.mail_check);
        _smsCheck = (CheckBox) findViewById(R.id.sms_check);
        _gpsCheck = (CheckBox) findViewById(R.id.ubicacion);

        _cuerpo = (EditText) findViewById(R.id.cuerpo);
        _botonSiguiente = (Button) findViewById(R.id.boton_siguiente);
        _botonFoto = (Button) findViewById(R.id.agregarFoto);

        _tiempo = (long) 6000;


        _cuerpoMensaje = _cuerpo.getText().toString();

        if(_gpsCheck.isChecked()){
            agregarUbicacion();
        }

        _botonFoto.setOnClickListener(new View.OnClickListener(){
             public void onClick (View view){
                lanzarIntentFoto();
             }


        });


        _botonSiguiente.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                hacerSonarAlarmaEn();

            }
        });


    }
    String _dondeEstaLaFoto;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String _nombreArchivo= "JPEG_" + timeStamp + "_";

        File _path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(
                _nombreArchivo,
                ".jpg",
                _path
        );

        // Save a file: path for use with ACTION_VIEW intents
        _dondeEstaLaFoto = imagen.getAbsolutePath();
        return imagen;
    }
    private void lanzarIntentFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {


            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected void agregarUbicacion(){

/*
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criterio = new Criteria();
        String provider = mLocationManager.getBestProvider(criterio, false);
        Location location = mLocationManager.getLastKnownLocation(provider);

        String _ubicacion = location.toString();

        _cuerpoMensaje = _cuerpoMensaje + "Mi ultima ubicacion es :" +  _ubicacion;

*/


        GPSTracker _tracker = new GPSTracker(this);
        Double location =_tracker.getLatitude();
        String _ubicacion = location.toString();
        location = _tracker.getLongitude();
        _ubicacion = _ubicacion + location.toString();



        _cuerpoMensaje = _cuerpoMensaje + "Mi ultima ubicacion es :" +  _ubicacion;



    }

    protected void enviarSMS() {
        Log.i("Send SMS", "");

        String phoneNo = _nroreceptor;
        String message = _cuerpoMensaje;

        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);

        } catch (Exception e) {


            e.printStackTrace();
        }
    }



    protected void enviarMail() throws Exception{

/*        String[] recipients = {_receptor};
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        // prompts email clients only
        email.setType("message/rfc822");

        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, "alerta");
        email.putExtra(Intent.EXTRA_TEXT, _cuerpoMensaje);



        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Seleccionar Cliente de mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(newAlert.this, "No hay cliente de mail instalado",
                    Toast.LENGTH_LONG).show();
        }*/

            String to = _receptor;
            String subject = "alarma!";
            String message = _cuerpoMensaje ;
            String attachement = _dondeEstaLaFoto;


            Mail mail = new Mail(_password, _user);

            if (subject != null && subject.length() > 0) {
                mail.setSubject(subject);
            } else {
                mail.setSubject("Subject");
            }

            if (message != null && message.length() > 0) {
                mail.setBody(message);
            } else {
                mail.setBody("Message");
            }

            mail.setTo(new String[] {to});

            mail.setFrom(_receptor);
            mail.setPassword("Tanarinototoro18");

            if (attachement != null) {
                  mail.addAttachment(attachement);
            }

           mail.send();

    }









    private class ejecutarAlarma extends AsyncTask  {

        @Override
        protected Object doInBackground(Object[] objects) {


/*                    if(_gpsCheck.isChecked()){
                        agregarUbicacion();
                    }*/

                    if (_mailCheck.isChecked()) {
                        try {
                            enviarMail();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (_smsCheck.isChecked()){
                        enviarSMS();
                    }

            return null;
        }
    }


    public void hacerSonarAlarmaEn() {
        final Handler _handler = new Handler();
        Timer _timer = new Timer();
        TimerTask _ejecutarAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                _handler.post(new Runnable() {
                    public void run() {
                        try {
                            ejecutarAlarma alarmaAEjecutar = new ejecutarAlarma();
                            // ejecuta la asyntask creada
                            alarmaAEjecutar.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        _timer.schedule(_ejecutarAsynchronousTask, _tiempo); //tiempo en ms
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
