package wil.mirk.compmovil.parandroid.app;

/**
 * Created by Yves on 27/06/2014.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;


public class timer_predefinido extends Activity {

    private String[] os = { "10","15","20","25", "30","35","40","45","50","55","60","65","70","75","80","85","90","95","100","115","120" ,"160" };

    Button definir_tiempo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_predefinido);

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.tiempo_definido);

        definir_tiempo=(Button)findViewById(R.id.button_timer);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, os);

        textView.setThreshold(1);

        textView.setAdapter(adapter);


        // creo que aca logro pasar a long el string pasado por el usuario

        final Long tiempo = new Long(Long.parseLong(textView.toString()));

        definir_tiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_pasar_tiempo = new Intent(timer_predefinido.this,newAlert.class);

                Bundle _bundle = new Bundle();

                _bundle.putLong("tiempo",tiempo);

                startActivity(intent_pasar_tiempo);

            }

        });
    }


}

