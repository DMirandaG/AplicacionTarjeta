package pem.tema4.modelo;

import pem.tema4.AppMediador;
import pem.tema4.modelo.bajo_nivel.ServicioLocalizacion;

public class Modelo implements IModelo {

    private static Modelo singleton = null;
    private AppMediador appMediador;
    private ServicioLocalizacion localizacion;

    private Modelo() {
        appMediador = AppMediador.getInstance();
        localizacion = new ServicioLocalizacion();
    }

    public static Modelo getInstance() {
        if (singleton == null)
            singleton = new Modelo();
        return singleton;
    }

    // TODO Método redefinido actualizarTarjetas que recupera la información a presentar en la vista. Para ello, lanza el
    // servicio que recupera la información del GPS sobre la ubicación del usuario

    @Override
    public void actualizarTarjetas() {
        appMediador.launchService(localizacion.getClass(),null);
    }

}
