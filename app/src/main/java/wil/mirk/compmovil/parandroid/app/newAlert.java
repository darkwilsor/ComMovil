package wil.mirk.compmovil.parandroid.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
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


public class newAlert extends ActionBarActivity {


    String _cuerpoMensaje;

    EditText _cuerpo;

    Button _botonSiguiente;
    Button _botonFoto;

    CheckBox _smsCheck, _mailCheck, _gpsCheck;

    String _dondeEstaLaFoto;

    static final int REQUEST_TAKE_PHOTO = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alert);





        _mailCheck = (CheckBox) findViewById(R.id.mail_check);
        _smsCheck = (CheckBox) findViewById(R.id.sms_check);
        _gpsCheck = (CheckBox) findViewById(R.id.ubicacion);

        _cuerpo = (EditText) findViewById(R.id.cuerpo);
        _botonSiguiente = (Button) findViewById(R.id.boton_siguiente);
        _botonFoto = (Button) findViewById(R.id.agregarFoto);






        _botonFoto.setOnClickListener(new View.OnClickListener(){
             public void onClick (View view){
                lanzarIntentFoto();
             }
        });





        _botonSiguiente.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                _cuerpoMensaje = _cuerpo.getText().toString();

                if(_gpsCheck.isChecked()){
                    agregarUbicacion();
                }

                Intent _timerInt = new Intent(newAlert.this, inicializando_timer.class);
                Bundle _datosPasados = new Bundle();


                _datosPasados.putBoolean("_sms",_smsCheck.isChecked());
                _datosPasados.putBoolean("_mail",_mailCheck.isChecked());
                _datosPasados.putString ("_cuerpoMensaje", _cuerpoMensaje);
                _datosPasados.putString ("_dondeEstaLaFoto", _dondeEstaLaFoto);

                _timerInt.putExtras(_datosPasados);

                startActivity(_timerInt);

                finish();


            }
        });


    }


    private File createImageFile() throws IOException {
        // Crea un nombre unico para evitar coliciones
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String _nombreArchivo= "JPEG_" + timeStamp + "_";

        File _path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(
                _nombreArchivo,
                ".jpg",
                _path
        );


        _dondeEstaLaFoto = imagen.getAbsolutePath();//guardo path de la foto
        return imagen;
    }


    private void lanzarIntentFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //se fija si que se pueda hacer el intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //llamo a createImageFile para que cree la imagen
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {


            }
            //si el archivo fue creado sigo
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected void agregarUbicacion(){


        GPSTracker _tracker = new GPSTracker(this);
        Double location =_tracker.getLatitude();
        String _ubicacion = location.toString() + ' ';
        location = _tracker.getLongitude();
        _ubicacion = _ubicacion + location.toString();


        _cuerpoMensaje = _cuerpoMensaje + " Mi ultima ubicacion es : " +  _ubicacion;
        //agrego al mensaje original las coordenadas actuales
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
