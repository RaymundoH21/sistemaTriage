package com.example.sistematriage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/* Esta clase pertenece a la primer pantalla, donde aparece el logo y la opción de ir a la pantalla de inicio de sesión*/

public class MenuPrincipal extends AppCompatActivity {

    Intent Inicio;
    AlertDialog.Builder builder;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE); // se obtienen los datos de inicio de sesión
        editor = preferences.edit();

        // Se verifica que existan datos de inicio de sesión guardadados, si los hay, redirige a la activity de la lista de heridos
        if (revisarSesion()) {
            Intent intent = new Intent(MenuPrincipal.this, ListaHeridos.class);
            intent.putExtra("nombre", this.preferences.getString("usuario", ""));
            startActivity(intent);
            finish();
        }

        // cambia el color de la barra de estado a color blanco
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }
    }

    // destructor de la clase
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Inicio = null;
        builder = null;
        Runtime.getRuntime().gc();
    }

    // Método para ir a inicio de sesión
    public void AInicioDeSesion(View view){
        Inicio = new Intent(this, MainActivity.class);
        startActivity(Inicio);
        finish();
    }

    // Método para cerrar la aplicación
    public void SalirApp(View view){
        builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea salir de sistema triage?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
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

    // Método que revisa que haya datos de inicio de sesión guardados
    private Boolean revisarSesion(){
        return this.preferences.getBoolean("sesion", false);
    }
}