package pem.tema4.modelo.bajo_nivel;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import pem.tema4.AppMediador;
import pem.tema4.modelo.AccesoServidor;

public class ServicioLocalizacion extends Service implements LocationListener {

    // Distancia mínima en metros entre actualizaciones
    private static final long DISTANCIA_MINIMA_ENTRE_ACTUALIZACIONES = 0; // junto a...
    // Tiempo mínimo en milisegundos entre actualizaciones
    private static final long TIEMPO_MINIMO_ENTRE_ACTUALIZACIONES = 0;  // al instante
    boolean gpsEstaHabilitado = false;
    protected LocationManager locationManager;
    private AppMediador appMediador;

    @Override
    public void onCreate() {
        appMediador = AppMediador.getInstance();
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        gpsEstaHabilitado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsEstaHabilitado) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    TIEMPO_MINIMO_ENTRE_ACTUALIZACIONES,
                    DISTANCIA_MINIMA_ENTRE_ACTUALIZACIONES, this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Recupera la información del GPS sobre la ubicación del usuario y solicita la información meteorológica al
        // servidor RESTful de open weather

        // Url para encontrar la información sobre el momento actual
        String urlServidor_Actual = "http://api.openweathermap.org/data/2.5/weather"+
                "?lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() +
                "&units=" + AppMediador.UNIDAD +
                "&mode=json" +
                "&appid=" + AppMediador.CLAVE;

        // Url para encontrar la información con una previsión de 5 días
        String urlServidor_5Forecast = "http://api.openweathermap.org/data/2.5/forecast/daily"+
                "?lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() +
                "&units=" + AppMediador.UNIDAD +
                "&cnt=5" +
                "&mode=json" +
                "&appid=" + AppMediador.CLAVE;
        new AccesoServidor().execute(urlServidor_Actual, urlServidor_5Forecast);
        locationManager.removeUpdates(this);
        this.stopSelf();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
