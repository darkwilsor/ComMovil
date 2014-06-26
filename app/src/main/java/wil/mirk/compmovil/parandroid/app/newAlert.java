package wil.mirk.compmovil.parandroid.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;


public class newAlert extends ActionBarActivity {

    String _receptor;
    String _nroreceptor;
    String _cuerpoMensaje;
    EditText _cuerpo;
    Button _botonSiguiente;
    CheckBox _smsCheck, _mailCheck, _gpsCheck;
    Long _tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alert);


        _receptor = "darkwilsor@gmail.com";
        _nroreceptor = "01164209786";

        _mailCheck = (CheckBox) findViewById(R.id.mail_check);
        _smsCheck = (CheckBox) findViewById(R.id.sms_check);
        _gpsCheck = (CheckBox) findViewById(R.id.ubicacion);

        _cuerpo = (EditText) findViewById(R.id.cuerpo);
        _botonSiguiente = (Button) findViewById(R.id.boton_siguiente);

        _tiempo = (long) 6000;


        _cuerpoMensaje = _cuerpo.getText().toString();

        if(_gpsCheck.isChecked()){
            agregarUbicacion();
        }


        _botonSiguiente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                hacerSonarAlarmaEn();

            }
        });










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
           /* String[] attachements = null;*/


            Mail mail = new Mail();

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

/*            if (attachements != null) {
                for (String attachement : attachements) {
                    mail.addAttachment(attachement);
                }
            }*/
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
