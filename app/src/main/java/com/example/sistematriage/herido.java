package com.example.sistematriage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class herido {

    private Integer noPaciente;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String sexo;
    private Integer edad;
    private String gravedad;
    private String lesiones;
    private String tipoSangre;
    private String alergias;
    private String enfermedades;
    private String medicamentos;
    private String direccion;
    private String tel;
    private String nombreFam;
    private String parentesco;
    private String telFam;
    private String dato;
    private Bitmap imagen;
    private String rutaImagen;


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
    public void setNoPaciente(Integer organo) {
        this.noPaciente = noPaciente;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApPaterno() {
        return apPaterno;
    }
    public void setApPaterno(String apPaterno) { this.apPaterno = apPaterno; }

    public String getApMaterno() {
        return apMaterno;
    }
    public void setApMaterno(String apMaterno) { this.apMaterno = apMaterno; }

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public Integer getEdad() {
        return edad;
    }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getGravedad() {
        return gravedad;
    }
    public void setGravedad(String gravedad) { this.gravedad = gravedad; }

    public String getLesiones() {
        return lesiones;
    }
    public void setLesiones(String lesiones) { this.lesiones = lesiones; }

    public String getTipoSangre() {
        return tipoSangre;
    }
    public void setTipoSangre(String tipoSangre) { this.tipoSangre = tipoSangre; }

    public String getAlergias() {
        return alergias;
    }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getEnfermedades() { return enfermedades; }
    public void setEnfermedades(String enfermedades) { this.enfermedades = enfermedades; }

    public String getMedicamentos() { return medicamentos; }
    public void setMedicamentos(String medicamentos) { this.medicamentos = medicamentos; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getNombreFam() { return nombreFam; }
    public void setNombreFam(String nombreFam) { this.nombreFam = nombreFam; }

    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }

    public String getTelFam() { return telFam; }
    public void setTelFam(String telFam) { this.telFam = telFam; }

    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.telFam = rutaImagen; }

}


