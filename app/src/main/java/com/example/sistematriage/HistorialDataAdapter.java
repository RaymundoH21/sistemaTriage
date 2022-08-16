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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.ArrayList;

public class HistorialDataAdapter extends RecyclerView.Adapter<HistorialDataAdapter.HistorialViewHolder> implements View.OnClickListener {
    private View.OnClickListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    List<herido> listaHeridos;
    private Activity activity;

    int t = 0, r = 0, a = 0, v = 0, n = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;



    public HistorialDataAdapter(List<herido> heridos, RecyclerView recyclerView, Activity activity) {
        this.activity = activity;
        listaHeridos = heridos;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (! loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        return listaHeridos.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public HistorialDataAdapter.HistorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_card,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new HistorialDataAdapter.HistorialViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialDataAdapter.HistorialViewHolder holder, int position) {
        if(listaHeridos.get(position).getImagen()!=null) {
/*
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), listaHeridos.get(position).getImagen());
            roundedBitmapDrawable.setCircular(true);
            holder.imagen.setImageDrawable(roundedBitmapDrawable);
            //holder.imagen.setImageBitmap(listaPersonas.get(position).getImagen());*/

            //String ip = "http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/imagenes/Rojo.jpg";

            Glide.with(activity).load(listaHeridos.get(position).getImagen())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imagen);
            //Picasso.get().load(ip).resize(150, 150).into(holder.imagen);
            //RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), listaHeridos.get(position).getImagen());
            //roundedBitmapDrawable.setCircular(true);
            //holder.imagen.setImageDrawable(roundedBitmapDrawable);

            //holder.imagen.setImageBitmap(getResizedBitmap(listaHeridos.get(position).getImagen(),250,250));
            //holder.imagen.setImageBitmap(listaHeridos.get(position).getImagen());
        }
        else{
            holder.imagen.setImageResource(R.drawable.ic_launcher_foreground);
        }
        holder.txtNoPaciente.setText(listaHeridos.get(position).getNoPaciente().toString());
        holder.txtDestino.setText(String.valueOf(listaHeridos.get(position).getDestino().toString()));

        holder.txtColor.setText("Color: " + String.valueOf(listaHeridos.get(position).getColor().toString()));

        if (listaHeridos.get(position).getEstado().equals("Recibido")){
            holder.linearRecibido.setVisibility(View.VISIBLE);
        } else {
            holder.linearRecibido.setVisibility(View.GONE);
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


    public void setLoaded(boolean state) {
        loading = state;
    }

    @Override
    public int getItemCount() {
        return listaHeridos.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
        {
            listener.onClick(v);
        }
    }


    //
    public static class HistorialViewHolder extends RecyclerView.ViewHolder{

        LinearLayout personCardView, linearRecibido;
        TextView txtNoPaciente, txtDestino, txtColor;
        ImageView imagen, color;

        public HistorialViewHolder(View itemView) {
            super(itemView);
            personCardView = (LinearLayout) itemView.findViewById(R.id.historial_card);
            imagen = (ImageView)itemView.findViewById(R.id.imagen);
            txtNoPaciente = (TextView) itemView.findViewById(R.id.txtNoPaciente);
            txtDestino = (TextView) itemView.findViewById(R.id.txtDestino);
            txtColor = (TextView) itemView.findViewById(R.id.txtColor);
            color = (ImageView)itemView.findViewById(R.id.ivColor);
            linearRecibido = (LinearLayout) itemView.findViewById(R.id.LinearPalomita);

        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            //progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public void setOnClickListener(View.OnClickListener Listener)
    {
        this.listener = Listener;
    }

}
