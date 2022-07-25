package com.example.sistematriage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button Inicio;
    EditText enumero, Contrasena;
    TextView estado;
    Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocalizacion();

        Inicio = (Button) findViewById(R.id.Inicio);

        enumero = findViewById(R.id.NumEmpleado);
        Contrasena = findViewById(R.id.Contrasena);

        estado = findViewById(R.id.tvSeleccion);
        spinner1 = (Spinner) findViewById(R.id.idSpinner);

        String [] opciones = {"Paramédico","Bombero"};

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_textview,opciones);
        spinner1.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Inicio = null;
        enumero = null;
        Contrasena = null;
        spinner1 = null;
        Runtime.getRuntime().gc();

    }

    public void mostrar(View view){
        String seleccion = spinner1.getSelectedItem().toString();
        if(seleccion.equals("Paramédico")){
            confirmarLogeo();
        }else if(seleccion.equals("Bombero")){
            confirmarLogeo2();
        }
    }
    private void limpiarCampo() {
        enumero.setText("");
        Contrasena.setText("");
    }
    public void cargarInicio() {
        final String numero = enumero.getText().toString();
        final String contra = Contrasena.getText().toString();
        //final String temporal = Integer.toString(numero);
        if(numero.isEmpty() && contra.isEmpty()) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
        else if(numero.isEmpty()){
            Toast.makeText(this,"Ingrese el Numero de Empleado",Toast.LENGTH_SHORT).show();
        }
        else if(contra.isEmpty()){
            Toast.makeText(this,"Ingrese su Contraseña",Toast.LENGTH_SHORT).show();

        }else{
            Response.Listener<String> respuesta = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean ok = jsonObject.getBoolean("success");

                        if (ok == true) {
                            String nombre = jsonObject.getString("nombre");
                            Intent intent = new Intent(MainActivity.this, ListaHeridos.class);
                            intent.putExtra("nombre",nombre);
                            startActivity(intent);
                            limpiarCampo();
                            finish();
                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setMessage("Fallo en el Logeo")
                                    .setNegativeButton("Reintentar", null)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }

            };
            LoginRequest r = new LoginRequest(numero, contra, respuesta);
            RequestQueue cola = Volley.newRequestQueue(MainActivity.this);
            cola.add(r);
        }

    }

    public void cargarInicio2() {
        final String num = enumero.getText().toString();
        final String contrasena = Contrasena.getText().toString();
        //final String temporal = Integer.toString(numero);
        if(num.isEmpty() && contrasena.isEmpty()) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
        else if(num.isEmpty()){
            Toast.makeText(this,"Ingrese el Numero de Empleado",Toast.LENGTH_SHORT).show();
        }
        else if(contrasena.isEmpty()){
            Toast.makeText(this,"Ingrese su Contraseña",Toast.LENGTH_SHORT).show();

        }else{
            Response.Listener<String> respuestados = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean ok = jsonObject.getBoolean("success");


                        if (ok == true) {
                            String nombre = jsonObject.getString("nombre");
                            Intent intent = new Intent(MainActivity.this, ListaHeridos.class);
                            intent.putExtra("nombre",nombre);
                            startActivity(intent);
                            limpiarCampo();
                            finish();

                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setMessage("Fallo en el Logeo")
                                    .setNegativeButton("Reintentar", null)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }

            };
            LoginRequestDos s = new LoginRequestDos(num, contrasena, respuestados);
            RequestQueue cola = Volley.newRequestQueue(MainActivity.this);
            cola.add(s);
        }

    }

    private void confirmarLogeo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Desea Iniciar Sesion?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cargarInicio();
            }
        });

        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void confirmarLogeo2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Desea Iniciar Sesion?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cargarInicio2();
            }
        });

        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    public void APantallaPrincipal(View view){
        Intent regresar = new Intent(this, MenuPrincipal.class);
        startActivity(regresar);
        finish();
    }

}