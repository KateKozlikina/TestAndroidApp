package com.example.user.testandroidapp;

public class SingletonUser {
    private static volatile SingletonUser instance;
    private  String client_id = "PIPO_WEBAPP";
    private  String client_secret = "DJF22HS^#Khdsfj325ruh)_#";
    private String name;
    private String phone;
    private String email;
    private String access_token;


    private SingletonUser() {    }

    public static SingletonUser getInstance() {
        if (instance == null) {
            synchronized (SingletonUser.class) {
                if (instance == null) {
                    instance = new SingletonUser();
                }
            }
        }
        return instance;
    }

    public  String getClient_id() { return client_id;    }

    public  String getClient_secret() { return client_secret; }

    public String getEmail() { return email;    }

    public String getName() { return name;    }

    public String getPhone() { return phone;    }

    public String getAccess_token() {        return access_token;    }

    public void setAccess_token(String access_token) {        this.access_token = access_token;    }

    public void setEmail(String email) {        this.email = email;    }

    public void setName(String name) {        this.name = name;    }

    public void setPhone(String phone) {        this.phone = phone;    }

}
