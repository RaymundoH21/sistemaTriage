package com.example.sistematriage;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrarPaciente extends AppCompatActivity {

    TextView usuario2;
    Toolbar toolbar;

    EditText ubi, color, usuario, estado;
    ImageView btnGuardar;

    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    ImageView IV;
    String path;
    Bitmap bitmap;
    Uri file;

    RequestQueue request;
    StringRequest stringRequest;

    BottomNavigationView bottomNavigationView;

    RadioButton rbRojo, rbAmarillo, rbVerde, rbNegro;

    DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm");

    String nombre;

    String lat, lon, locat, alt;

    Double latitud, longitud, altitud;

    Double latitude, longitude, altitude;

    String timeStamp;
    String imageFileName;
    File storageDir;
    File image;

    String url;
    Bitmap adjustedBitmap;

    ByteArrayOutputStream array;
    byte[] imagenByte;
    String imagenString;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_paciente);

        preferences = getSharedPreferences("sesiones",Context.MODE_PRIVATE);
        editor = preferences.edit();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }

        lat = "";
        lon = "";
        locat = "";
        alt = "";
        latitud = 0.0;
        longitud = 0.0;
        latitude = 0.0;
        longitude = 0.0;
        altitud = 0.0;
        altitude = 0.0;
        file = null;

        getLocalizacion();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuario2 = (TextView) findViewById(R.id.NombresUsuario);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");

        usuario2.setText(nombre);

        request = Volley.newRequestQueue(this);

        IV = (ImageView) findViewById(R.id.foto);

        btnGuardar = ((ImageView)findViewById(R.id.btnGuardar));

        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onclick();


            }
        });

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.registrarpaciente);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.listaheridos:
                        //startActivity(new Intent(getApplicationContext(),ListaHeridos.class));
                        Intent intent = new Intent(RegistrarPaciente.this,ListaHeridos.class);
                        intent.putExtra("nombre",nombre);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.registrarpaciente:
                        return true;

                    case R.id.mapa:
                        //startActivity(new Intent(getApplicationContext(),Mapa.class));
                        Intent intent2 = new Intent(RegistrarPaciente.this,MapaPrincipal.class);
                        intent2.putExtra("nombre",nombre);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    case R.id.historial:
                        //startActivity(new Intent(getApplicationContext(),HistorialRegistros.class));
                        Intent intent3 = new Intent(RegistrarPaciente.this,HistorialRegistros.class);
                        intent3.putExtra("nombre",nombre);
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                }
                return false;
            }
        });

        rbNegro = (RadioButton) findViewById(R.id.rbNegro);
        rbRojo = (RadioButton) findViewById(R.id.rbRojo);
        rbAmarillo = (RadioButton) findViewById(R.id.rbAmarillo);
        rbVerde = (RadioButton) findViewById(R.id.rbVerde);


        if(validaPermisos()){
            IV.setEnabled(true);
        }else{
            IV.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //stringRequest.cancel();
        usuario2 = null;
        toolbar = null;
        ubi = null;
        color = null;
        usuario = null;
        estado = null;
        btnGuardar = null;
        IV = null;
        path = null;
        bitmap = null;
        request = null;
        stringRequest = null;
        bottomNavigationView = null;
        rbRojo = null;
        rbAmarillo = null;
        rbVerde = null;
        rbNegro = null;
        df = null;
        nombre = null;
        lat = null;
        lon = null;
        locat = null;
        latitud = null;
        longitud = null;
        latitude = null;
        longitude = null;
        timeStamp = null;
        imageFileName = null;
        storageDir = null;
        image = null;
        url = null;
        adjustedBitmap = null;
        array = null;
        imagenByte = null;
        imagenString = null;
        Runtime.getRuntime().gc();

    }

    public void onRadioButtonClicked(View view) {
        boolean marcado = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbNegro:
                if (marcado) {
                    rbRojo.setChecked(false);
                    rbAmarillo.setChecked(false);
                    rbVerde.setChecked(false);
                }
                break;

            case R.id.rbRojo:
                if (marcado) {
                    rbNegro.setChecked(false);
                    rbAmarillo.setChecked(false);
                    rbVerde.setChecked(false);
                }
                break;

            case R.id.rbAmarillo:
                if (marcado) {
                    rbRojo.setChecked(false);
                    rbNegro.setChecked(false);
                    rbVerde.setChecked(false);
                }
                break;

            case R.id.rbVerde:
                if (marcado) {
                    rbRojo.setChecked(false);
                    rbNegro.setChecked(false);
                    rbAmarillo.setChecked(false);
                }
                break;
        }
    }

    private void cargarLocalizacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationManager locationManager = (LocationManager) RegistrarPaciente.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                latitud = location.getLatitude();
                longitud = location.getLongitude();
                altitud = location.getAltitude();
                lat = Double.toString(location.getLatitude());
                lon = Double.toString(location.getLongitude());
                alt = Double.toString(location.getAltitude());
                //LatLng ubicacion = new LatLng(location.getLatitude(),location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider){

            }
        };

        int permiso = ContextCompat.checkSelfPermission(RegistrarPaciente.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //Toast.makeText(RegistrarPaciente.this, "Ubicación generada con éxito", Toast.LENGTH_LONG).show();
    }

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(RegistrarPaciente.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegistrarPaciente.this, Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(RegistrarPaciente.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
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
        editor.putBoolean("sesion", false);
        editor.apply();
        Intent intent = new Intent(this, MenuPrincipal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                IV.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RegistrarPaciente.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistrarPaciente.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    public void onclick() {
        cargarImagen();
    }

    private void cargarImagen() {
        tomarFotografia();
    }

    private void tomarFotografia() {
/*
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }


        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;


        File imagen=new File(path);
        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);

        ////*/


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.sistematriage.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){

                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });

                    ;
                    bitmap= BitmapFactory.decodeFile(path);


                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    int rotationInDegrees = exifToDegrees(rotation);

                    Matrix matrix = new Matrix(); if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}



                    adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap = adjustedBitmap;
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    roundedBitmapDrawable.setCircular(true);
                    IV.setImageDrawable(roundedBitmapDrawable);
                    //IV.setImageBitmap(bitmap);

            bitmap = redimensionarImagen(bitmap, 600, 800);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        path = image.getAbsolutePath();
        return image;
    }

    private static int exifToDegrees(int exifOrientation) { if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) { return 180; } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) { return 270; } return 0; }


    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void Guardar(View v){
        PreguntaGuardar();
    }

    private void cargarWebService(){

        //url="http://192.168.1.12/sistematriage/RegistrarPaciente.php";
        url="http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/RegistrarPaciente.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ///if (response.trim().equalsIgnoreCase("Registra")) {
                //ubi.setText("");


                IV.setImageResource(R.drawable.boton_tomar_foto);

                Toast.makeText(RegistrarPaciente.this, response, Toast.LENGTH_LONG).show();
                showToast("Se ha Registrado Exitosamente");
                Intent nuevoform = new Intent(RegistrarPaciente.this, RegistrarPaciente.class);
                nuevoform.putExtra("nombre", nombre);
                startActivity(nuevoform);
                finish();
                /*}else{
                    showToast("No se puede registrar");
                    Log.i("RESPUESTA: ",""+response);
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(RegistrarPaciente.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String Ubicacion = locat;
                String Color = obtenerColor();
                String Usuario = nombre;
                String Estado = "En espera";
                String Fecha = df.format(Calendar.getInstance().getTime());

                String imagen = convertirImgString(bitmap);
                String Latitud = lat;
                String Longitud = lon;
                String Altitud = alt;

                Map<String,String> parametros = new HashMap<>();

                parametros.put("Ubicacion",Ubicacion);
                parametros.put("Color",Color);
                parametros.put("Usuario",Usuario);
                parametros.put("Estado",Estado);
                parametros.put("Fecha", Fecha);
                parametros.put("imagen",imagen);
                parametros.put("Latitud", Latitud);
                parametros.put("Longitud", Longitud);
                parametros.put("Altitud", Altitud);

                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {

        array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        imagenByte=array.toByteArray();
        imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }



    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }

    }

    private void PreguntaGuardar() {
        cargarLocalizacion();
        /*AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistrarPaciente.this);
        dialogo.setTitle("¿Deseas guardar la información?");
        dialogo.setMessage("Los datos se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {*/
                latitude = latitud;
                longitude = longitud;
                if(latitude != 0 && longitude != 0) {
                    locat = getCurrentLocationViaJSON(latitude, longitude);
                    cargarWebService();
                }else {
                    alerta("No se han podido obtener las coordenas, vuelve a intentar");
                }
            /*}
        });
        dialogo.setNegativeButton(Html.fromHtml("<font color='#696B6A'>Cancelar</font>"), new DialogInterface.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();*/

    }

    public void AListaHeridos (View view){
        Intent regr = new Intent(RegistrarPaciente.this,ListaHeridos.class);
        startActivity(regr);
        finish();
    }

    public String obtenerColor() {

        if (rbNegro.isChecked()==true){
            return "Negro";
        }

        else if (rbRojo.isChecked()==true){
            return "Rojo";
        }

        else if (rbAmarillo.isChecked()==true){
            return "Amarillo";
        }

        else if (rbVerde.isChecked()==true){
            return "Verde";
        }

        else{
            return "";
        }
    }

    public void alerta(String cadena) {
        //se prepara la alerta creando nueva instancia
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //seleccionamos la cadena a mostrar
        dialogBuilder.setMessage(cadena);
        //elegimo un titulo y configuramos para que se pueda quitar
        dialogBuilder.setCancelable(true).setTitle("Error");
        //mostramos el dialogBuilder
        dialogBuilder.create().show();
    }

    public static JSONObject getLocationInfo(Double lat, Double lng) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

            HttpGet httpGet = new HttpGet(
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                            + lat + "," + lng + "&sensor=true&key=AIzaSyAr-EnqwpqJwx5nJbc1m3kDUE_5TJxQhqI");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }
        return null;
    }

    public static String getCurrentLocationViaJSON(Double lat, Double lng) {

        JSONObject jsonObj = getLocationInfo(lat, lng);
        Log.i("JSON string =>", jsonObj.toString());

        String currentLocation = "";

        try {
            String status = jsonObj.getString("status").toString();
            Log.i("status", status);

            if (status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                //JSONArray res = zero
                       // .getJSONArray("formatted_address");
                String dir = new String(zero.getString("formatted_address").getBytes("ISO-8859-1"), "UTF-8");
                //String dir = zero.getString("formatted_address");

                currentLocation = dir;

            }
        } catch (Exception e) {

        }
        return currentLocation;

    }

}

