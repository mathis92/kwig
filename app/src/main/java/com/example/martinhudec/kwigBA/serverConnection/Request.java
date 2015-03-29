package com.example.martinhudec.kwigBA.serverConnection;

import android.net.Network;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by martinhudec on 26/03/15.
 */
public class Request {
    String requestUrl = null;
    JSONObject requestedJSONObject = null;

    public Request(String requestUrl) {


        this.requestUrl = requestUrl;

        try {
            URL url = new URL("http://bpbp.ctrgn.net/api" + requestUrl);

            URLConnection urlConnection = url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            try {
                Log.d("Request", in.toString());
                requestedJSONObject = convertInputStreamToJSONObject(new InputStreamReader(in));
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                in.close();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getRequestedJSONObject() {
        return requestedJSONObject;
    }

    private static JSONObject convertInputStreamToJSONObject(InputStreamReader inputStream)
            throws JSONException, IOException {
        BufferedReader bufferedReader = new BufferedReader(inputStream);
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return new JSONObject(result);

    }


}

