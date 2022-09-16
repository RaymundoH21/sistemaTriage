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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

/* Esta clase pertenece a la actividad del historial, donde se muestra un recyclerView con todos los
   pacientes que acaban de ser registrados y están en espera de que llegue la ambulancia por ellos o
   que ya están siendo trasladados */

public class ListaHeridos extends AppCompatActivity {

    String NombreUsuario; // El nombre que se muestra en el toolbar

    TextView usuario;
    Toolbar toolbar; // barra de la parte superior

    BottomNavigationView bottomNavigationView; // Navbar de la parte superior

    HeridoAdapter adapter; // Objeto que permite mandar los datos a cada elemento del recyclerView

    ProgressDialog progress; // Barra de progreso
    StringRequest request; // Petición al servidor
    JsonObjectRequest jsonObjectRequest; // Petición al servidor

    RequestQueue request1;
    JsonObjectRequest jsonObjectRequest1;

    RecyclerView recyclerHeridos;
    ArrayList<herido> listaHeridos; // Lista que se manda al adapter
    ArrayList<herido> listaHeridosBackup; // Lista para guardar los valores recibidos de la BD y poder restablecerlos

    // Se utilzan para consultar la información de la BD del paciente al presionar sobre un elemento del recyclerView
    TextView NoPaciente;
    String stringNoPaciente;

    ArrayList<String> filtros;
    Boolean filtroColor;
    String cadenaFiltros;

    TextView tvTotal, tvRojo, tvAmarillo, tvVerde, tvNegro;

    public Integer t = 0, r = 0, a = 0, v = 0, n = 0; // contadores

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
    ArrayList<herido> FiltrarLista;

    // Se utiliza para guardar los datos de inicio de sesión
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_heridos);

        preferences = getSharedPreferences("sesiones",Context.MODE_PRIVATE); // Obtiene los datos de inicio de sesión guardados
        editor = preferences.edit();

        // Establece el color de la barra de estado en color blanco
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }

        getLocalizacion();

        lista = "Espera"; // Esta variable se utiliza para saber en que lista estamos, para que cuando nos regresemos de la pantalla de perfilHerido, regresemos a esta pantalla.
        // Referencia al elemento shimmerLayout que se muestra mientras se carga la información de la BD
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        // Referencias a los elementos del archivo activity_lista_heridos.xml
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
        adapter=new HeridoAdapter(listaHeridos, this);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.listaheridos);
        bottomNavigationView.setItemIconTintList(null);
        //bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

        // Listener para el navbar de la parte superior, cambia de activity entre las 4 activities principales (ListaHerido, RegistrarPaciente, MapaPrincipal, HistorialRegistros)
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.listaheridos:
                            return true;
                        case R.id.registrarpaciente:
                            Intent intent = new Intent(ListaHeridos.this,RegistrarPaciente.class);
                            intent.putExtra("nombre",nombre);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            finish();
                            return true;
                        case R.id.mapa:
                            Intent intent2 = new Intent(ListaHeridos.this,MapaPrincipal.class);
                            intent2.putExtra("nombre",nombre);
                            startActivity(intent2);
                            overridePendingTransition(0,0);
                            finish();
                            return true;
                        case R.id.historial:
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

        // Establece la división entre los elementos del recyclerView
        recyclerHeridos.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        webService(); // Llamada el método que hace la consulta a la base de datos

    }

    // Destructor de la clase
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //finish();
        jsonObjectRequest.cancel(); // Se cancela la petición para evitar que se cierre la aplicación
        // Todos los elementos son puestos en null para que el recolector de basura los pueda eliminar y liberar memoria
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
        FiltrarLista = null;
        Runtime.getRuntime().gc(); // Llamada al recolector de basura para liberar memoria
        //placePicutreimgView.setImageDrawable(null);

    }

    // Métodos para controlar los estados de la aplicación
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

    // Controlar la pulsacion del boton Atras
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

    // Se hace referencia al menú declarado en el archivo menu2.xml utilizado para el toolbar de la parte superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    // Se definen las opciones de los elementos del menú del toolbar de la parte superior
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item2:
                this.logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // Método para cerrar sesión y regresar al activity de inicio de sesión
    private void logout() {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.apply();
        this.finish();
        this.overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim);
         */
        editor.putBoolean("sesion", false);
        editor.apply();
        Intent intent = new Intent(this, MenuPrincipal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Método que hace la petición al servidor
    private void webService() {

        // un url indica la dirección hacia el archivo ubicado en el servidor local y otro al servidor web
        //url = "http://192.168.1.12/sistematriage/ConsultarLista.php";
        url = "http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/ConsultarLista.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                herido=null;

                json=response.optJSONArray("paciente");

                try {

                    // con este for se reciben todos los registros devueltos por la respuesta del servidor, almacenando la información en una lista de objetos de tipo herido
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
                            herido.setRutaImagen(jsonObject.optString("RutaFoto"));
                            herido.setLatitud(jsonObject.optDouble("Latitud"));
                            herido.setLongitud(jsonObject.optDouble("Longitud"));
                            herido.setAltitud(jsonObject.optDouble("Altitud"));
                            herido.setAmbulancia(jsonObject.optString("Ambulancia"));
                            listaHeridos.add(herido); // Cada objeto se añade a la listaHeridos
                            listaHeridosBackup.add(herido); // Se utiliza para mostrar otra vez todos los elementos al quitar los filtros

                            // Se van contando el total de personas de cada color
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

                        // Se detiene el efecto de carga
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        // Los totales de personas de cada color son concatenados en cadenas
                        total = "T: " + t;
                        rojo = "R: " + r;
                        amarillo = "A: " + a;
                        verde = "V: " + v;
                        negro = "N: " + n;

                        // Se asignan las cadenas de los totales a los textviews
                        tvTotal.setText(total);
                        tvRojo.setText(rojo);
                        tvAmarillo.setText(amarillo);
                        tvVerde.setText(verde);
                        tvNegro.setText(negro);

                        // Listener para definir un evento al presionar sobre un elemento de la lista (recyclerView)
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                NoPaciente = view.findViewById(R.id.txtNoPaciente); // se hace referencia al textview donde se encuentra el NoPaciente seleccionado
                                stringNoPaciente = NoPaciente.getText().toString(); // se toma el valor del textview del elemento del recyclerView donde se encuentra el Número del paciente seleccionado

                                APacienteSeleccionado(recyclerHeridos); // Se manda llamar el método que redirige al activity PerfilHerido
                            }
                        });

                        recyclerHeridos.setAdapter(adapter); // Actualiza los datos del recyclerView
                        webserviceTerminado = true;

                } catch (JSONException e) { // En caso de error se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
                    e.printStackTrace();
                    Toast.makeText(ListaHeridos.this, "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    webserviceTerminado = true;
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() { // En caso de error en la petición, se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
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
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest); // Se manda la petición
    }

    public void APacienteSeleccionado(View view){
        Paciente = new Intent(this,PerfilHerido.class);
        Paciente.putExtra("NoPaciente",stringNoPaciente); // Nombre del paciente del que se va a hacer la consultar al iniciar la actividad PerfilHerido
        Paciente.putExtra("nombre", nombre); // nombre del usuario
        Paciente.putExtra("lista", lista); // la lista desde la cual presionamos el elemento de la lista
        startActivity(Paciente);
        finish();
    }

    // Actualiza la activity, haciendo que se vuelva a ejecutar el método onCreate para cargar nuevos elementos de la base de datos en caso de que se hayan registrado
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

    // Muestra todos los registros obtenidos de la base de datos
    public void QuitarFiltro(){
        adapter.Filtrar(listaHeridosBackup);
        filtros.clear();
        filtroColor = false;
        cadenaFiltros = "";
    }

    // Método para filtrar los registros dependiendo la cadena que se le envíe como parámetro
    public void Filtrar(String texto){
        FiltrarLista = new ArrayList<>();

        for(herido Herido : listaHeridos) {
            if(Herido.getColor().toLowerCase().contains(texto.toLowerCase())){
                FiltrarLista.add(Herido);
            }
        }
        adapter.Filtrar(FiltrarLista);
    }

    // Métodos para filtrar por color, primero quitan el filtro anterior y mandan llamar el método Filtrar con la cadena correspondiente
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

    // Pide permisos para obtener utilizar la ubicación
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