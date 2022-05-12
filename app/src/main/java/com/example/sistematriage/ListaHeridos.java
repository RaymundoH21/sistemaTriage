package com.example.sistematriage;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaHeridos extends AppCompatActivity {

    ProgressDialog pDialog;

    BottomNavigationView bottomNavigationView;

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

    ArrayList<String> filtros;
    boolean filtroColor;
    String cadenaFiltros;

    TextView tvTotal, tvRojo, tvAmarillo, tvVerde, tvNegro;

    public int t = 0, r = 0, a = 0, v = 0, n = 0;

    String total;
    String rojo;
    String amarillo;
    String verde;
    String negro;


    private RecyclerView.LayoutManager IManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_heridos);

        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvRojo = (TextView) findViewById(R.id.tvTRojos);
        tvAmarillo = (TextView) findViewById(R.id.tvTAmarillos);
        tvVerde = (TextView) findViewById(R.id.tvTVerdes);
        tvNegro = (TextView) findViewById(R.id.tvTNegros);

        filtroColor = false;
        cadenaFiltros = "";
        filtros = new ArrayList<>();

        listaHeridos = new ArrayList<>();
        listaHeridosBackup = new ArrayList<>();

        recyclerHeridos = findViewById(R.id.reciclador);
        IManager = new LinearLayoutManager(this);
        recyclerHeridos.setLayoutManager(IManager);
        recyclerHeridos.setHasFixedSize(true);

        request1= Volley.newRequestQueue(this);
        adapter=new HeridoAdapter(listaHeridos);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.listaheridos);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.listaheridos:
                        return true;

                    case R.id.registrarpaciente:
                        startActivity(new Intent(getApplicationContext(),RegistrarPaciente.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.mapa:
                        startActivity(new Intent(getApplicationContext(),Mapa.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.historial:
                        startActivity(new Intent(getApplicationContext(),HistorialRegistros.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        webService();



    }

    private void webService() {

        progress = new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.show();

        String url = "http://192.168.0.106/sistemaTriage/ConsultarLista.php?";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                progress.hide();
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

                        switch (herido.getColor()){
                            case "Rojo":
                                r++;
                                break;
                            case "Amarillo":
                                a++;
                                break;
                            case "Verde":
                                v++;
                                break;
                            case "Negro":
                                n++;
                                break;
                        }

                        t++;

                    }

                    total = "T: " + t;
                    rojo = "R: " + r;
                    amarillo = "A: " + a;
                    verde = "V: " + v;
                    negro = "N: " + n;

                    tvTotal.setText(total);
                    tvRojo.setText(rojo);
                    tvAmarillo.setText(amarillo);
                    tvVerde.setText(verde);
                    tvNegro.setText(negro);


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
                    progress.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListaHeridos.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                progress.hide();
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

    public void ANuevoPaciente(View view){
        Intent Paciente = new Intent(this,RegistrarPaciente.class);
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

    public void QuitarFiltro(){
        adapter.Filtrar(listaHeridosBackup);
        filtros.clear();
        filtroColor = false;
        cadenaFiltros = "";
    }

    public void Filtrar(String texto){
        ArrayList<herido> FiltrarLista = new ArrayList<>();

        for(herido Herido : listaHeridos) {
            if(Herido.getColor().toLowerCase().contains(texto.toLowerCase())){
                FiltrarLista.add(Herido);
            }
        }
        adapter.Filtrar(FiltrarLista);
    }

    public void FiltrarRojo(View view)
    {
        QuitarFiltro();
        Filtrar("Rojo");
    }

    public void FiltrarAmarillo(View view)
    {
        QuitarFiltro();
        Filtrar("Amarillo");
    }

    public void FiltrarVerde(View view)
    {
        QuitarFiltro();
        Filtrar("Verde");
    }

    public void FiltrarNegro(View view)
    {
        QuitarFiltro();
        Filtrar("Negro");
    }

    public void SinFiltro(View view)
    {
        QuitarFiltro();
    }
}