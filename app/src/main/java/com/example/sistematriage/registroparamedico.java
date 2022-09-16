package com.example.sistematriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/* Esta clase no se utiliza */

public class registroparamedico<onActivityResult> extends AppCompatActivity {

    ImageView picture;
    ImageButton opencamara;
    ImageButton album;
    EditText etNumeroEmpleado, etNombre, etAPaterno, etAMaterno, etEdad, etContra, etConfirmar;
    Button btnRegistrar;
    Bitmap bitmap;

    //String UPLOAD_URL ="http://192.168.1.12/sistematriage/registro.php";
    String UPLOAD_URL ="http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/registro.php";

    BottomNavigationView bottomNavigationView;

    private static final int REQUEST_PERMISSION_CAMERA = 101;
    private static final int REQUEST_IMAGE_CAMERA = 101;
    private static final int LEN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroparamedico);
        picture = findViewById(R.id.picture);
        opencamara = findViewById(R.id.opencamara);
        album = findViewById(R.id.album);
        etNumeroEmpleado = findViewById(R.id.etNumeroEmpleado);
        etNombre = findViewById(R.id.etNombre);
        etAPaterno = findViewById(R.id.etAPaterno);
        etAMaterno = findViewById(R.id.etAMaterno);
        etEdad = findViewById(R.id.etEdad);
        etContra = findViewById(R.id.etContra);
        etConfirmar = findViewById(R.id.etConfirmar);
        btnRegistrar = findViewById(R.id.btnRegistrar);



        opencamara.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(registroparamedico.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        goToCamara();
                    }else{
                        ActivityCompat.requestPermissions(registroparamedico.this, new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA);
                    }

                    }else{
                    goToCamara();
                }

            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),10);

            }
        });
        btnRegistrar.setOnClickListener((v) -> {confirmarLogeo();});
    }
    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte [] imagenByte=array.toByteArray();
        String encodedImage = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return encodedImage;

    }

    private void limpiarCampos() {
        etNumeroEmpleado.setText("");
        etNombre.setText("");
        etAPaterno.setText("");
        etAMaterno.setText("");
        etEdad.setText("");
        etContra.setText("");
        etConfirmar.setText("");
        picture.setImageResource(R.drawable.ic_baseline_person_24);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CAMERA){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                goToCamara();
            }else{
                Toast.makeText(this, "You need to enable permissions", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAMERA){
            if(resultCode == Activity.RESULT_OK){
                bitmap = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(bitmap);
                Log.i("TAG","Result =>" + bitmap);
            }
        }
        else if(resultCode == RESULT_OK){
            Uri path = data.getData();
            picture.setImageURI(path);

            try {
                bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),path);
                picture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void goToCamara(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        }
    }
    public  void upLoadImagen(){
        final ProgressDialog loading = ProgressDialog.show(this,"Subiendo","Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(registroparamedico.this,response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(registroparamedico.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                int numero = Integer.parseInt(etNumeroEmpleado.getText().toString());
                String nombre = etNombre.getText().toString();
                String apellidop = etAPaterno.getText().toString();
                String apellidom = etAMaterno.getText().toString();
                int edad = Integer.parseInt(etEdad.getText().toString());
                String contra = etContra.getText().toString();
                String confirmar = etConfirmar.getText().toString();
                String imagen = convertirImgString(bitmap);


                    Map<String, String> params = new Hashtable<>();
                    params.put("numero", numero + "");
                    params.put("nombre", nombre);
                    params.put("apellidop", apellidop);
                    params.put("apellidom", apellidom);
                    params.put("edad", edad + "");
                    params.put("contra", contra);
                    params.put("confirmar", confirmar);
                    params.put("imagen", imagen);
                    return params;
                }
        };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        Intent intent = new Intent(registroparamedico.this,MainActivity.class);
        registroparamedico.this.startActivity(intent);
        limpiarCampos();
    }
    public  void ValidarCampos(){
        final String nombre = etNombre.getText().toString();
        final String apellidop = etAPaterno.getText().toString();
        final String apellidom = etAMaterno.getText().toString();
        final String contra = etContra.getText().toString();
        final String confirmar = etConfirmar.getText().toString();
        final String imagen = convertirImgString(bitmap);
        final String numero = etNumeroEmpleado.getText().toString();
        final String edad = etEdad.getText().toString();

        if(!contra.equals(confirmar)){
            Toast.makeText(this,"Una de las Contraseñas es Incorrecta",Toast.LENGTH_SHORT).show();
        }else if(nombre.isEmpty() || apellidop.isEmpty() || apellidom.isEmpty() || contra.isEmpty() || confirmar.isEmpty() || imagen.isEmpty() || numero.isEmpty() || edad.isEmpty()){
            Toast.makeText(this,"Llenar campos vacios",Toast.LENGTH_SHORT).show();
        } else{
            upLoadImagen();
        }
    }
    private void confirmarLogeo(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(registroparamedico.this);
        builder.setMessage("¿Desea Registrar los Datos?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ValidarCampos();
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}