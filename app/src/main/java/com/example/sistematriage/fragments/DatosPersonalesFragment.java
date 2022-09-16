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

/* Esta clase pertenece a uno de los fragments de la activity PerfilHerido,
   en donde se introducen los datos personales del paciente
 */

public class DatosPersonalesFragment extends Fragment {

    ImageView ivENombre, ivESexo, ivEEdad, ivELesiones;
    TextView tvNombre, tvSexo, tvEdad, tvLesiones;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    // Este método se manda llamar al cambiar entre cada dialog
    @Override
    public void onResume() {
        super.onResume();
        /* Se pregunta por el estado de la variable cancel, la cual
           nos dice si los cambios a la información se cancelaron */
        if (PerfilHerido.cancel == true){
            /* Si se canceló, a los textviews se les asignan
               los valores que se recibieron de la consulta a la BD */
            tvNombre.setText(PerfilHerido.nombreHerido);
            tvSexo.setText(PerfilHerido.sexo);
            tvEdad.setText(PerfilHerido.edad);
            tvLesiones.setText(PerfilHerido.lesiones);
            PerfilHerido.cancel = false;
        }
        /* Se inicializa el objeto fragment de la clase PerfilHerido
           con el constructor de este fragment, para saber cual es el
           fragment que se encuentra visible en ese momento */
        PerfilHerido.fragment = new DatosPersonalesFragment();
        /* Si el botón Editar fue presionado, al entrar a este
           fragment se hacen visibles los botones de editar*/
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

        // Se hace referencia a los elementos del archivo fragment_datos_personales.xml
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

        /* Si el botón Editar fue presionado, al entrar a este
           fragment se hacen visibles los botones de editar*/
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

        // Listeners para cada uno de los botones de editar
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

    /* Método para crear un alertDialog en el cual se incluye
       un editText para introducir el nombre del paciente */
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

    /* Método para crear un alertDialog en el cual se incluye
       una lista con los valores de Hombre y Mujer */
    public void editarSexo(){
        final CharSequence[] opciones={"Hombre","Mujer"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona el sexo");
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

    /* Método para crear un alertDialog en el cual se incluye
       una lista con todas las edades posibles */
    public void editarEdad(){
        final CharSequence[] opciones = new CharSequence[102];
        opciones[0] = "Menos de 1 año";
        opciones[1] = "1 año";
        // ciclo para añadir edades a la lista
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

    /* Método para crear un alertDialog en el cual se incluye
       un editText para introducir las lesiones del paciente */
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