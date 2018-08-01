package com.example.user.testandroidapp;

public class User {
    private  String client_id = "PIPO_WEBAPP";
    private  String client_secret = "DJF22HS^#Khdsfj325ruh)_#";
    private  boolean remember_me = false;
    private String login;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String access_token;
    private boolean flqAuth;

    public  String getClient_id() { return client_id;    }

    public  String getClient_secret() { return client_secret; }
    public  boolean getRemember_me() { return remember_me;}

    public String getEmail() { return email;    }

    public String getName() { return name;    }

    public String getPhone() { return phone;    }

    public boolean isFlqAuth() {        return flqAuth;    }

    public boolean isRemember_me() {        return remember_me;    }

    public String getAccess_token() {        return access_token;    }

    public String getLogin() {        return login;    }

    public String getPassword() {        return password;    }

    public void setLogin(String login) {        this.login = login;    }

    public void setPassword(String password) {        this.password = password;    }

    public void setAccess_token(String access_token) {        this.access_token = access_token;    }

    public void setFlqAuth(boolean flqAuth) { this.flqAuth = flqAuth;    }

    public void setEmail(String email) {        this.email = email;    }

    public void setName(String name) {        this.name = name;    }

    public void setPhone(String phone) {        this.phone = phone;    }
}
