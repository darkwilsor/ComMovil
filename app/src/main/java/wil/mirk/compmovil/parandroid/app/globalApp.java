package wil.mirk.compmovil.parandroid.app;

import android.app.Application;


public class globalApp extends Application {

    //esta clase genera una aplicacion para poder usar variables globales,
    // principalmente porque muy avanzado el desarrollo vimos que se podia hacer con
    //sharedsettigs y no teniamos tiempo de modificar
    //el codigo e implementar lo necesario pero suponemos que en la presentacion
    //va a estar implementado con settings ya que es mas prolijo y nos podemos
    // librar de las variables harcodeadas



    private boolean _cancelado = false;

    String _passwordGlobal;


    public boolean getCancelado(){

        return  _cancelado;
    }

    public void setCancelado(boolean globalCancelado){

        _cancelado = globalCancelado;

    }

    public String getPasswordGlobal(){
        _passwordGlobal = "password";
        return _passwordGlobal;

    }


}