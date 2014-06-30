package wil.mirk.compmovil.parandroid.app;

import android.app.Activity;
import android.content.Intent;
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

            /*========================VALORES HARCODEADOS ALPHA========================================*/

            _nroreceptor = "";      //numero de telefono de
            _user = "";             //usuario de cuenta de mail
            _password = "";         //password de usuario de mail
            _receptor = "";         //mail de quien va a recivir la alerta


            //obtengo las cosas del intent anterior

            Bundle newAlert = getIntent().getExtras();

            _sms = newAlert.getBoolean("_sms");
            _mail = newAlert.getBoolean("_mail");
            _cuerpoMensaje = newAlert.getString("_cuerpoMensaje");
            _dondeEstaLaFoto = newAlert.getString("_dondeEstaLaFoto");


            // obtenemos las cosas que estan en el xml.
            setear_alarma = (Button) findViewById(R.id.boton_timer);

            _tiempoHastaAlarma = (Spinner) findViewById(R.id.elegirTiempo);




            Integer[] tiempos = new Integer[]{4, 30, 60, 90, 120}; // los int son minutos de alarmas posibles 4 es para tests

            ArrayAdapter<Integer> _adaptador = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, tiempos);

            _tiempoHastaAlarma.setAdapter(_adaptador);


            setear_alarma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    _tiempo = (Integer) _tiempoHastaAlarma.getSelectedItem();

                    _tiempo *= 60 * 1000; //tiempo em ms

                    hacerSonarAlarmaEn();


                    //boton de home para minimizar la app
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);

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
            }

            if (message != null && message.length() > 0) {
                mail.setBody(message);
            } else {
                mail.setBody("Alarma generada automaticamente");
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

                if (_sms){
                    enviarSMS();
                }

                if (_mail) {
                    try {
                        enviarMail();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }


        final Timer _timer = new Timer();

        public void hacerSonarAlarmaEn() {
            final Handler _handler = new Handler();
            Integer _3min = 1000*60*3;


            TimerTask _ejecutarAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    _handler.post(new Runnable() {
                        public void run() {

                            boolean _cancelado = false;

                            _cancelado = ((globalApp)getApplication()).getCancelado();



                            if (_cancelado){

                                _timer.purge();

                            } else {

                                try {
                                    ejecutarAlarma alarmaAEjecutar = new ejecutarAlarma();
                                    // ejecuta la asyntask creada

                                    alarmaAEjecutar.execute();

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                }
                            }
                        }
                    });
                }
            };

            TimerTask _cancelarAlarma =new TimerTask() {
                @Override
                public void run() {

                    Intent _cancelarAlarmaInt = new Intent(inicializando_timer.this, wil.mirk.compmovil.parandroid.app.cancelarAlarma.class);
                    _cancelarAlarmaInt.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(_cancelarAlarmaInt);

                }
            };


            _timer.schedule(_cancelarAlarma, _tiempo - _3min); //doy la chanse de cancelar la alarma
            _timer.schedule(_ejecutarAsynchronousTask, _tiempo); //tiempo en ms
        }



    }






