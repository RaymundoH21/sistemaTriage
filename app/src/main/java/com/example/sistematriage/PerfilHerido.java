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
    TextView tvNombre, tvApPat, tvApMat, tvSexo, tvEdad, tvGravedad, tvLesiones, tvSangre, tvAlergias, tvEnfermedades, tvMedicamentos, tvDireccion, tvTel, tvNombreFam, tvParentesco, tvTelFam;
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
            miUsuario.setNombre(jsonObject.optString("Nombre"));
            miUsuario.setApPaterno(jsonObject.optString("ApPaterno"));
            miUsuario.setApMaterno(jsonObject.optString("ApMaterno"));
            miUsuario.setSexo(jsonObject.optString("Sexo"));
            miUsuario.setEdad(jsonObject.optInt("Edad"));
            miUsuario.setGravedad(jsonObject.optString("Gravedad"));
            miUsuario.setLesiones(jsonObject.optString("Lesiones"));
            miUsuario.setTipoSangre(jsonObject.optString("Sangre"));
            miUsuario.setAlergias(jsonObject.optString("Alergias"));
            miUsuario.setEnfermedades(jsonObject.optString("Enfermedades"));
            miUsuario.setMedicamentos(jsonObject.optString("Medicamentos"));
            miUsuario.setDireccion(jsonObject.optString("Direccion"));
            miUsuario.setTel(jsonObject.optString("Tel"));
            miUsuario.setNombreFam(jsonObject.optString("NombreFam"));
            miUsuario.setParentesco(jsonObject.optString("Parentesco"));
            miUsuario.setTelFam(jsonObject.optString("TelFam"));
            miUsuario.setDato(jsonObject.optString("imagen"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvNombre.setText(miUsuario.getNombre()+" "+ miUsuario.getApPaterno() + " " + miUsuario.getApMaterno());
        tvSexo.setText(miUsuario.getSexo());
        tvEdad.setText(String.valueOf(miUsuario.getEdad()));
        tvGravedad.setText(miUsuario.getGravedad());
        tvLesiones.setText(miUsuario.getLesiones());
        tvSangre.setText(miUsuario.getTipoSangre());
        tvAlergias.setText(miUsuario.getAlergias());
        tvEnfermedades.setText(miUsuario.getEnfermedades());
        tvMedicamentos.setText(miUsuario.getMedicamentos());
        tvDireccion.setText(miUsuario.getDireccion());
        tvTel.setText(miUsuario.getTel());
        tvNombreFam.setText(miUsuario.getNombreFam());
        tvParentesco.setText(miUsuario.getParentesco());
        tvTelFam.setText(miUsuario.getTelFam());

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

        tvNombre = (TextView) findViewById(R.id.txtNombre);
        tvSexo = (TextView) findViewById(R.id.txtSexo);
        tvEdad = (TextView) findViewById(R.id.txtEdad);
        tvGravedad = (TextView) findViewById(R.id.txtGravedad);
        tvLesiones = (TextView) findViewById(R.id.txtLesiones);
        tvSangre = (TextView) findViewById(R.id.txtSangre);
        tvAlergias = (TextView) findViewById(R.id.txtAlergias);
        tvEnfermedades = (TextView) findViewById(R.id.txtEnfermedades);
        tvMedicamentos = (TextView) findViewById(R.id.txtMedicamentos);
        tvDireccion = (TextView) findViewById(R.id.txtDireccion);
        tvTel =(TextView) findViewById(R.id.txtTelefono);
        tvNombreFam =(TextView) findViewById(R.id.txtNombreFam);
        tvParentesco = findViewById(R.id.txtParentesco);
        tvTelFam = findViewById(R.id.txtTelFamiliar);
        imagen = (ImageView) findViewById(R.id.ImgVFoto);

        String url = "http://192.168.0.10/bd/ConsultarPaciente.php?NoPaciente="+NoPaciente;
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