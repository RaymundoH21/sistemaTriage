package com.example.sistematriage;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.ArrayList;

public class HeridoAdapter extends RecyclerView.Adapter<HeridoAdapter.HeridoViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    List<herido> listaHeridos;
    Activity activity;

    int t = 0, r = 0, a = 0, v = 0, n = 0;

    public HeridoAdapter(List<herido> listaPersonas, Activity activity) {
        this.activity = activity;
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

            //String ip = "http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/imagenes/Rojo.jpg";

            //Picasso.get().load(ip).resize(150, 150).into(holder.imagen);

            /*Glide.with(activity).load(listaHeridos.get(position).getImagen())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imagen);*/

            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), listaHeridos.get(position).getImagen());
            roundedBitmapDrawable.setCircular(true);
            holder.imagen.setImageDrawable(roundedBitmapDrawable);

            //holder.imagen.setImageBitmap(getResizedBitmap(listaHeridos.get(position).getImagen(),200,200));
            //holder.imagen.setImageBitmap(listaHeridos.get(position).getImagen());
        }
        else{
            holder.imagen.setImageResource(R.drawable.ic_launcher_foreground);
        }
        holder.txtNoPaciente.setText(listaHeridos.get(position).getNoPaciente().toString());
        holder.txtUbicacion.setText(String.valueOf(listaHeridos.get(position).getUbicacion().toString()));

        holder.txtColor.setText("Color: " + String.valueOf(listaHeridos.get(position).getColor().toString()));
        holder.txtLat.setText("Latitud: " +String.valueOf(listaHeridos.get(position).getLatitud().toString()));
        holder.txtLng.setText("Longitud: " +String.valueOf(listaHeridos.get(position).getLongitud().toString()));
        holder.txtAlt.setText("Altitud: "+String.valueOf(listaHeridos.get(position).getAltitud().toString()));
        if (listaHeridos.get(position).getAmbulancia().equals("null") || listaHeridos.get(position).getAmbulancia().equals("")){
            holder.LinearAmbulancia.setVisibility(View.GONE);
        } else if(listaHeridos.get(position).getAmbulancia().equals("BC-159 (supervisor)")){
            holder.txtAmbulancia.setText("BC-159");
        } else if(listaHeridos.get(position).getAmbulancia().equals("BC-169 (supervisor)")){
            holder.txtAmbulancia.setText("BC-169");
        } else if(listaHeridos.get(position).getAmbulancia().equals("BC-187 (supervisor)")){
            holder.txtAmbulancia.setText("BC-187");
        } else if(listaHeridos.get(position).getAmbulancia().equals("BC-190 (rescate)")){
            holder.txtAmbulancia.setText("BC-190");
        } else if(listaHeridos.get(position).getAmbulancia().equals("BC-195 (rescate)")){
            holder.txtAmbulancia.setText("BC-195");
        } else if(listaHeridos.get(position).getAmbulancia().equals("BC-196 (upr)")){
            holder.txtAmbulancia.setText("BC-196");
        } else {
            holder.txtAmbulancia.setText(listaHeridos.get(position).getAmbulancia());
        }

        switch (listaHeridos.get(position).getColor()){
            case "Rojo":
                //holder.color.setBackgroundColor(Color.parseColor("#A51717"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A51717")));
                r++;
                break;
            case "Amarillo":
                //holder.color.setBackgroundColor(Color.parseColor("#FFEB3B"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                a++;
                break;
            case "Verde":
                //holder.color.setBackgroundColor(Color.parseColor("#287A2C"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#287A2C")));
                v++;
                break;
            case "Negro":
                //holder.color.setBackgroundColor(Color.parseColor("#000000"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                n++;
                break;
            default:
                //holder.color.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
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



    public class HeridoViewHolder extends RecyclerView.ViewHolder{

        LinearLayout personCardView, LinearAmbulancia;
        TextView txtNoPaciente, txtUbicacion, txtColor, txtLat, txtLng, txtAlt, txtAmbulancia;
        ImageView imagen, color;

        public HeridoViewHolder(View itemView) {
            super(itemView);
            personCardView = (LinearLayout) itemView.findViewById(R.id.herido_card);
            imagen= (ImageView)itemView.findViewById(R.id.imagen);
            txtNoPaciente= (TextView) itemView.findViewById(R.id.txtNoPaciente);
            txtUbicacion= (TextView) itemView.findViewById(R.id.txtUbicacion);
            txtColor= (TextView) itemView.findViewById(R.id.txtColor);
            color = (ImageView)itemView.findViewById(R.id.ivColor);
            txtLat = (TextView) itemView.findViewById(R.id.txtLat);
            txtLng = (TextView) itemView.findViewById(R.id.txtLng);
            txtAlt = (TextView) itemView.findViewById(R.id.txtAlt);
            txtAmbulancia = (TextView) itemView.findViewById(R.id.tvAmbulancia);
            LinearAmbulancia = (LinearLayout) itemView.findViewById(R.id.LinearAmbu);


        }

    }

    public void Filtrar(ArrayList<herido> filtroPersonas){
        this.listaHeridos = filtroPersonas;
        notifyDataSetChanged();
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
