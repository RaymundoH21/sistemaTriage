package com.example.sistematriage;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/* Esta clase se utiliza para el inicio de sesión, consulta la base de datos del hospital (parámedico) */

public class LoginRequestDos extends StringRequest {
    //private static final String LOGIN_REQUEST_URLDOS="http://192.168.1.12/sistematriage/Loginparamedico.php";
    private static final String LOGIN_REQUEST_URLDOS="http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/Loginparamedico.php";

    private Map<String,String> para;
    public  LoginRequestDos(String num, String con, Response.Listener<String> listenere ){
        super(Request.Method.POST, LOGIN_REQUEST_URLDOS,listenere,null);
        para= new HashMap<>();
        para.put("numero",num+"");
        para.put("contra",con);
    }

    @Override
    public Map<String, String> getParams() {
        return para;
    }
}
