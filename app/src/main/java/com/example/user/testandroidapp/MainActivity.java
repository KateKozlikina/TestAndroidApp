package com.example.user.testandroidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public static final String ACCESS_TOKEN = "myPreference";
    TextView textName;
    TextView textPhone;
    TextView textEmail;
    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor editor;
    String log = "log";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySharedPreferences = getSharedPreferences(ACCESS_TOKEN, MODE_PRIVATE);
        textName = (TextView) findViewById(R.id.textUserName);
        textPhone = (TextView) findViewById(R.id.textUserPhone);
        textEmail = (TextView) findViewById(R.id.textUserEmail);

//                = SingletonUser.getInstance().getAccess_token() ;
//        if(SingletonUser.getInstance().getAccess_token()==null)
          SingletonUser.getInstance().setAccess_token( mySharedPreferences.getString("access_token", null));
//        else
//            editor.putString("access_token", null);
//        editor.apply();
//            accessToken = SingletonUser.getInstance().getAccess_token();

        Log.e(log,"accessToken:"+SingletonUser.getInstance().getAccess_token());
     if (SingletonUser.getInstance().getAccess_token() != null) {
         //SingletonUser.getInstance().setFlqAuth(true);
         try {
             new SendPostRequest().execute();
         } catch (Exception e) {
         }
     }
     else {
         Intent intent = new Intent(this, AuthActivity.class);
         startActivity(intent);
     }



    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {
        private  String server = "nightlybuilds.i-retail.freematiq.com";
        private  String client_id = "PIPO_WEBAPP";
        private  String client_secret = "DJF22HS^#Khdsfj325ruh)_#";
        private  String getData = "/api/user/get-data";
        private  String authentication = "/api/user/authentication";
        public  boolean remember_me = false;
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try{
                String myURL = "https://"+server+getData;
                URL url = new URL(myURL);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("client_id", SingletonUser.getInstance().getClient_id());
                postDataParams.put("client_secret", SingletonUser.getInstance().getClient_secret());
                postDataParams.put("access_token", SingletonUser.getInstance().getAccess_token());
                Log.e("log",postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                PostData postData = new PostData();
                writer.write(postData.getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    Log.e("log",sb.toString());
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    boolean status = (boolean) jsonObject.get("status");
                    if (!status) {
                        editor = mySharedPreferences.edit();
                        editor.putString("access_token", null);
                        editor.apply();
                        SingletonUser.getInstance().setAccess_token(null);
                        Log.e("log","Обнуляем access token");
                       // SingletonUser.getInstance().setAccess_token(null);
                    }
                    else {
                        SingletonUser.getInstance().setName(jsonObject.getJSONObject("result").get("name").toString());
                        SingletonUser.getInstance().setPhone(jsonObject.getJSONObject("result").get("phone").toString());
                        SingletonUser.getInstance().setEmail(jsonObject.getJSONObject("result").get("email").toString());
                    }

                }
                else {
                    return new String("false : "+responseCode); //ошибка в запросе
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {

            if (SingletonUser.getInstance().getAccess_token()==null) {
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(intent);
            }
            else {
                textName.setText(SingletonUser.getInstance().getName());
                textPhone.setText(SingletonUser.getInstance().getPhone());
                textEmail.setText(SingletonUser.getInstance().getEmail());
            }

        }
    }

}





