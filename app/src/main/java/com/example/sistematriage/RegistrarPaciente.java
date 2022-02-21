package com.example.sistematriage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrarPaciente extends AppCompatActivity {

    EditText nom, apPat, apMat, edad, grav, les, sangre, aler, enfer, medi, dir, tel, nomFam, parFam, telFam;
    Button btnGuardar;

    RequestQueue request;
    StringRequest stringRequest;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_paciente);

        request = Volley.newRequestQueue(this);
        nom = ((EditText)findViewById(R.id.etNombre));
        apPat = ((EditText)findViewById(R.id.etAPaterno));
        apMat = ((EditText)findViewById(R.id.etAMaterno));
        edad = ((EditText)findViewById(R.id.etEdad));
        grav = ((EditText)findViewById(R.id.etEstado));
        les = ((EditText)findViewById(R.id.etLesiones));
        sangre = ((EditText)findViewById(R.id.etSangre));
        aler = ((EditText)findViewById(R.id.etAlergias));
        enfer = ((EditText)findViewById(R.id.etEnfermedades));
        medi = ((EditText)findViewById(R.id.etMedicamentos));
        dir = ((EditText)findViewById(R.id.etDireccion));
        tel = ((EditText)findViewById(R.id.etTelefono));
        nomFam = ((EditText)findViewById(R.id.etNombreFamiliar));
        parFam = ((EditText)findViewById(R.id.etParentesco));
        telFam = ((EditText)findViewById(R.id.etTelefonoFamiliar));

        btnGuardar = ((Button)findViewById(R.id.btnGuardar));
    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void cargarWebService(){

        progress = new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.show();

        String url="http://192.168.0.4/php/listaEspera/RegistroMovil.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ///if (response.trim().equalsIgnoreCase("Registra")) {
                nom.setText("");
                apPat.setText("");
                apMat.setText("");
                edad.setText("");
                grav.setText("");
                les.setText("");
                sangre.setText("");
                aler.setText("");
                enfer.setText("");
                medi.setText("");
                dir.setText("");
                tel.setText("");
                nomFam.setText("");
                parFam.setText("");
                telFam.setText("");


                //IV.setImageResource(R.drawable.imbase);

                Toast.makeText(RegistrarPaciente.this, response, Toast.LENGTH_LONG).show();
                showToast("Se ha Registrado Exitosamente");
                //Intent nuevoform = new Intent(RegistrarPaciente.this, listaEspera.class);
                //startActivity(nuevoform);
                finish();
                /*}else{
                    showToast("No se puede registrar");
                    Log.i("RESPUESTA: ",""+response);
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrarPaciente.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String Nombre = nom.getText().toString();
                String ApPaterno = apPat.getText().toString();
                String ApMaterno = apMat.getText().toString();
                String Edad = edad.getText().toString();
                String Gravedad = grav.getText().toString();
                String Lesiones = les.getText().toString();
                String Sangre = sangre.getText().toString();
                String Alergias = aler.getText().toString();
                String Enfermedades = enfer.getText().toString();
                String Medicamentos = medi.getText().toString();
                String Direccion = dir.getText().toString();
                String Telefono = tel.getText().toString();
                String NombreFam = nomFam.getText().toString();
                String Parentesco = parFam.getText().toString();
                String TelefonoFam = telFam.getText().toString();

                //String imagen = convertirImgString(bitmap);

                Map<String,String> parametros = new HashMap<>();

                parametros.put("Nombre",Nombre);
                parametros.put("ApPaterno",ApPaterno);
                parametros.put("ApMaterno",ApMaterno);
                parametros.put("Edad",Edad);
                parametros.put("Gravedad",Gravedad);
                parametros.put("Lesiones",Lesiones);
                parametros.put("Sangre",Sangre);
                parametros.put("Alergias",Alergias);
                parametros.put("Enfermedades",Enfermedades);
                parametros.put("Medicamentos",Medicamentos);
                parametros.put("Direccion",Direccion);
                parametros.put("Telefono",Telefono);
                parametros.put("NombreFam",NombreFam);
                parametros.put("Parentesco",Parentesco);
                parametros.put("TelefonoFam",TelefonoFam);

                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void PreguntaGuardar() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistrarPaciente.this);
        dialogo.setTitle("¿Deseas guardar la información?");
        dialogo.setMessage("Los datos se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cargarWebService();
            }
        });
        dialogo.setNegativeButton(Html.fromHtml("<font color='#696B6A'>Cancelar</font>"), new DialogInterface.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();

    }

}