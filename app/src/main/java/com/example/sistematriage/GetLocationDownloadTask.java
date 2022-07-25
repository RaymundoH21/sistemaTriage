package com.example.sistematriage;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetLocationDownloadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            int data = inputStreamReader.read();
            while (data != -1) {
                char curr = (char) data;
                result += curr;
                data = inputStreamReader.read();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            try {
                JSONObject locationObject = new JSONObject(result);
                JSONObject locationGeo = locationObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
