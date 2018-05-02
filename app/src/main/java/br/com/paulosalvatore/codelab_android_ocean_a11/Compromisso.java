package br.com.paulosalvatore.codelab_android_ocean_a11;

/**
 * Created by paulo on 01/05/2018.
 */

public class Compromisso {
    private int id;
    private String titulo;
    private double latitude;
    private double longitude;
    private long data;

    public Compromisso(int id, String titulo, double latitude, double longitude, long data) {
        this.id = id;
        this.titulo = titulo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
    }

    public Compromisso(String titulo, double latitude, double longitude, long data) {
        this.titulo = titulo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
