package com.example.sistematriage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
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

    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    herido miUsuario;

    public FloatingActionButton fabEdit;
    public FloatingActionButton fabSave;
    public FloatingActionButton fabExit;

    Boolean cambio = false, cambioFoto = false;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_herido);

        imagen = (ImageView) findViewById(R.id.ImgVFoto);
        ivEFoto = (ImageView) findViewById(R.id.ivEFoto);
        ivEColor = (ImageView) findViewById(R.id.ivEColor);
        ivEEstado = (ImageView) findViewById(R.id.ivEEstado);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_photo_camera_24);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imagen.setImageDrawable(roundedBitmapDrawable);

        RecibirDatos();
        request = Volley.newRequestQueue(getBaseContext());
        webService();

        fabEdit = findViewById(R.id.fabEditar);
        fabSave = findViewById(R.id.fabGuardar);
        fabExit = findViewById(R.id.fabSalEditar);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Snackbar.make(view, "Modo Edición", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();*/
                //mostrarDialogOpciones();
                ivEFoto.setVisibility(View.VISIBLE);
                ivEColor.setVisibility(View.VISIBLE);
                ivEEstado.setVisibility(View.VISIBLE);
                fabEdit.setVisibility(View.INVISIBLE);
                fabExit.setVisibility(View.VISIBLE);
                fabSave.setVisibility(View.VISIBLE);
            }
        });

        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Modo Vista", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();*/
                //mostrarDialogOpciones();
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
                }
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaGuardar();
            }
        });

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
        JSONArray json = response.optJSONArray("paciente2");
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
        tvFecha = (TextView) findViewById(R.id.tvFecha);
        imagen = (ImageView) findViewById(R.id.ImgVFoto);

        String url = "http://192.168.0.17/bd/ConsultarPacientes.php?NoPaciente="+NoPaciente;
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

    public void editarColor(View view) {

        final CharSequence[] opciones={"Negro","Rojo","Amarillo","Verde","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(PerfilHerido.this);
        alertOpciones.setTitle("Selecciona el color");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Negro")){
                    tvColor.setText("Negro");
                    cambio = true;
                }
                else if (opciones[i].equals("Rojo")) {
                    tvColor.setText("Rojo");
                    cambio = true;
                }
                else if (opciones[i].equals("Amarillo")){
                    tvColor.setText("Amarillo");
                    cambio = true;
                }
                else if (opciones[i].equals("Verde")) {
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

        final CharSequence[] opciones={"En espera","Trasladando","Hospital","SEMEFO","Alta","Cancelar"};
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
                    tvEstado.setText("Hospital");
                    cambio = true;
                }
                else if (opciones[i].equals("SEMEFO")) {
                    tvEstado.setText("SEMEFO");
                    cambio = true;
                }
                else if (opciones[i].equals("Alta médica")) {
                    tvEstado.setText("Alta médica");
                    cambio = true;
                }
                else {
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();

    }

    public void tomarFotografia(View view) {
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

        ////
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){

            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath=data.getData();
                    imagen.setImageURI(miPath);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),miPath);
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        roundedBitmapDrawable.setCircular(true);
                        imagen.setImageDrawable(roundedBitmapDrawable);
                        //IV.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case COD_FOTO:

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

                    break;
            }

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

    private void PreguntaGuardar() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(PerfilHerido.this);
        dialogo.setTitle("¿Deseas guardar los cambios?");
        dialogo.setMessage("Los cambios se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
        //pDialog = new ProgressDialog(this);
       // pDialog.setMessage("Cargando...");
        //pDialog.show();

        String url;


        if (cambioFoto) {

            url = "http://192.168.0.17/bd/ActualizarHerido.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();


                    Toast.makeText(PerfilHerido.this, "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
                    Intent APerfil = new Intent(PerfilHerido.this,PerfilHerido.class);
                    APerfil.putExtra("NoPaciente",NoPaciente);
                    startActivity(APerfil);
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PerfilHerido.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    pDialog.hide();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String Pac = NoPaciente;
                    String Color = tvColor.getText().toString();
                    String Estado = tvEstado.getText().toString();
                    String Fecha = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                    String imagen = convertirImgString(bitmap);

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("NoPaciente", Pac);
                    parametros.put("Color", Color);
                    parametros.put("Estado", Estado);
                    parametros.put("Fecha", Fecha);
                    parametros.put("imagen", imagen);
                    return parametros;
                }
            };
            //request.add(stringRequest);
            VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);

        }else
        {
            url = "http://192.168.0.17/bd/ActualizarHeridoSinFoto.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //pDialog.hide();


                    Toast.makeText(PerfilHerido.this, "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
                    Intent APerfil = new Intent(PerfilHerido.this,PerfilHerido.class);
                    APerfil.putExtra("NoPaciente",NoPaciente);
                    startActivity(APerfil);
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PerfilHerido.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    pDialog.hide();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String Pac = NoPaciente;
                    String Color = tvColor.getText().toString();
                    String Estado = tvEstado.getText().toString();
                    String Fecha = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("NoPaciente", Pac);
                    parametros.put("Color", Color);
                    parametros.put("Estado", Estado);
                    parametros.put("Fecha", Fecha);
                    return parametros;
                }
            };
            //request.add(stringRequest);
            VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
        }

    }
}