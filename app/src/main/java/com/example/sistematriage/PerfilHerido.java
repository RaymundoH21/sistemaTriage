package com.example.sistematriage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PerfilHerido extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    String NoPaciente;
    TextView tvColor, tvUbicacion, tvEstado, tvUsuario;
    ImageView imagen;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    RoundedBitmapDrawable roundedBitmapDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_herido);

        imagen = (ImageView) findViewById(R.id.ImgVFoto);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_photo_camera_24);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imagen.setImageDrawable(roundedBitmapDrawable);

        RecibirDatos();
        request = Volley.newRequestQueue(getBaseContext());
        webService();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        showToast("No se puede Consultar" + error.toString());
    }
    private void showToast(String s) { Toast.makeText(this,s, Toast.LENGTH_SHORT).show();}

    @Override
    public void onResponse(JSONObject response) {
        //showToast("Consulta Exitosa");
        herido miUsuario = new herido();
        JSONArray json = response.optJSONArray("paciente");
        JSONObject jsonObject= null;
        try {
            jsonObject=json.getJSONObject(0);
            miUsuario.setColor(jsonObject.optString("Color"));
            miUsuario.setUbicacion(jsonObject.optString("Ubicacion"));
            miUsuario.setEstado(jsonObject.optString("Estado"));
            miUsuario.setUsuario(jsonObject.optString("Usuario"));
            miUsuario.setDato(jsonObject.optString("imagen"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvColor.setText(miUsuario.getColor());
        tvUbicacion.setText(miUsuario.getUbicacion());
        tvEstado.setText(miUsuario.getEstado());
        tvUsuario.setText(miUsuario.getUsuario());


        if(miUsuario.getImagen()!=null) {
            roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), miUsuario.getImagen());
            roundedBitmapDrawable.setCircular(true);
            imagen.setImageDrawable(roundedBitmapDrawable);
        }
        else{
            imagen.setImageResource(R.drawable.ic_baseline_photo_camera_24);
        }

    }

    private void webService(){

        tvColor = (TextView) findViewById(R.id.txtColor);
        tvUbicacion = (TextView) findViewById(R.id.txtUbicacion);
        tvEstado = (TextView) findViewById(R.id.txtEstado);
        tvUsuario = (TextView) findViewById(R.id.txtUsuario);
        imagen = (ImageView) findViewById(R.id.ImgVFoto);

        String url = "http://192.168.0.106/sistemaTriage/ConsultarPaciente.php?NoPaciente="+NoPaciente;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

    }

    private void RecibirDatos()
    {
        Bundle extras = getIntent().getExtras();
        NoPaciente = extras.getString("NoPaciente");
    }

    public void Volver (View view){
        Intent regr = new Intent(PerfilHerido.this,ListaHeridos.class);
        startActivity(regr);
        finish();
    }
}