package com.example.sistematriage;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    //private static final String LOGIN_REQUEST_URL="http://192.168.1.12/sistematriage/LoginDoctor.php";
    private static final String LOGIN_REQUEST_URL="http://ec2-54-183-143-71.us-west-1.compute.amazonaws.com/LoginDoctor.php";

    private Map<String,String> params;
    public  LoginRequest(String numero, String contra, Response.Listener<String> listener ){
        super(Request.Method.POST, LOGIN_REQUEST_URL,listener,null);
        params= new HashMap<>();
        params.put("numero",numero+"");
        params.put("contra",contra);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
