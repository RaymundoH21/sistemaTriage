package com.example.sistematriage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class herido {

    private Integer noPaciente;
    private String ubicacion;
    private String color;
    private String estado;
    private String usuario;
    private String dato;
    private Bitmap imagen;
    private String rutaImagen;
    private String fecha;


    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try{
            byte[] byteCode = Base64.decode(dato,Base64.DEFAULT);
            this.imagen = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public Integer getNoPaciente() {
        return noPaciente;
    }
    public void setNoPaciente(Integer noPaciente) {
        this.noPaciente = noPaciente;
    }

    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getColor() {
        return color;
    }
    public void setColor(String color) { this.color = color; }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) { this.fecha = fecha; }

}


