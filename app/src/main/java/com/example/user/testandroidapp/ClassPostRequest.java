package com.example.user.testandroidapp;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

public class ClassPostRequest extends AsyncTask<String, Void, String> {
    protected static String server = "nightlybuilds.i-retail.freematiq.com";
    protected static String client_id = "PIPO_WEBAPP";
    protected static String client_secret = "DJF22HS^#Khdsfj325ruh)_#";
    private static String getData = "/api/user/get-data";
    private static String authentication = "/api/user/authentication";
    protected static boolean remember_me = false;

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
