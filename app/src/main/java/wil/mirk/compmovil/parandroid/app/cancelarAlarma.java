package wil.mirk.compmovil.parandroid.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;





public class cancelarAlarma extends ActionBarActivity {

    EditText _passwordAlarma;
    Button _botonCancelar;
    String _passwordUsuario;
    String _password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_alarma);

        _passwordAlarma = (EditText) findViewById(R.id.passAlarma);
        _botonCancelar = (Button) findViewById(R.id.botonCancelar);

        _password = "password";

        _botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _passwordUsuario = _passwordAlarma.getText().toString();



                _password = ((globalApp)getApplication()).getPasswordGlobal().toString();


                if (_passwordUsuario.equals(_password)){

                    ((globalApp)getApplication()).setCancelado(true);

                    finish();

                }
            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cancelar_alarma, menu);
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
