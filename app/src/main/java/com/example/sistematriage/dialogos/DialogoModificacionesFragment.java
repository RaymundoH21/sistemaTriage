package com.example.sistematriage.dialogos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistematriage.ListaHeridos;
import com.example.sistematriage.ModificacionAdapter;
import com.example.sistematriage.R;
import com.example.sistematriage.VerticalSpaceItemDecoration;
import com.example.sistematriage.VolleySingleton;
import com.example.sistematriage.herido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DialogoModificacionesFragment extends DialogFragment {

    Activity actividad;

    ImageView btnSalir;
    LinearLayout barraSuperior;

    ModificacionAdapter adapter;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    RecyclerView recyclerModificaciones;
    ArrayList<herido> listaModificaciones;
    private RecyclerView.LayoutManager IManager;

    Context context;
    String NoPaciente;


    public DialogoModificacionesFragment(Context contexto, String Paciente) {
        context = contexto;
        NoPaciente = Paciente;
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogoModificaciones();
    }

    private Dialog crearDialogoModificaciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialogo_modificaciones, null);
        builder.setView(v);

        barraSuperior = v.findViewById(R.id.barraSalir);

        btnSalir = v.findViewById(R.id.btn_equis);

        listaModificaciones = new ArrayList<>();
        recyclerModificaciones = v.findViewById(R.id.recicladorModificaciones);
        IManager = new LinearLayoutManager(getActivity());
        recyclerModificaciones.setLayoutManager(IManager);
        recyclerModificaciones.setHasFixedSize(true);
        request= Volley.newRequestQueue(getActivity());
        adapter=new ModificacionAdapter(listaModificaciones);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerModificaciones.getContext(),
                ((LinearLayoutManager) recyclerModificaciones.getLayoutManager()).getOrientation());
        recyclerModificaciones.addItemDecoration(mDividerItemDecoration);

        recyclerModificaciones.addItemDecoration(new VerticalSpaceItemDecoration(20));

        eventosBotones();
        webService();

        return builder.create();
    }

    private void webService() {

        String url = "http://192.168.0.106/sistematriage/ConsultarModificaciones.php?NoPaciente="+NoPaciente;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                herido herido=null;

                JSONArray json=response.optJSONArray("modificacionescolor");

                try {

                    for (int i=0;i<json.length();i++){
                        herido = new herido();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        herido.setNoPaciente(jsonObject.optInt("NoPaciente"));
                        herido.setColor(jsonObject.optString("Color"));
                        herido.setEstado(jsonObject.optString("Estado"));
                        herido.setUsuario(jsonObject.optString("Usuario"));
                        herido.setFecha(jsonObject.optString("Fecha"));
                        listaModificaciones.add(herido);

                    }

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    recyclerModificaciones.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "No se han encontrado registros", Toast.LENGTH_SHORT).show();
                //progress.hide();
            }
        });


        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);

    }

    private void eventosBotones() {

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad= (Activity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialogo_modificaciones, container, false);
    }
}