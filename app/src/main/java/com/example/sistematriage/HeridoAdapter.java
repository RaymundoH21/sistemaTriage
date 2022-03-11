package com.example.sistematriage;

import android.content.res.Resources;
import android.graphics.Bitmap;
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

public class HeridoAdapter extends RecyclerView.Adapter<HeridoAdapter.HeridoViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    List<herido> listaHeridos;

    public HeridoAdapter(List<herido> listaPersonas) {
        this.listaHeridos = listaPersonas;
    }

    @Override
    public HeridoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.herido_card,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new HeridoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(HeridoViewHolder holder, final int position) {
        //holder.imagen.setImageBitmap(listaPersonas.get(position).getImagen());
        if(listaHeridos.get(position).getImagen()!=null) {

            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), listaHeridos.get(position).getImagen());
            roundedBitmapDrawable.setCircular(true);
            holder.imagen.setImageDrawable(roundedBitmapDrawable);
            //holder.imagen.setImageBitmap(listaPersonas.get(position).getImagen());
        }
        else{
            holder.imagen.setImageResource(R.drawable.ic_launcher_foreground);
        }
        //holder.txtNoPaciente.setText(listaHeridos.get(position).getNoPaciente().toString());
        holder.txtNombre.setText(String.valueOf(listaHeridos.get(position).getNombre().toString()) + " " + String.valueOf(listaHeridos.get(position).getApPaterno().toString()) + " " + String.valueOf(listaHeridos.get(position).getApMaterno().toString()));
        //holder.txtApPaterno.setText("Apellido Paterno: " + String.valueOf(listaPersonas.get(position).getApPaterno().toString()));
        //holder.txtApMaterno.setText("Apellido Materno: " + String.valueOf(listaPersonas.get(position).getApMaterno().toString()));
        holder.txtEdad.setText("Edad: " + String.valueOf(listaHeridos.get(position).getEdad().toString()));
        holder.txtTipoSangre.setText("Tipo de Sangre: " + String.valueOf(listaHeridos.get(position).getTipoSangre().toString()));
        holder.txtGravedad.setText("Estado de salud: " + String.valueOf(listaHeridos.get(position).getGravedad().toString()));


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



    public class HeridoViewHolder extends RecyclerView.ViewHolder{

        CardView personCardView;
        TextView txtNoPaciente, txtNombre, txtApPaterno, txtApMaterno, txtTipoSangre, txtGravedad, txtEdad, txtGenero, txtPeso, txtAltura, txtFechaNacimiento, txtLugarNacimiento, txtFechaMod, txtSeguridadSocial, txtCENATRA;;
        ImageView imagen;

        public HeridoViewHolder(View itemView) {
            super(itemView);
            personCardView = (CardView) itemView.findViewById(R.id.herido_card);
            imagen= (ImageView)itemView.findViewById(R.id.imagen);
            txtNoPaciente= (TextView) itemView.findViewById(R.id.txtNoPaciente);
            txtNombre= (TextView) itemView.findViewById(R.id.txtNombre);
            ///txtApPaterno= (TextView) itemView.findViewById(R.id.txtApPaterno);
            ///txtApMaterno= (TextView) itemView.findViewById(R.id.txtApMaterno);
            txtEdad= (TextView) itemView.findViewById(R.id.txtEdad);
            txtTipoSangre= (TextView) itemView.findViewById(R.id.txtTipoSangre);
            txtGravedad= (TextView) itemView.findViewById(R.id.txtGravedad);

        }

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        //bm.recycle();
        return resizedBitmap;
    }
}
