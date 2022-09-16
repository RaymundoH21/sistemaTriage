package com.example.sistematriage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistematriage.dialogos.DialogoModificacionesFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* Esta clase pertenece al perfil del paciente, donde se muestra y se puede editar
   su información y sus estados */

public class PerfilHerido extends AppCompatActivity {

    public static String NoPaciente;
    ImageView imagen, ivEFoto;

    RoundedBitmapDrawable roundedBitmapDrawable;

    String path;
    public static Bitmap bitmap;

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    // Ruta donde se guardarán las fotos tomadas
    private String CARPETA_RAIZ="misImagenesPrueba/";
    private String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    herido miUsuario;

    public FloatingActionButton fabEdit;
    public FloatingActionButton fabSave;
    public FloatingActionButton fabExit;
    public FloatingActionButton fabEliminar;

    public static Boolean cambio, cambioFoto;

    public static String nombre;

    public static String lista;
    Intent regr;
    String lista2;

    String timeStamp;
    String imageFileName;
    File storageDir;
    File image;
    String url;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;

    // variables públicas para que puedan ser accedidas en cualquiera de los dos fragments añadidos a esta activity
    public static String nombreHerido, edad, sexo, lesiones;
    public static Boolean modoEditar;
    public static Fragment fragment;
    public static Boolean cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_herido);

        // Establece el color de la barra de estado en color blanco
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }

        tabLayout = findViewById(R.id.tab_layout); // Menú para cambiar entre los dos fragments
        viewPager2 = findViewById(R.id.view_pager); // Elemento donde se colocan los fragments y permite intercambiar entre ellos
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        cambio = false;
        cambioFoto = false;
        modoEditar = false;
        cancel = false;

        // Listeners para el menú de los fragments
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        // Establece una imagen por default
        imagen = (ImageView) findViewById(R.id.ImgVFoto);
        ivEFoto = (ImageView) findViewById(R.id.ivEFoto);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_photo_camera_24);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imagen.setImageDrawable(roundedBitmapDrawable);
        //webService();

        RecibirDatos();
    }

    // Destructor de la clase
    @Override
    protected void onDestroy() {
        super.onDestroy();

        imagen.setImageDrawable(null);
        ivEFoto.setImageDrawable(null);
        NoPaciente = null;
        imagen = null;
        ivEFoto = null;
        roundedBitmapDrawable = null;
        path = null;
        bitmap = null;
        miUsuario = null;
        fabEdit = null;
        fabSave = null;
        fabExit = null;
        fabEliminar = null;
        cambio = null;
        cambioFoto = null;
        nombre = null;
        CARPETA_RAIZ = null;
        RUTA_IMAGEN = null;
        regr = null;
        lista2 = null;
        timeStamp = null;
        imageFileName = null;
        storageDir = null;
        image = null;
        url = null;
        Runtime.getRuntime().gc();

    }


    /* Para tomar la fotografía se declara un Intent con el cual se manda llamar la
       aplicación de fotografía del dispositivo, una vez que se toma la fotografía, se crea el
       archivo, se almacena en el dispositivo y se devuelve la fotografía a la aplicación
       Sistema Triage */

    public void tomarFotografia(View view) {

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


            // Se define el tamaño de la fotografía y se redondea para despues mostrarla in un imageView
            Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap = adjustedBitmap;
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imagen.setImageDrawable(roundedBitmapDrawable);
            cambio = true;
            cambioFoto = true;
            //IV.setImageBitmap(bitmap);

            bitmap = redimensionarImagen(bitmap, 600, 800);
        }

    }

    // Obtiene los datos provenientes del intent de la activity anterior
    private void RecibirDatos()
    {
        Bundle extras = getIntent().getExtras();
        NoPaciente = extras.getString("NoPaciente");
        nombre = extras.getString("nombre");
        lista = extras.getString("lista");
    }


    private static int exifToDegrees(int exifOrientation) { if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) { return 180; } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) { return 270; } return 0; }



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

    // Envía a la pantalla de la lista de la cual se proviene
    public void Volver (View view){
        lista2 = "Historial";
        if (lista.equals(lista2)){
            regr = new Intent(PerfilHerido.this, HistorialRegistros.class);
        } else {
            regr = new Intent(PerfilHerido.this, ListaHeridos.class);
        }
        regr.putExtra("nombre", nombre);
        startActivity(regr);
        finish();
    }

    // Abre el fragment fragment_dialogo_modificciones.xml que muestra el recyclerView con todas las modificaciones de ese paciente
    public void MostrarModificaciones(View view){
        DialogoModificacionesFragment dialogoModificaciones = new DialogoModificacionesFragment(this, NoPaciente);
        dialogoModificaciones.show(getSupportFragmentManager(),"DialogoModificaciones");
    }
}