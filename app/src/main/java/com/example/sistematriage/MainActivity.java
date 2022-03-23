package com.example.sistematriage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button Registro, Inicio;
    EditText enumero, Contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Registro = (Button) findViewById(R.id.Registro);
        Inicio = (Button) findViewById(R.id.Inicio);

        enumero = findViewById(R.id.NumEmpleado);
        Contrasena = findViewById(R.id.Contrasena);

        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), registroparamedico.class));
            }
        });
        Inicio.setOnClickListener((v) -> {
            confirmarLogeo();
        });
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
                            Intent intent = new Intent(MainActivity.this, Principal.class);
                            intent.putExtra("nombre", nombre);
                            MainActivity.this.startActivity(intent);
                            limpiarCampo();
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
}