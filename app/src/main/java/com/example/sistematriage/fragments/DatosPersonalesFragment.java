package com.example.sistematriage.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistematriage.PerfilHerido;
import com.example.sistematriage.R;

public class DatosPersonalesFragment extends Fragment {

    ImageView ivENombre, ivESexo, ivEEdad, ivELesiones;
    TextView tvNombre, tvSexo, tvEdad, tvLesiones;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PerfilHerido.cancel == true){
            tvNombre.setText(PerfilHerido.nombreHerido);
            tvSexo.setText(PerfilHerido.sexo);
            tvEdad.setText(PerfilHerido.edad);
            tvLesiones.setText(PerfilHerido.lesiones);
            PerfilHerido.cancel = false;
        }
        PerfilHerido.fragment = new DatosPersonalesFragment();
        if(PerfilHerido.modoEditar == true){
            ivENombre.setVisibility(View.VISIBLE);
            ivESexo.setVisibility(View.VISIBLE);
            ivEEdad.setVisibility(View.VISIBLE);
            ivELesiones.setVisibility(View.VISIBLE);
        }
        else {
            ivENombre.setVisibility(View.INVISIBLE);
            ivESexo.setVisibility(View.INVISIBLE);
            ivEEdad.setVisibility(View.INVISIBLE);
            ivELesiones.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PerfilHerido.fragment = new DatosPersonalesFragment();
        View view =  inflater.inflate(R.layout.fragment_datos_personales, container, false);

        ivENombre = (ImageView) view.findViewById(R.id.ivENombre);
        ivESexo = (ImageView) view.findViewById(R.id.ivESexo);
        ivEEdad = (ImageView) view.findViewById(R.id.ivEEdad);
        ivELesiones = (ImageView) view.findViewById(R.id.ivELesiones);
        tvNombre = (TextView) view.findViewById(R.id.txtNombreHerido);
        tvSexo = (TextView) view.findViewById(R.id.txtSexo);
        tvEdad = (TextView) view.findViewById(R.id.txtEdad);
        tvLesiones = (TextView) view.findViewById(R.id.txtLesiones);

        tvNombre.setText(PerfilHerido.nombreHerido);
        tvSexo.setText(PerfilHerido.sexo);
        tvEdad.setText(PerfilHerido.edad);
        tvLesiones.setText(PerfilHerido.lesiones);

        if(PerfilHerido.modoEditar == true){
            ivENombre.setVisibility(View.VISIBLE);
            ivESexo.setVisibility(View.VISIBLE);
            ivEEdad.setVisibility(View.VISIBLE);
            ivELesiones.setVisibility(View.VISIBLE);
        }
        else {
            ivENombre.setVisibility(View.GONE);
            ivESexo.setVisibility(View.GONE);
            ivEEdad.setVisibility(View.GONE);
            ivELesiones.setVisibility(View.GONE);
        }

        ivENombre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                editarNombre();
            }
        });

        ivESexo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                editarSexo();
            }
        });

        ivEEdad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                editarEdad();
            }
        });

        ivELesiones.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                editarLesiones();
            }
        });

        return view;
    }

    public void editarNombre(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nombre del paciente");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Introduce el nombre completo");
        builder.setView(input);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PerfilHerido.cambio = true;
                PerfilHerido.nombreHerido = input.getText().toString().trim();
                tvNombre.setText(PerfilHerido.nombreHerido);
                Toast.makeText(getActivity(), "Nombre: " + PerfilHerido.nombreHerido, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    public void editarSexo(){
        final CharSequence[] opciones={"Hombre","Mujer"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona el color");
        alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Hombre")){
                    PerfilHerido.sexo = "Hombre";
                    tvSexo.setText(PerfilHerido.sexo);
                    PerfilHerido.cambio = true;
                }
                else if (opciones[i].equals("Mujer")) {
                    PerfilHerido.sexo = "Mujer";
                    tvSexo.setText(PerfilHerido.sexo);
                    PerfilHerido.cambio = true;
                }
            }
        });
        final AlertDialog dialog = alertOpciones.create();
        dialog.show();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //create a new one
        layoutParams.weight = 1.0f;
        layoutParams.gravity = Gravity.CENTER; //this is layout_gravity
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(layoutParams);
    }

    public void editarEdad(){
        final CharSequence[] opciones = new CharSequence[102];
        opciones[0] = "Menos de 1 año";
        opciones[1] = "1 año";
        for(Integer i = 2; i <= 100; i++){
            opciones[i] = "" + i + " años";
        }
        opciones[101] = "Más de 100 años";
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona la edad");
        alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    PerfilHerido.edad = opciones[i].toString();
                    tvEdad.setText(PerfilHerido.edad);
                    PerfilHerido.cambio = true;
            }
        });
        alertOpciones.show();
    }

    public void editarLesiones(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Lesiones Principales");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Escriba las lesiones");
        builder.setView(input);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PerfilHerido.cambio = true;
                PerfilHerido.lesiones = input.getText().toString().trim();
                tvLesiones.setText(PerfilHerido.lesiones);
                //Toast.makeText(getActivity(), "Nombre: " + PerfilHerido.nombreHerido, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

}