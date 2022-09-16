package com.example.sistematriage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

/* Esta clase se utiliza para mostrar la información del historial de cambios en el recyclerView
   que aparece al presionar el botón de la parte superior derecha en la pantalla de perfil herido*/

public class ModificacionAdapter extends RecyclerView.Adapter<ModificacionAdapter.ModificacionViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    List<herido> listaHeridos;

    int t = 0, r = 0, a = 0, v = 0, n = 0;

    // Constructor de la clase que recibe la lista de tipo herido con la información obtenida de la BD
    public ModificacionAdapter(List<herido> listaPersonas) {
        this.listaHeridos = listaPersonas;
    }

    // se hace la referencia al archivo modificacion_card.xml en el cual se define la vista de cada elemento del recyclerView
    @Override
    public ModificacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.modificacion_card,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new ModificacionViewHolder(vista);
    }

    // Obtiene cada uno de los valores de la lista que se recibe en el constructor de la clase y los va asignando a los elementos del archivo modificacion_card.xml
    @Override
    public void onBindViewHolder(ModificacionViewHolder holder, final int position) {

        holder.txtNoPaciente.setText(listaHeridos.get(position).getNoPaciente().toString());
        holder.txtColor.setText("Color: " + String.valueOf(listaHeridos.get(position).getColor().toString()));
        holder.txtEstado.setText("Estado: " + String.valueOf(listaHeridos.get(position).getEstado().toString()));
        holder.txtUsuario.setText("Usuario: " + String.valueOf(listaHeridos.get(position).getUsuario().toString()));
        holder.txtFecha.setText(String.valueOf(listaHeridos.get(position).getFecha().toString()));

        switch (listaHeridos.get(position).getColor()){
            case "Rojo":
                holder.color.setBackgroundColor(Color.parseColor("#A51717"));
                r++;
                break;
            case "Amarillo":
                holder.color.setBackgroundColor(Color.parseColor("#FFEB3B"));
                a++;
                break;
            case "Verde":
                holder.color.setBackgroundColor(Color.parseColor("#287A2C"));
                v++;
                break;
            case "Negro":
                holder.color.setBackgroundColor(Color.parseColor("#000000"));
                n++;
                break;
            default:
                holder.color.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }

        t = r + a + v + n;



    }

    @Override
    public int getItemCount() {
        return listaHeridos.size();
    }

    public void setOnClickListener(View.OnClickListener Listener)
    {
        this.listener = Listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
        {
            listener.onClick(v);
        }
    }


    // Se declaran los objetos y se hacen las referencias a cada uno de los elementos del archivo modificacion_card.xml
    public class ModificacionViewHolder extends RecyclerView.ViewHolder{

        CardView personCardView;
        TextView txtNoPaciente, txtColor, txtEstado, txtUsuario, txtFecha;
        ImageView color;

        public ModificacionViewHolder(View itemView) {
            super(itemView);
            personCardView = (CardView) itemView.findViewById(R.id.modificacion_card);
            txtNoPaciente= (TextView) itemView.findViewById(R.id.txtNoPaciente);
            txtColor= (TextView) itemView.findViewById(R.id.txtColor);
            txtEstado= (TextView) itemView.findViewById(R.id.txtEstado);
            txtUsuario = (TextView) itemView.findViewById(R.id.txtUsuario);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
            color = (ImageView)itemView.findViewById(R.id.ivBarraColor);


        }

    }

    public void Filtrar(ArrayList<herido> filtroPersonas){
        this.listaHeridos = filtroPersonas;
        notifyDataSetChanged();
    }

}
