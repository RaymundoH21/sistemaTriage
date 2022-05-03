package com.example.sistematriage;

import static com.google.android.material.bottomnavigation.BottomNavigationView.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Principal extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.registrarpaciente);

        bottomNavigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
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

                    case R.id.registroparamedico:
                        startActivity(new Intent(getApplicationContext(),registroparamedico.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });


    }



}