package wil.mirk.compmovil.parandroid.app;

import android.app.Application;


public class globalApp extends Application {


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