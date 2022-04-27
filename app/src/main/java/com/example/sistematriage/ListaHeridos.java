package com.example.sistematriage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
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

import java.util.ArrayList;

public class ListaHeridos extends AppCompatActivity {

    HeridoAdapter adapter;

    ProgressDialog progress;
    StringRequest request;
    JsonObjectRequest jsonObjectRequest;

    RequestQueue request1;
    JsonObjectRequest jsonObjectRequest1;

    RecyclerView recyclerHeridos;
    ArrayList<herido> listaHeridos;
    ArrayList<herido> listaHeridosBackup;

    TextView NoPaciente;
    String stringNoPaciente;

    private RecyclerView.LayoutManager IManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_heridos);

        listaHeridos = new ArrayList<>();
        listaHeridosBackup = new ArrayList<>();

        recyclerHeridos = findViewById(R.id.reciclador);
        IManager = new LinearLayoutManager(this);
        recyclerHeridos.setLayoutManager(IManager);
        recyclerHeridos.setHasFixedSize(true);

        request1= Volley.newRequestQueue(this);
        adapter=new HeridoAdapter(listaHeridos);

        webService();
    }

    private void webService() {
        //progress = new ProgressDialog(this);
        //progress.setMessage("Cargando...");
        //progress.show();

        String url = "http://192.168.0.10/bd/ConsultarLista.php?";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                herido herido=null;

                JSONArray json=response.optJSONArray("paciente");

                try {

                    for (int i=0;i<json.length();i++){
                        herido = new herido();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        herido.setNoPaciente(jsonObject.optInt("NoPaciente"));
                        herido.setUbicacion(jsonObject.optString("Ubicacion"));
                        herido.setColor(jsonObject.optString("Color"));
                        herido.setEstado(jsonObject.optString("Estado"));
                        herido.setUsuario(jsonObject.optString("Usuario"));
                        herido.setDato(jsonObject.optString("Foto"));
                        listaHeridos.add(herido);
                        listaHeridosBackup.add(herido);
                    }


                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NoPaciente = view.findViewById(R.id.txtNoPaciente);
                            stringNoPaciente = NoPaciente.getText().toString();

                            APacienteSeleccionado(recyclerHeridos);
                        }
                    });

                    recyclerHeridos.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ListaHeridos.this, "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListaHeridos.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                //progress.hide();
            }
        });


        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }

    public void APacienteSeleccionado(View view){
        Intent Paciente = new Intent(this,PerfilHerido.class);
        Paciente.putExtra("NoPaciente",stringNoPaciente);
        startActivity(Paciente);
        finish();
    }

    public static Bitmap loadBitmapFrmView(View v, int width, int height) {
        Bitmap bmpImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpImg);
        v.draw(c);
        return bmpImg;
    }

    public static Bitmap getCircleBitmap(Bitmap bm) {

        int sice = Math.min((bm.getWidth()), (bm.getHeight()));

        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, sice, sice);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}