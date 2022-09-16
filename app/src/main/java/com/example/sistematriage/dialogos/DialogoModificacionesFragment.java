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


/* Esta clase pertenece al dialog que aparece en la pantalla de perfil del herido
al momento de presionar sobre el botón de la esquina superior derecha y muestra
el historial de modificaciones */

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
    String url;


    // Constructor de la clase que recibe el contexto de la activity el número de paciente
    public DialogoModificacionesFragment(Context contexto, String Paciente) {
        context = contexto;
        NoPaciente = Paciente;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogoModificaciones();
    }

    private Dialog crearDialogoModificaciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // La vista de esta clase se encuentra en el archivo fragment_dialogo_modificaciones.xml
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialogo_modificaciones, null);
        builder.setView(v);

        // Referencias a los elementos del archivo fragment_dialogo_modificaciones.xml
        barraSuperior = v.findViewById(R.id.barraSalir);
        btnSalir = v.findViewById(R.id.btn_equis);
        listaModificaciones = new ArrayList<>();
        recyclerModificaciones = v.findViewById(R.id.recicladorModificaciones);
        IManager = new LinearLayoutManager(getActivity());
        recyclerModificaciones.setLayoutManager(IManager);
        recyclerModificaciones.setHasFixedSize(true);


        request= Volley.newRequestQueue(getActivity());
        adapter=new ModificacionAdapter(listaModificaciones);

        // Elemento para dividir cada elemento del recyclerView
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerModificaciones.getContext(),
                ((LinearLayoutManager) recyclerModificaciones.getLayoutManager()).getOrientation());
        recyclerModificaciones.addItemDecoration(mDividerItemDecoration);

        // Cambiar el valor en caso de aumentar o reducir el espacio de separación de los elementos
        recyclerModificaciones.addItemDecoration(new VerticalSpaceItemDecoration(20));

        eventosBotones();
        webService();

        return builder.create();
    }

    // Destructor de la clase
    @Override
    public void onDestroy() {
        super.onDestroy();

        /* Se cancela la petición al servidor, se hacen null los objetos y variables para
        que el recolector de basura pueda liberar memoria al cambiar de actividad */

        jsonObjectRequest.cancel();
        btnSalir = null;
        barraSuperior = null;
        adapter = null;
        request = null;
        jsonObjectRequest = null;
        recyclerModificaciones = null;
        listaModificaciones = null;
        IManager = null;
        context = null;
        NoPaciente = null;
        url = null;

        // Llamada al recolector de basura
        Runtime.getRuntime().gc();

    }

    private void webService() {

        /* url del servidor local (XAMPP) o servidor web (AWS), solo comentar una y
        descomentar la otra, dependiendo de a cual servidor se hará la conexión */

        //url = "http://192.168.1.12/sistematriage/ConsultarModificaciones.php?NoPaciente="+NoPaciente;
        url = "http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/ConsultarModificaciones.php?NoPaciente="+NoPaciente;

        // Listener para la petición al servidor
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                herido herido=null;

                // Nombre del objeto json devuelto por el archivo php, debe ser el mismo nombre
                JSONArray json=response.optJSONArray("modificacionescolor");

                try {

                    // Ciclo for para recorrer todos los registros devueltos en la petición
                    for (int i=0;i<json.length();i++){
                        herido = new herido();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);


                        // Cada registro obtenido de la BD es asignado a objetos de tipo herido
                        herido.setNoPaciente(jsonObject.optInt("NoPaciente"));
                        herido.setColor(jsonObject.optString("Color"));
                        herido.setEstado(jsonObject.optString("Estado"));
                        herido.setUsuario(jsonObject.optString("Usuario"));
                        herido.setFecha(jsonObject.optString("Fecha"));
                        // Cada objeto se añade a una lista
                        listaModificaciones.add(herido);

                    }

                    // Modificar aquí para agregar una acción al presionar sobre cada elemento de la lista
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    // Se actualizan los datos en el recyclerView
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

        // Se envía la petición por medio de Volley
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);

    }

    private void eventosBotones() {
        // Listener para el botón de cerrar dialog en la parte superior derecha
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