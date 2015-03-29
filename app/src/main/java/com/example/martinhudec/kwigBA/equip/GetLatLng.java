package com.example.martinhudec.kwigBA.equip;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by martinhudec on 07/12/14.
 */
public class GetLatLng {

    LatLng ll;

    public GetLatLng() {


       // System.out.println("-----------------------------------------> RX1 ");


        HttpClient client = new DefaultHttpClient();
        //System.out.println("-----------------------------------------> RX2 ");

        try {
            URI web = new URI("http://bpbp.ctrgn.net/api/vehicle/39");
            HttpGet get = new HttpGet();
            get.setURI(web);
            //System.out.println("-----------------------------------------> RX3 ");

            HttpResponse response;
            response = client.execute(get);
           // System.out.println("-----------------------------------------> RX$ ");

            if (response != null) {


                JSONObject coordinatesJO = convertInputStreamToJSONObject(new InputStreamReader(response.getEntity().getContent()));

                JSONObject latlngJO = coordinatesJO.getJSONObject("coordinates");

                ll = new LatLng(Double.parseDouble(latlngJO.getString("latitude")), Double.parseDouble(latlngJO.getString("longitude")));

            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public LatLng getLl() {
        return ll;
    }
}
