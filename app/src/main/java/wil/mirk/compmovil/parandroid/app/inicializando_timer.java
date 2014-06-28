package wil.mirk.compmovil.parandroid.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Timer;


public class inicializando_timer extends Activity

    {
        TimePicker tiempoDeAlarma;

        Button setear_alarma;

        @Override

        protected void onCreate(Bundle savedInstanceState)
            {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_timer);

                // obtenemos las cosas que estan en el xml.
                setear_alarma = (Button)findViewById(R.id.button_timer);
                tiempoDeAlarma = (TimePicker)findViewById(R.id.timePicker);

                                                // no me deja pone OnClickListener()
                setear_alarma.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                        public void onClick(View v)
                            {
                                Intent intent_enviando_tiempo = new Intent(inicializando_timer.this,newAlert.class);

                                Bundle hora_envio = new Bundle();

                                hora_envio.put;
                            }
                });

            }


    }
