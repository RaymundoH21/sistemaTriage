package com.example.sistematriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistorialRegistros extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_registros);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.historial);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.listaheridos:
                        startActivity(new Intent(getApplicationContext(),ListaHeridos.class));
                        overridePendingTransition(0,0);
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
                        return true;

                }
                return false;
            }
        });

    }
}