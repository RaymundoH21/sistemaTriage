package com.example.sistematriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Mapa extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.mapa);

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
                        return true;

                    case R.id.historial:
                        startActivity(new Intent(getApplicationContext(),HistorialRegistros.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }
}