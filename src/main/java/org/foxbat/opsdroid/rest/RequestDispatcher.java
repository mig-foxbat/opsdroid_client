package org.foxbat.opsdroid.rest;

/**
 * Created by chlr on 9/19/14.
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestDispatcher
{
    public RequestDispatcher()
    {
    }


    public String getStringResponse(String address) {
        InputStream is;
        try {
            URL url = new URL(address);
            HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000 /* milliseconds */);
            conn.setConnectTimeout(100000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String contentAsString = getString(is);
            return contentAsString;
        }
        catch ( IOException e) {
            e.printStackTrace();
            return null;
        }


    }


    public static String getString( InputStream is) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        while((ch = is.read())!= -1)
            sb.append((char)ch);
        return sb.toString();
    }




    public JSONArray getJsonArrayResponse (String url) {
        try {
            String str = this.getStringResponse(url);
            return new JSONArray(str);
        }
        catch (JSONException e)
        {
            Log.v(getClass().getName(),e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public JSONObject getJsonObjectResponse (String url) {

        try {
            String str = this.getStringResponse(url);
            return new JSONObject(str);
        }
        catch (JSONException e) {
            e.getMessage();
            e.printStackTrace();
            return null;
        }
    }




}

