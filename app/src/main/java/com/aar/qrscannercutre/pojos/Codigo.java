package com.aar.qrscannercutre.pojos;

public class Codigo
{

    private String nombre;
    private String nombre_imagen;
    private String tipo;
    private String path;
    private String fecha;


    public Codigo()
    {

    }

    public Codigo(String nombre, String nombre_imagen, String tipo, String path, String fecha)
    {
        this.nombre = nombre;
        this.nombre_imagen = nombre_imagen;
        this.tipo = tipo;
        this.path = path;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombre_imagen() {
        return nombre_imagen;
    }

    public void setNombre_imagen(String nombre_imagen) {
        this.nombre_imagen = nombre_imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
