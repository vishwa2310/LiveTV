package com.livetv;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by vishwajeetm on 29-05-2017.
 */


public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.getResponseCode();
            conn.getContentType();
           // System.out.println("url hit details content type="+conn.getContentType()+"responce code="+  conn.getResponseCode());
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
           // System.out.println("Inputstreammmmmmmmmm" + in);
            response = convertStreamToString(in);
            //System.out.println("Inputstreammmmmmmmmm   Resonse" + response);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e);
          //  System.out.println("mailfrmed Responce erorrrr" + e);
            response = "vishwa";

        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e);
          //  System.out.println("protocall Responce erorrrr" + e);
            response = "vishwa";
        } catch (IOException e) {
            Log.e(TAG, "Io Exaption http classs" + e);
          //  System.out.println("IO exaptions=====" + e);
            response = "vishwa";
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
           // System.out.println("exaption final" + e);
            response = "vishwa";
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
