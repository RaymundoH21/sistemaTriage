package com.example.sistematriage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/* Clase la cual contiene todos los atributos del paciente, se utiliza para recibir los datos de la BD*/

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
    private Double latitud;
    private Double longitud;
    private String destino;
    private Double altitud;
    private String ambulancia;
    private String cama;
    private String nombre;
    private String sexo;
    private String edad;
    private String lesiones;

    // Métodos para acceder y modificar los valores de las propiedades de esta clase

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

    public Double getLatitud() {
        return latitud;
    }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() {
        return longitud;
    }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public String getDestino() {
        return destino;
    }
    public void setDestino(String destino) { this.destino = destino; }

    public Double getAltitud() {
        return altitud;
    }
    public void setAltitud(Double altitud) { this.altitud = altitud; }

    public String getAmbulancia() {
        return ambulancia;
    }
    public void setAmbulancia(String ambulancia) { this.ambulancia = ambulancia; }

    public String getCama() {
        return cama;
    }
    public void setCama(String cama) { this.cama = cama; }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEdad() {
        return edad;
    }
    public void setEdad(String edad) { this.edad = edad; }

    public String getLesiones() {
        return lesiones;
    }
    public void setLesiones(String lesiones) { this.lesiones = lesiones; }

}


