package com.example.sistematriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* Esta clase pertenece a la actividad del historial, donde se muestra un recyclerView con todos los
   pacientes que han sido registrados y que ya fueron entregados ya sea al hospital,
   SEMEFO o fueon dados de alta en el lugar de los hechos */

public class HistorialRegistros extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; // Navbar inferior
    TextView usuario; // Nombre del usuario mostrado en la parte superior
    Toolbar toolbar; // navbar superior

    ArrayList<String> filtros;
    Boolean filtroColor;
    String cadenaFiltros;

    JsonObjectRequest jsonObjectRequest; // petición al servidor

    TextView NoPaciente;
    String stringNoPaciente;
    TextView tvTotal, tvRojo, tvAmarillo, tvVerde, tvNegro;
    String total;
    String rojo;
    String amarillo;
    String verde;
    String negro;


    private RecyclerView.LayoutManager IManager;

    RecyclerView recyclerHistorial;
    ArrayList<herido> historial;
    ArrayList<herido> listaHeridosBackups;

    HistorialAdapter adapter; // objeto de la clase que definimos en el archivo HistorialAdapter
    RequestQueue request1;

    String nombre;

    public Integer t = 0, r = 0, a = 0, v = 0, n = 0;

    private ShimmerFrameLayout shimmerFrameLayout;

    herido herido;
    JSONArray json;
    String url;
    ArrayList<herido> FiltrarLista;
    JSONObject jsonObject;
    Intent Paciente;
    String lista;

    SharedPreferences preferences; // Se utiliza para guardar los datos de inicio de sesión
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_registros);

        preferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE); // Obtiene los datos de inicio de sesión guardados
        editor = preferences.edit();

        // Establece el color de la barra de estado en color blanco
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }

        // Referencia al elemento shimmerLayout que se muestra mientras se carga la información de la BD
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        // Referencia a los demás elementos del archivo activity_historial_registros.xml
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvRojo = (TextView) findViewById(R.id.tvTRojos);
        tvAmarillo = (TextView) findViewById(R.id.tvTAmarillos);
        tvVerde = (TextView) findViewById(R.id.tvTVerdes);
        tvNegro = (TextView) findViewById(R.id.tvTNegros);

        filtroColor = false;
        cadenaFiltros = "";
        filtros = new ArrayList<>();


        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.historial);
        bottomNavigationView.setItemIconTintList(null);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuario = (TextView) findViewById(R.id.NombresUsuario);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");

        usuario.setText(nombre);

        recyclerHistorial = findViewById(R.id.reciclador2);
        IManager = new LinearLayoutManager(this);
        recyclerHistorial.setLayoutManager(IManager);
        recyclerHistorial.setHasFixedSize(true);

        historial = new ArrayList<>();
        listaHeridosBackups = new ArrayList<>();

        request1= Volley.newRequestQueue(this);
        adapter=new HistorialAdapter(historial, this);

        // Listener para el navbar de la parte superior, cambia de activity entre las 4 activities principales (ListaHerido, RegistrarPaciente, MapaPrincipal, HistorialRegistros)
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.listaheridos:
                        //startActivity(new Intent(getApplicationContext(),ListaHeridos.class));
                        Intent intent = new Intent(HistorialRegistros.this,ListaHeridos.class);
                        intent.putExtra("nombre",nombre);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.registrarpaciente:
                        //startActivity(new Intent(getApplicationContext(),RegistrarPaciente.class));
                        Intent intent2 = new Intent(HistorialRegistros.this,RegistrarPaciente.class);
                        intent2.putExtra("nombre",nombre);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.mapa:
                        //startActivity(new Intent(getApplicationContext(),Mapa.class));
                        Intent intent3 = new Intent(HistorialRegistros.this,MapaPrincipal.class);
                        intent3.putExtra("nombre",nombre);
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.historial:
                        return true;

                }
                return false;
            }
        });

        // Establece la división entre los elementos del recyclerView
        recyclerHistorial.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
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
        recyclerHistorial.setLayoutManager(null);
        historial = null;
        listaHeridosBackups = null;
        filtros = null;
        recyclerHistorial = null;
        jsonObjectRequest = null;
        request1 = null;
        adapter = null;
        usuario = null;
        toolbar = null;
        NoPaciente = null;
        stringNoPaciente = null;
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
        filtroColor = null;
        t = null;
        r = null;
        a = null;
        v = null;
        n = null;
        herido = null;
        json = null;
        url = null;
        jsonObject=null;
        Paciente = null;
        lista = null;
        ArrayList<herido> FiltrarLista = null;
        Runtime.getRuntime().gc(); // Llamada al recolector de basura para liberar memoria
        //placePicutreimgView.setImageDrawable(null);

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
        editor.putBoolean("sesion", false); // Elimina los datos de acceso, para que al volver a abrir la aplicación se vuelvan a pedir
        editor.apply();
        Intent intent = new Intent(this, MenuPrincipal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Método que hace la petición al servidor
    private void webService() {
        //progress = new ProgressDialog(this);
        //progress.setMessage("Cargando...");
        //progress.show();

        // un url indica la dirección hacia el archivo ubicado en el servidor local y otro al servidor web
        //url = "http://192.168.1.12/sistematriage/ConsultarListas2.php";
        url = "http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/ConsultarListas2.php";

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
                        herido.setDestino(jsonObject.optString("Destino"));
                        historial.add(herido); // Cada objeto se añade a la lista historial
                        listaHeridosBackups.add(herido); // Se utiliza para mostrar otra vez todos los elementos al quitar los filtros

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

                            APacienteSeleccionado(recyclerHistorial); // Se manda llamar el método que redirige al activity PerfilHerido
                        }
                    });

                    recyclerHistorial.setAdapter(adapter); // Actualiza los datos del recyclerView


                } catch (JSONException e) { // En caso de error se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
                    e.printStackTrace();
                    Toast.makeText(HistorialRegistros.this, "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    //progress.hide();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() { // En caso de error en la petición, se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistorialRegistros.this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
                //progress.hide();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });


        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest); // Se manda la petición
    }

    public void APacienteSeleccionado(View view){
        lista = "Historial";
        Paciente = new Intent(this,PerfilHerido.class);
        Paciente.putExtra("NoPaciente",stringNoPaciente); // Nombre del paciente del que se va a hacer la consultar al iniciar la actividad PerfilHerido
        Paciente.putExtra("nombre", nombre); // nombre del usuario
        Paciente.putExtra("lista", lista); // la lista desde la cual presionamos el elemento de la lista
        startActivity(Paciente);
        finish();
    }

    // Muestra todos los registros obtenidos de la base de datos
    public void QuitarFiltro(){
        adapter.Filtrar(listaHeridosBackups); // se manda la lista con todos los registros obtenidos de la base de datos al método filtrar del HistorialAdapter
        filtros.clear();
        filtroColor = false;
        cadenaFiltros = "";
    }

    // Método para filtrar los registros dependiendo la cadena que se le envíe como parámetro
    public void Filtrar(String texto){
        FiltrarLista = new ArrayList<>();

        for(herido Herido : historial) {
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
    } // Elimina los filtros

    // Actualiza la activity, haciendo que se vuelva a ejecutar el método onCreate para cargar nuevos elementos de la base de datos en caso de que se hayan registrado
    public void RefrescarListaHistorial(View view){
        Paciente = new Intent(this, HistorialRegistros.class);
        Paciente.putExtra("NoPaciente",stringNoPaciente);
        Paciente.putExtra("nombre", nombre);
        startActivity(Paciente
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(0,0);
        startActivity(Paciente);
        finish();
    }

}