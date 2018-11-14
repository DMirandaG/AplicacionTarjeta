package pem.tema4.modelo;

import android.os.AsyncTask;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pem.tema4.AppMediador;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AccesoServidor  extends AsyncTask<String, Void, Void> {
    private String datosRecuperados;
    private boolean error;
    private AppMediador appMediador;
    private InfoTemperatura infoTemperatura;

    public AccesoServidor() {
        datosRecuperados = "";
        error = false;
        appMediador = AppMediador.getInstance();
    }

    // Se llama después del método onPreExecute
    protected Void doInBackground(String... urls) {
        BufferedReader reader = null;
        // Se envían los datos
        try {
            // Se recuperan los datos del momento actual
            URL url = new URL(urls[0]);
            datosRecuperados = recuperarDatos(url);
            InfoTemperatura infoTemperatura_actual = getActual(datosRecuperados);
            // Se recuperan los datos de la previsión de 5 días
            url = new URL(urls[1]);
            datosRecuperados = recuperarDatos(url);
            InfoTemperatura infoTemperatura_5Forecast = get5DiasForecast(datosRecuperados);

            // TODO Inicalice los objetos temperaturas, presion, humedad, velViento y dirViento (de tipo double[] y tipoTiempo (de tipo
            // String[]). El contenido de estos objetos será: la posición 0, el valor correspondiente
            // en el objeto infoTemperatura_actual y el valor de las entradas 1 en adelante, el valor correspondiente
            // en el objeto infoTemperatura_5Forecast. Una vez definidos los objetos e inciiados con su valor adecuado, cree
            // un nuevo objeto de tipo InfoTemperatura con la información y envíelo al presentador
            double[] temperaturas = infoTemperatura_5Forecast.getTempMedia();
            temperaturas[0] = infoTemperatura_actual.getTempMedia()[0];
            double[] presion = infoTemperatura_5Forecast.getPresion();
            presion[0] = infoTemperatura_actual.getPresion()[0];
            double[] humedad = infoTemperatura_5Forecast.getHumedad();
            humedad[0] = infoTemperatura_actual.getHumedad()[0];
            double[] velViento = infoTemperatura_5Forecast.getVelViento();
            velViento[0] = infoTemperatura_actual.getVelViento()[0];
            double[] dirViento = infoTemperatura_5Forecast.getDirViento();
            dirViento[0] = infoTemperatura_actual.getDirViento()[0];
            String[] tipoTiempo = infoTemperatura_5Forecast.getTipoTiempo();
            tipoTiempo[0] = infoTemperatura_actual.getTipoTiempo()[0];
            String ciudad = infoTemperatura_actual.getCiudad();
            // Se envía al presentador la información recopilada
            infoTemperatura = new InfoTemperatura(ciudad, temperaturas, presion, humedad, tipoTiempo, velViento, dirViento);
            Bundle extras = new Bundle();
            extras.putSerializable(AppMediador.CLAVE_INFORMACION, infoTemperatura);
            AppMediador.getInstance().sendBroadcast(AppMediador.AVISO_NUEVA_INFORMACION, extras);

        } catch (Exception ex) {
            AppMediador.getInstance().sendBroadcast(AppMediador.AVISO_NUEVA_INFORMACION, null);
        }
        return null;
    }

    private String recuperarDatos(URL url) {
        BufferedReader reader = null;
        // Se envían los datos
        try {
            // Se envía petición POST al servidor web RESTful
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.connect();
            // Se obtiene la respuesta
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + " ");
            }
            // Append Server Response To Content String
            return sb.toString();
        } catch (Exception ex) {
            error = true;
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                error = true;
            }
        }
        return null;
    }

    private InfoTemperatura get5DiasForecast(String datosRecuperados) throws JSONException {
        // Crea un nuevo objeto JSON a partir de los datos recuperados desde el servidor
        // Se sigue la información presentada en: https://openweathermap.org/forecast16#JSON
        JSONObject respuestaJSON = new JSONObject(datosRecuperados);
        // Obtiene el nodo principal city
        JSONObject datos = respuestaJSON.optJSONObject("city");
        String ciudad = datos.optString("name"); // nombre de la ciudad
        JSONArray lista = respuestaJSON.optJSONArray("list");
        int cont = 5;
        if (cont > lista.length())
            cont = lista.length();
        double[] temperaturas = new double[cont+1];
        double[] presion = new double[cont+1];
        double[] humedad = new double[cont+1];
        String[] condicion = new String[cont+1];
        double[] velViento = new double[cont+1];
        double[] dirViento = new double[cont+1];
        for (int i = 0; i < cont; i++) {
            JSONObject nodoHijoJSON = lista.getJSONObject(i);
            // Se coge el campo temp que tiene los valores de temperatura para extraer los campos min y max
            JSONObject nodoTemp = (JSONObject) nodoHijoJSON.get("temp");
            double tempMin = nodoTemp.optDouble("min");
            double tempMax = nodoTemp.optDouble("max");
            double tempMedia = (tempMin + tempMax) / 2; // temperatura media (según unidad) de la ciudad
            temperaturas[i] = tempMedia;
            presion[i] = nodoHijoJSON.optDouble("pressure"); // presión atmosférica (en hPa) de la ciudad
            humedad[i] = nodoHijoJSON.optDouble("humidity"); // humedad (en %) de la ciudad
            JSONArray nodoWeather = (JSONArray) nodoHijoJSON.optJSONArray("weather");
            JSONObject itemWeather = (JSONObject) nodoWeather.getJSONObject(0);
            condicion[i] = itemWeather.optString("main"); // tipo de tiempo de la ciudad
            velViento[i] = nodoHijoJSON.optDouble("speed");; // velocidad del viento (en m/s) de la ciudad
            dirViento[i] = nodoHijoJSON.optDouble("deg");; // dirección del viento (en grados) de la ciudad
        }
        // se desprecia el último valor
        for (int i = cont ; i > 0 ; i--) {
            temperaturas[i] = temperaturas[i-1];
            presion[i] = presion[i-1];
            humedad[i] = humedad[i-1];
            condicion[i] = condicion[i-1];
            velViento[i] = velViento[i-1];
            dirViento[i] = dirViento[i-1];
        }
        return new InfoTemperatura(ciudad, temperaturas, presion, humedad,
                condicion, velViento, dirViento);
    }

    private InfoTemperatura getActual(String datosRecuperados) throws JSONException {
        // Crea un nuevo objeto JSON a partir de los datos recuperados desde el servidor
        // Se sigue la información presentada en: https://openweathermap.org/current#current_JSON
        double[] temperaturas = new double[1];
        double[] presion = new double[1];
        double[] humedad = new double[1];
        String[] condicion = new String[1];
        double[] velViento = new double[1];
        double[] dirViento = new double[1];
        JSONObject respuestaJSON = new JSONObject(datosRecuperados);
        String ciudad = respuestaJSON.optString("name"); // nombre de la ciudad
        JSONArray nodoWeather = (JSONArray) respuestaJSON.optJSONArray("weather");
        JSONObject itemWeather = (JSONObject) nodoWeather.getJSONObject(0);
        condicion[0] = itemWeather.optString("main"); // tipo de tiempo de la ciudad
        JSONObject nodoMain = respuestaJSON.getJSONObject("main");
        temperaturas[0] = nodoMain.optDouble("temp"); // temperatura media (según unidad) de la ciudad
        presion[0] = nodoMain.optDouble("pressure"); // presión atmosférica (en hPa) de la ciudad
        humedad[0] = nodoMain.optDouble("humidity"); // humedad (en %) de la ciudad
        JSONObject nodoWind = respuestaJSON.getJSONObject("wind");
        velViento[0] = nodoWind.optDouble("speed"); // velocidad del viento (en m/s) de la ciudad
        dirViento[0] = nodoWind.optDouble("deg"); // dirección del viento (en grados) de la ciudad
        return new InfoTemperatura(ciudad, temperaturas, presion, humedad,
                condicion, velViento, dirViento);
    }
}
