package pem.tema4.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import pem.tema4.AppMediador;
import pem.tema4.modelo.IModelo;
import pem.tema4.modelo.InfoTemperatura;
import pem.tema4.modelo.Modelo;
import pem.tema4.vista.IVistaTarjeta;
import pem.tema4.vista.VistaTarjeta;

public class PresentadorTarjeta implements IPresentadorTarjeta {

    // TODO Declare un objeto IModelo, un objeto AppMediador y un objeto IVistaTarjeta.

    private static AppMediador appMediador;
    private static IModelo iModelo;


    // TODO Declare y cree un receptor de avisos para que atienda al aviso AVISO_NUEVA_INFORMACION. Al recibir este aviso
    // el receptor recupera los datos (de tipo InfoTemperatura) y crea un array de Object con la siguiente información en
    // sus entradas:
    // 0: nombre del lugar
    // 1: temperatura media del lugar (actual + 5 días de previsión)
    // 2: presión del lugar (actual + 5 días de previsión)
    // 3: humedad del lugar (actual + 5 días de previsión)
    // 4: velocidad del viento del lugar (actual + 5 días de previsión)
    // 5: dirección del viento del lugar (actual + 5 días de previsión)
    // 6: tipoTiempo del clima (tipo del clima) del lugar (actual + 5 días de previsión)
    // A continuación le pide a la vista que actualice la interfaz de usuario (tarjetas)

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_NUEVA_INFORMACION)){
                InfoTemperatura infoTemperatura = (InfoTemperatura) intent.getSerializableExtra(AppMediador.CLAVE_INFORMACION);
                if(infoTemperatura != null){
                    Object[] datos = new Object[7];
                    datos[0] = infoTemperatura.getCiudad();
                    datos[1] = infoTemperatura.getTempMedia();
                    datos[2] = infoTemperatura.getPresion();
                    datos[3] = infoTemperatura.getHumedad();
                    datos[4] = infoTemperatura.getVelViento();
                    datos[5] = infoTemperatura.getDirViento();
                    datos[6] = infoTemperatura.getTipoTiempo();
                    appMediador.getVistaMapa().actualizarInformacion(datos);
                }
            }
            appMediador.unRegisterReceiver(this);
        }
    };




    // TODO Cree, de forma adecuada, los objetos de tipo IModelo, AppMediador e IVistaTarjeta declarados en la clase

    public PresentadorTarjeta() {
        appMediador = AppMediador.getInstance();
        iModelo = Modelo.getInstance();
    }

    // TODO Método redefinido actualizarTarjetas que inicia la aplicación, recibiendo desde el GPS la ubicación del terminal
    // del usuario y recuperando la información sobre la temperatura, presión, humedad, velocidad y dirección del viento y
    // actualiza las tarjetas presentes en la vista

    @Override
    public void actualizarTarjetas() {
        AppMediador.getInstance().registerReceiver(receptorDeAvisos, AppMediador.AVISO_NUEVA_INFORMACION);
        iModelo.actualizarTarjetas();
    }

}
