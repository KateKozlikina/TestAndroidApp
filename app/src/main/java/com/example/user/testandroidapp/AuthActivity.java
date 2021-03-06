package com.example.user.testandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


public class AuthActivity extends AppCompatActivity {
    public static final String ACCESS_TOKEN = "myPreference";
    public EditText login;
    public EditText pass;
    String region = "RU";
    String log = "log";
    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mySharedPreferences = getSharedPreferences(ACCESS_TOKEN, MODE_PRIVATE);
        Button btn = (Button) findViewById(R.id.button1);
        login = (EditText) findViewById(R.id.editText1);
        login.setText("+7");
        login.setSelection(login.getText().length());
        pass = (EditText) findViewById(R.id.editText2);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
        String strLogin = login.getText().toString();
             if (login.getText().length()!=0 && pass.getText().length()!=0) {
                 if (isMobileNumberValid(strLogin, region))

                     try {
                         new PostAuthentication().execute();

                     } catch (Exception e) {
                         Toast.makeText(getApplicationContext(), e.toString(),
                                 Toast.LENGTH_LONG).show();
                     }
                     else
                     Toast.makeText(getApplicationContext(), "Такого номера не существует",
                             Toast.LENGTH_LONG).show();
             }
                else
                        Toast.makeText(getApplicationContext(), "Пустые логин или пароль",
                Toast.LENGTH_LONG).show();

            }
        });
        }

        //валидация номера телефона
    public boolean isMobileNumberValid(String phoneNumber, String defaultRegion)
    {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber swissNumberProto =null ;
        try {
            swissNumberProto = phoneUtil.parse(phoneNumber,  defaultRegion);
        } catch (NumberParseException e) {
            System.err.println( e.toString());
            return false;
        }

        if(phoneUtil.isValidNumber(swissNumberProto))
        {
            return true;
        }

        return false;
    }
//запрос на авторизацию
    public class PostAuthentication extends AsyncTask<String, Void, String> {
        private  String server = "nightlybuilds.i-retail.freematiq.com";
        private  String authentication = "/api/user/authentication";
        private  boolean remember_me = false;
        boolean status;
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try{
                String myURL = "https://"+server+authentication;
                URL url = new URL(myURL);
                status = false;
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("client_id", SingletonUser.getInstance().getClient_id());
                postDataParams.put("client_secret", SingletonUser.getInstance().getClient_secret());
                postDataParams.put("username", login.getText().toString());
                postDataParams.put("password", pass.getText().toString());
                postDataParams.put("remember_me", remember_me);
                Log.e(log,postDataParams.toString());
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
                    Log.e(log,sb.toString());
                    in.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    status = (boolean)jsonObject.get("status");
                    if (status == true) {
                        editor = mySharedPreferences.edit();
                        editor.putString("access_token",jsonObject.getJSONObject("result").get("access_token").toString());
                        editor.apply();
                        Log.e(log,"Загружаем данные");
                        return new String("Загружаем данные");

                    }
                    else
                        return new String("Неверный логин или пароль");

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(log,result);
            if (status) {
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();

        }
    }

}


