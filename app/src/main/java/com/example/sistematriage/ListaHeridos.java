package com.example.sistematriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Icon;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaHeridos extends AppCompatActivity {

    String NombreUsuario;

    private SharedPreferences prefs;
    TextView usuario;
    Toolbar toolbar;

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
    Boolean filtroColor;
    String cadenaFiltros;

    TextView tvTotal, tvRojo, tvAmarillo, tvVerde, tvNegro;

    public Integer t = 0, r = 0, a = 0, v = 0, n = 0;

    String total;
    String rojo;
    String amarillo;
    String verde;
    String negro;


    private RecyclerView.LayoutManager IManager;

    private ShimmerFrameLayout shimmerFrameLayout;

    String nombre;

    JSONObject jsonObject;
    String url;
    herido herido;
    JSONArray json;
    Boolean webserviceTerminado = false;
    Intent Paciente;
    String lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_heridos);


        getLocalizacion();

        lista = "Espera";
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuario = (TextView) findViewById(R.id.NombresUsuario);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");

        usuario.setText(nombre);

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

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.listaheridos);
        bottomNavigationView.setItemIconTintList(null);
        //bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId())
                    {
                        case R.id.listaheridos:
                            return true;

                        case R.id.registrarpaciente:
                            //startActivity(new Intent(getApplicationContext(),RegistrarPaciente.class));
                            Intent intent = new Intent(ListaHeridos.this,RegistrarPaciente.class);
                            intent.putExtra("nombre",nombre);
                            startActivity(intent);
                            /*startActivity(intent
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                            overridePendingTransition(0,0);
                            finish();
                            //finish();
                            return true;

                        case R.id.mapa:
                            //startActivity(new Intent(getApplicationContext(),Mapa.class));
                            Intent intent2 = new Intent(ListaHeridos.this,MapaPrincipal.class);
                            intent2.putExtra("nombre",nombre);
                            startActivity(intent2);
                            overridePendingTransition(0,0);
                            finish();
                            return true;

                        case R.id.historial:
                            //startActivity(new Intent(getApplicationContext(),HistorialRegistros.class));
                            Intent intent3 = new Intent(ListaHeridos.this,HistorialRegistros.class);
                            intent3.putExtra("nombre",nombre);
                            startActivity(intent3);
                            overridePendingTransition(0,0);
                            finish();
                            return true;

                    }
                    return false;
                }
            });

        recyclerHeridos.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        webService();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //finish();
        jsonObjectRequest.cancel();
        json = null;
        request = null;
        jsonObjectRequest = null;
        request1 = null;
        jsonObjectRequest1 = null;
        filtros = null;
        progress = null;
        adapter.setOnClickListener(null);
        adapter = null;
        usuario = null;
        toolbar = null;
        NoPaciente = null;
        stringNoPaciente = null;
        NombreUsuario = null;
        bottomNavigationView = null;
        cadenaFiltros = null;
        tvTotal = null;
        tvRojo = null;
        tvAmarillo = null;
        tvVerde = null;
        tvNegro = null;
        IManager = null;
        total = null;
        rojo = null;
        amarillo = null;
        verde = null;
        negro = null;
        shimmerFrameLayout = null;
        nombre = null;
        prefs = null;
        filtroColor = null;
        t = null;
        r = null;
        a = null;
        v = null;
        n = null;
        jsonObject = null;
        url = null;
        herido = null;
        recyclerHeridos = null;
        listaHeridos = null;
        listaHeridosBackup = null;
        Paciente = null;
        lista = null;
        Runtime.getRuntime().gc();
        //placePicutreimgView.setImageDrawable(null);

    }


    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    //Controlar la pulsacion del boton Atras
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir de sistema triage?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item2:
                this.logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.apply();
        this.finish();
        this.overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim);
         */
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void webService() {

        //String url = "http://192.168.1.12/sistematriage/ConsultarLista.php";
        url = "http://ec2-54-219-50-144.us-west-1.compute.amazonaws.com/ConsultarLista.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                herido=null;

                json=response.optJSONArray("paciente");

                try {

                        for (int i=0;i<json.length();i++){
                            herido = new herido();
                            jsonObject=null;
                            jsonObject=json.getJSONObject(i);

                            herido.setNoPaciente(jsonObject.optInt("NoPaciente"));
                            herido.setUbicacion(jsonObject.optString("Ubicacion"));
                            herido.setColor(jsonObject.optString("Color"));
                            herido.setEstado(jsonObject.optString("Estado"));
                            herido.setUsuario(jsonObject.optString("Usuario"));
                            herido.setDato(jsonObject.optString("Foto"));
                            herido.setLatitud(jsonObject.optDouble("Latitud"));
                            herido.setLongitud(jsonObject.optDouble("Longitud"));
                            herido.setAltitud(jsonObject.optDouble("Altitud"));
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

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
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
                        webserviceTerminado = true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ListaHeridos.this, "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    webserviceTerminado = true;
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListaHeridos.this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                webserviceTerminado = true;
                //progress.hide();
            }
        });


        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }

    public void APacienteSeleccionado(View view){
        Paciente = new Intent(this,PerfilHerido.class);
        Paciente.putExtra("NoPaciente",stringNoPaciente);
        Paciente.putExtra("nombre", nombre);
        Paciente.putExtra("lista", lista);
        startActivity(Paciente);
        finish();
    }

    public void RefrescarLista(View view){
        Paciente = new Intent(this, ListaHeridos.class);
        Paciente.putExtra("NoPaciente",stringNoPaciente);
        Paciente.putExtra("nombre", nombre);
        startActivity(Paciente
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(0,0);
        startActivity(Paciente);
        finish();
    }

    public void ANuevoPaciente(View view){
        Paciente = new Intent(this,RegistrarPaciente.class);
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

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(ListaHeridos.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ListaHeridos.this, Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(ListaHeridos.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
}