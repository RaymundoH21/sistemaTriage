package com.example.sistematriage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
                startActivity(new Intent(getApplicationContext(),registroparamedico.class));
            }
        });

        Inicio.setOnClickListener((v) -> {ultima();});

    }

    private void limpiarCampo() {
        enumero.setText("");
        Contrasena.setText("");
    }
    public void ultima(){
            final int numero = Integer.parseInt(enumero.getText().toString());
            final String contra = Contrasena.getText().toString();

            Response.Listener<String> respuesta = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean ok = jsonObject.getBoolean("success");
                        if(ok==true){

                            String nombre = jsonObject.getString("nombre");
                            Intent intent = new Intent(MainActivity.this,Principal.class);
                            intent.putExtra("nombre",nombre);
                            MainActivity.this.startActivity(intent);
                            limpiarCampo();
                        }else{
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setMessage("Fallo en el Logeo")
                                    .setNegativeButton("Reintentar",null)
                                    .show();
                        }
                    }catch(JSONException e){
                        e.getMessage();
                    }
                }

            };
            LoginRequest r = new LoginRequest(numero,contra,respuesta);
            RequestQueue cola = Volley.newRequestQueue(MainActivity.this);
            cola.add(r);
        }
}