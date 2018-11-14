package pem.tema4.modelo;

import java.io.Serializable;

public class InfoTemperatura implements Serializable {
    private String ciudad;
    private double[] tempMedia, presion, humedad, velViento, dirViento;
    private String[] tipoTiempo;

    public InfoTemperatura(String ciudad, double[] tempMedia, double[] presion, double[] humedad,
                           String[] tipoTiempo, double[] velViento, double[] dirViento) {
        this.ciudad = ciudad;
        this.tempMedia = tempMedia;
        this.presion = presion;
        this.humedad = humedad;
        this.tipoTiempo = tipoTiempo;
        this.velViento = velViento;
        this.dirViento = dirViento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public double[] getTempMedia() {
        return tempMedia;
    }

    public void setTempMedia(double[] tempMedia) {
        this.tempMedia = tempMedia;
    }

    public double[] getPresion() {
        return presion;
    }

    public void setPresion(double[] presion) {
        this.presion = presion;
    }

    public double[] getHumedad() {
        return humedad;
    }

    public void setHumedad(double[] humedad) {
        this.humedad = humedad;
    }

    public String[] getTipoTiempo() {
        return tipoTiempo;
    }

    public void setTipoTiempo(String[] tipoTiempo) {
        this.tipoTiempo = tipoTiempo;
    }

    public double[] getVelViento() {
        return velViento;
    }

    public void setVelViento(double[] velViento) {
        this.velViento = velViento;
    }

    public double[] getDirViento() {
        return dirViento;
    }

    public void setDirViento(double[] dirViento) {
        this.dirViento = dirViento;
    }
}
