package com.example.sistematriage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    TextView Nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Nombre = findViewById(R.id.etNombre);

        Intent intent = this.getIntent();
        String nombre = intent.getStringExtra("nombre");

        Nombre.setText(Nombre.getText()+""+nombre);
    }
}