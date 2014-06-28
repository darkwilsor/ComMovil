package wil.mirk.compmovil.parandroid.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Timer;
import java.util.TimerTask;


public class inicializando_timer extends Activity

    {
        Spinner _tiempoHastaAlarma;

        Button setear_alarma;
        String _nroreceptor;
        String _cuerpoMensaje;
        String _user;
        String _password;
        String _receptor;
        String _dondeEstaLaFoto;
        Boolean _sms;
        Boolean _mail;
        Integer _tiempo;




        @Override

        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_timer);

            _nroreceptor = "01164209786";
            _user = "";
            _password = "";
            _receptor = "darkwilsor@gmail.com";


            //obtengo las cosas del intent anterior

            Bundle newAlert = getIntent().getExtras();

            _sms = newAlert.getBoolean("_sms");
            _mail = newAlert.getBoolean("_mail");
            _cuerpoMensaje = newAlert.getString("_cuerpoMensaje");
            _dondeEstaLaFoto = newAlert.getString("_dondeEstaLaFoto");


            // obtenemos las cosas que estan en el xml.
            setear_alarma = (Button) findViewById(R.id.boton_timer);


            _tiempoHastaAlarma = (Spinner) findViewById(R.id.elegirTiempo);




            Integer[] tiempos = new Integer[]{1, 30, 60, 90, 120};

            ArrayAdapter<Integer> _adaptador = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, tiempos);

            _tiempoHastaAlarma.setAdapter(_adaptador);


            setear_alarma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    _tiempo = (Integer) _tiempoHastaAlarma.getSelectedItem();

                    _tiempo *= 60 * 1000;

                    hacerSonarAlarmaEn();

                }
            });

        }

            protected void enviarSMS() {
                Log.i("Envio SMS", "");

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

            String to = _receptor;
            String subject = "alarma!";
            String message = _cuerpoMensaje ;
            String attachement = _dondeEstaLaFoto;


            Mail mail = new Mail(_user, _password);

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


            if (attachement != null) {
                mail.addAttachment(attachement);
            }

            mail.send();

        }

        private class ejecutarAlarma extends AsyncTask {

            @Override
            protected Object doInBackground(Object[] objects) {



                    if (_mail) {
                        try {
                            enviarMail();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (_sms){
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





    }






