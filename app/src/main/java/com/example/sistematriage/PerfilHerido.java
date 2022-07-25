package com.example.sistematriage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
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

public class PerfilHerido extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    String NoPaciente;
    TextView tvColor, tvUbicacion, tvEstado, tvUsuario, tvFecha;
    ImageView imagen, ivEFoto, ivEColor, ivEEstado;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    RoundedBitmapDrawable roundedBitmapDrawable;

    String path;
    Bitmap bitmap;

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private String CARPETA_RAIZ="misImagenesPrueba/";
    private String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    herido miUsuario;

    public FloatingActionButton fabEdit;
    public FloatingActionButton fabSave;
    public FloatingActionButton fabExit;
    public FloatingActionButton fabEliminar;

    Boolean cambio = false, cambioFoto = false;

    String tempColor, tempEstado;

    DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm");

    String nombre;

    LinearLayout colorPerfil;

    String destino;
    String lista;
    Intent regr;
    JSONArray json;
    String lista2;

    String timeStamp;
    String imageFileName;
    File storageDir;
    File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_herido);

        imagen = (ImageView) findViewById(R.id.ImgVFoto);
        ivEFoto = (ImageView) findViewById(R.id.ivEFoto);
        ivEColor = (ImageView) findViewById(R.id.ivEColor);
        ivEEstado = (ImageView) findViewById(R.id.ivEEstado);
        colorPerfil = (LinearLayout) findViewById(R.id.colorPerfil);
        destino = "";

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_photo_camera_24);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imagen.setImageDrawable(roundedBitmapDrawable);

        RecibirDatos();
        request = Volley.newRequestQueue(getBaseContext());
        webService();

        fabEdit = findViewById(R.id.fabEditar);
        fabSave = findViewById(R.id.fabGuardar);
        fabExit = findViewById(R.id.fabSalEditar);
        fabEliminar = findViewById(R.id.fabEliminar);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivEFoto.setVisibility(View.VISIBLE);
                ivEColor.setVisibility(View.VISIBLE);
                ivEEstado.setVisibility(View.VISIBLE);
                fabEdit.setVisibility(View.INVISIBLE);
                fabExit.setVisibility(View.VISIBLE);
                fabSave.setVisibility(View.VISIBLE);
                fabEliminar.setVisibility(View.VISIBLE);
            }
        });

        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cambio){
                    PreguntaSalirMod();
                } else {
                    ivEFoto.setVisibility(View.INVISIBLE);
                    ivEColor.setVisibility(View.INVISIBLE);
                    ivEEstado.setVisibility(View.INVISIBLE);
                    fabExit.setVisibility(View.INVISIBLE);
                    fabEdit.setVisibility(View.VISIBLE);
                    fabSave.setVisibility(View.INVISIBLE);
                    //ivEFoto.setVisibility(View.GONE);
                    fabEliminar.setVisibility(View.INVISIBLE);
                }
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaGuardar();
            }
        });

        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaEliminar();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        jsonObjectRequest.cancel();
        imagen.setImageDrawable(null);
        ivEFoto.setImageDrawable(null);
        ivEColor.setImageDrawable(null);
        ivEEstado.setImageDrawable(null);
        NoPaciente = null;
        tvColor = null;
        tvUbicacion = null;
        tvEstado = null;
        tvUsuario = null;
        tvFecha = null;
        imagen = null;
        ivEFoto = null;
        ivEColor = null;
        ivEEstado = null;
        request = null;
        jsonObjectRequest = null;
        stringRequest = null;
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
        tempColor = null;
        tempEstado = null;
        df = null;
        nombre = null;
        colorPerfil = null;
        destino = null;
        CARPETA_RAIZ = null;
        RUTA_IMAGEN = null;
        regr = null;
        json = null;
        lista2 = null;
        timeStamp = null;
        imageFileName = null;
        storageDir = null;
        image = null;
        Runtime.getRuntime().gc();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        showToast("No se puede Consultar" + error.toString());
    }
    private void showToast(String s) { Toast.makeText(this,s, Toast.LENGTH_SHORT).show();}

    @Override
    public void onResponse(JSONObject response) {
        //showToast("Consulta Exitosa");
        miUsuario = new herido();
        json = response.optJSONArray("paciente");
        JSONObject jsonObject= null;
        try {
            jsonObject=json.getJSONObject(0);
            miUsuario.setColor(jsonObject.optString("Color"));
            miUsuario.setUbicacion(jsonObject.optString("Ubicacion"));
            miUsuario.setEstado(jsonObject.optString("Estado"));
            miUsuario.setUsuario(jsonObject.optString("Usuario"));
            miUsuario.setFecha(jsonObject.optString("Fecha"));
            miUsuario.setDato(jsonObject.optString("imagen"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvColor.setText(miUsuario.getColor());
        tvUbicacion.setText(miUsuario.getUbicacion());
        tvEstado.setText(miUsuario.getEstado());
        tvUsuario.setText(miUsuario.getUsuario());
        tvFecha.setText(miUsuario.getFecha());

        tempColor = tvColor.getText().toString();
        tempEstado = tvEstado.getText().toString();


        if(miUsuario.getImagen()!=null) {
            roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), miUsuario.getImagen());
            roundedBitmapDrawable.setCircular(true);
            imagen.setImageDrawable(roundedBitmapDrawable);
        }
        else{
            imagen.setImageResource(R.drawable.ic_baseline_photo_camera_24);
        }

        switch (miUsuario.getColor()){
            case "Rojo":
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A51717")));
                break;
            case "Amarillo":
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                break;
            case "Verde":
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#287A2C")));
                break;
            case "Negro":
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                break;
            default:
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                break;
        }

    }

    private void webService(){

        tvColor = (TextView) findViewById(R.id.txtColor);
        tvUbicacion = (TextView) findViewById(R.id.txtUbicacion);
        tvEstado = (TextView) findViewById(R.id.txtEstado);
        tvUsuario = (TextView) findViewById(R.id.txtUsuario);
        tvFecha = (TextView) findViewById(R.id.tvFecha);
        imagen = (ImageView) findViewById(R.id.ImgVFoto);

        //String url = "http://192.168.1.12/sistematriage/ConsultarPaciente.php?NoPaciente="+NoPaciente;
        String url = "http://ec2-54-219-50-144.us-west-1.compute.amazonaws.com/ConsultarPaciente.php?NoPaciente="+NoPaciente;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

    }

    private void RecibirDatos()
    {
        Bundle extras = getIntent().getExtras();
        NoPaciente = extras.getString("NoPaciente");
        nombre = extras.getString("nombre");
        lista = extras.getString("lista");
    }

    public void Volver (View view){
        lista2 = "Historial";
        if (lista.equals(lista2)){
            regr = new Intent(PerfilHerido.this,HistorialRegistros.class);
        } else {
            regr = new Intent(PerfilHerido.this,ListaHeridos.class);
        }
        regr.putExtra("nombre", nombre);
        startActivity(regr);
        finish();
    }

    public void editarColor(View view) {

        final CharSequence[] opciones={"Negro","Rojo","Amarillo","Verde","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(PerfilHerido.this);
        alertOpciones.setTitle("Selecciona el color");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Negro")){
                    colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                    tvColor.setText("Negro");
                    cambio = true;
                }
                else if (opciones[i].equals("Rojo")) {
                    colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A51717")));
                    tvColor.setText("Rojo");
                    cambio = true;
                }
                else if (opciones[i].equals("Amarillo")){
                    colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                    tvColor.setText("Amarillo");
                    cambio = true;
                }
                else if (opciones[i].equals("Verde")) {
                    colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#287A2C")));
                    tvColor.setText("Verde");
                    cambio = true;
                }
                else {
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();

    }

    public void editarEstado(View view) {

        final CharSequence[] opciones={"En espera","Trasladando","Hospital","SEMEFO","Alta médica","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(PerfilHerido.this);
        alertOpciones.setTitle("Selecciona el estado");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("En espera")){
                    tvEstado.setText("En espera");
                    cambio = true;
                }
                else if (opciones[i].equals("Trasladando")) {
                    tvEstado.setText("Trasladando");
                    cambio = true;
                }
                else if (opciones[i].equals("Hospital")){

                    final CharSequence[] opciones={"Cruz Roja Mexicana Delegación Tijuana","Cruz Roja Mexicana Estatal B.C.","Hospital Angeles Tijuana","Hospital General Ensenada","Hospital General Mexicali","Hospital General Rosarito","Hospital General Tecate","Hospital General Tijuana","IMSS 6 Tecate","ISSSTE Hospital Gral. Fray Junipero Serra","Otro","Cancelar"};
                    final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(PerfilHerido.this);
                    alertOpciones.setTitle("Hospital de destino");
                    alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (opciones[i].equals("Cruz Roja Mexicana Delegación Tijuana")){
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "Cruz Roja Mexicana Delegación Tijuana";
                            }
                            else if (opciones[i].equals("Cruz Roja Mexicana Estatal B.C.")) {
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "Cruz Roja Mexicana Estatal Baja California";
                            }
                            else if (opciones[i].equals("Hospital Angeles Tijuana")){
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "Hospital Angeles Tijuana";
                            }
                            else if (opciones[i].equals("Hospital General Ensenada")) {
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "Hospital General Ensenada";
                            }
                            else if (opciones[i].equals("Hospital General Mexicali")) {
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "Hospital General Mexicali";
                            }
                            else if (opciones[i].equals("Hospital General Rosarito")) {
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "Hospital General Rosarito";
                            }
                            else if (opciones[i].equals("Hospital General Tecate")) {
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "Hospital General Tecate";
                            }
                            else if (opciones[i].equals("Hospital General Tijuana")) {
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "Hospital General Tijuana";
                            }
                            else if (opciones[i].equals("IMSS 6 Tecate")) {
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "IMSS 6 Tecate";
                            }
                            else if (opciones[i].equals("ISSSTE Hospital Gral. Fray Junipero Serra")) {
                                tvEstado.setText("Hospital");
                                cambio = true;
                                destino = "ISSSTE Hospital General Fray Junipero Serra";
                            }
                            else if (opciones[i].equals("Otro")) {
                                OtroDestinoDialog();
                            }
                            else {
                                dialogInterface.dismiss();
                            }
                        }
                    });
                    alertOpciones.show();

                }
                else if (opciones[i].equals("SEMEFO")) {
                    tvEstado.setText("SEMEFO");
                    cambio = true;
                    destino = "SEMEFO";
                }
                else if (opciones[i].equals("Alta médica")) {
                    tvEstado.setText("Alta médica");
                    cambio = true;
                    destino = "Alta médica";
                }
                else {
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();

    }

    public void tomarFotografia(View view) {
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

    private static int exifToDegrees(int exifOrientation) { if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) { return 180; } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) { return 270; } return 0; }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

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

    private void restablecerValores()
    {
        tvColor.setText(miUsuario.getColor());
        tvUbicacion.setText(miUsuario.getUbicacion());
        tvEstado.setText(miUsuario.getEstado());
        tvUsuario.setText(miUsuario.getUsuario());
        tvFecha.setText(miUsuario.getFecha());
        cambio = false;
        cambioFoto = false;


        if(miUsuario.getImagen()!=null) {
            roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), miUsuario.getImagen());
            roundedBitmapDrawable.setCircular(true);
            imagen.setImageDrawable(roundedBitmapDrawable);
        }
        else{
            imagen.setImageResource(R.drawable.ic_baseline_photo_camera_24);
        }
    }

    private void PreguntaSalirMod() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(PerfilHerido.this);
        dialogo.setTitle("¿Deseas dejar de editar?");
        dialogo.setMessage("Los cambios no se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ivEFoto.setVisibility(View.INVISIBLE);
                ivEColor.setVisibility(View.INVISIBLE);
                ivEEstado.setVisibility(View.INVISIBLE);
                fabExit.setVisibility(View.INVISIBLE);
                fabEdit.setVisibility(View.VISIBLE);
                fabSave.setVisibility(View.INVISIBLE);
                fabEliminar.setVisibility(View.INVISIBLE);
                restablecerValores();
            }
        });
        dialogo.setNegativeButton(Html.fromHtml("<font color='#696B6A'>Cancelar</font>"), new DialogInterface.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();
    }

    private void webServiceEliminar() {

        //String url="http://192.168.1.12/sistematriage/EliminarHerido.php?NoPaciente="+NoPaciente;
        String url="http://ec2-54-219-50-144.us-west-1.compute.amazonaws.com/EliminarHerido.php?NoPaciente="+NoPaciente;

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PerfilHerido.this,"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                Intent ALista = new Intent(PerfilHerido.this,ListaHeridos.class);
                ALista.putExtra("NoPaciente",NoPaciente);
                ALista.putExtra("nombre", nombre);
                startActivity(ALista);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PerfilHerido.this,"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
    }

    public void PreguntaEliminar(){
        AlertDialog.Builder dialogo=new AlertDialog.Builder(PerfilHerido.this);
        dialogo.setTitle("¿Desea eliminar este registro?");
        dialogo.setMessage("La información se borrará");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                webServiceEliminar();
            }
        });
        dialogo.setNegativeButton(Html.fromHtml("<font color='#696B6A'>Cancelar</font>"), new DialogInterface.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();
    }

    private void PreguntaGuardar() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(PerfilHerido.this);
        dialogo.setTitle("¿Deseas guardar los cambios?");
        dialogo.setMessage("Todas las modificaciones se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                webServiceAddModificacion();
                webServiceActualizar();
            }
        });
        dialogo.setNegativeButton(Html.fromHtml("<font color='#696B6A'>Cancelar</font>"), new DialogInterface.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();

    }

    private void webServiceActualizar() {

        String url;


        if (cambioFoto) {

            //url = "http://192.168.1.12/sistematriage/ActualizarHerido.php?";
            url = "http://ec2-54-219-50-144.us-west-1.compute.amazonaws.com/ActualizarHerido2.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(PerfilHerido.this, "Se ha Actualizado con éxito", Toast.LENGTH_SHORT).show();
                    Intent APerfil = new Intent(PerfilHerido.this,PerfilHerido.class);
                    APerfil.putExtra("NoPaciente",NoPaciente);
                    APerfil.putExtra("nombre", nombre);
                    APerfil.putExtra("lista", lista);
                    startActivity(APerfil);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PerfilHerido.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String Pac = NoPaciente;
                    String Color = tvColor.getText().toString();
                    String Usuario = nombre;
                    String Estado = tvEstado.getText().toString();
                    String Fecha = df.format(Calendar.getInstance().getTime());
                    String imagen = convertirImgString(bitmap);
                    String dest = destino;

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("NoPaciente", Pac);
                    parametros.put("Color", Color);
                    parametros.put("Usuario", Usuario);
                    parametros.put("Estado", Estado);
                    parametros.put("Fecha", Fecha);
                    parametros.put("imagen", imagen);
                    parametros.put("Destino", dest);
                    return parametros;
                }
            };
            request.add(stringRequest);
            //VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);

        }else
        {
            //url = "http://192.168.1.12/sistemaTriage/ActualizarHeridoSinFoto.php?";
            url = "http://ec2-54-219-50-144.us-west-1.compute.amazonaws.com/ActualizarHeridoSinFoto.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Toast.makeText(PerfilHerido.this, "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
                    Intent APerfil = new Intent(PerfilHerido.this,PerfilHerido.class);
                    APerfil.putExtra("NoPaciente",NoPaciente);
                    APerfil.putExtra("nombre", nombre);
                    APerfil.putExtra("lista", lista);
                    startActivity(APerfil);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PerfilHerido.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String Pac = NoPaciente;
                    String Color = tvColor.getText().toString();
                    String Usuario = nombre;
                    String Estado = tvEstado.getText().toString();
                    String Fecha = df.format(Calendar.getInstance().getTime());
                    String dest = destino;

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("NoPaciente", Pac);
                    parametros.put("Color", Color);
                    parametros.put("Usuario", Usuario);
                    parametros.put("Estado", Estado);
                    parametros.put("Fecha", Fecha);
                    parametros.put("Destino", dest);
                    return parametros;
                }
            };
            request.add(stringRequest);
            //VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
        }

    }

    private void webServiceAddModificacion() {

        String url;

            //url = "http://192.168.1.12/sistematriage/HistorialModificacion.php?";
            url = "http://ec2-54-219-50-144.us-west-1.compute.amazonaws.com/HistorialModificacion.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Toast.makeText(PerfilHerido.this, "Se ha Actualizado con éxito", Toast.LENGTH_SHORT).show();
                    //Intent APerfil = new Intent(PerfilHerido.this,PerfilHerido.class);
                    //APerfil.putExtra("NoPaciente",NoPaciente);
                    //startActivity(APerfil);
                    //finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PerfilHerido.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String Pac = NoPaciente;
                    String Usuario = nombre;
                    String Color = tempColor;
                    String Estado = tempEstado;
                    String Fecha = tvFecha.getText().toString();

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("NoPaciente", Pac);
                    parametros.put("Usuario", Usuario);
                    parametros.put("Color", Color);
                    parametros.put("Estado", Estado);
                    parametros.put("Fecha", Fecha);
                    return parametros;
                }
            };
            //request.add(stringRequest);
            VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
    }

    public void MostrarModificaciones(View view){
        DialogoModificacionesFragment dialogoModificaciones = new DialogoModificacionesFragment(this, NoPaciente);
        dialogoModificaciones.show(getSupportFragmentManager(),"DialogoModificaciones");
    }

    private void OtroDestinoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hospital de destino");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Escribe el nombre del hospital");
        builder.setView(input);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvEstado.setText("Hospital");
                cambio = true;
                destino = input.getText().toString().trim();
                Toast.makeText(PerfilHerido.this, "Destino: " + destino, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PerfilHerido.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

}