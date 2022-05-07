package com.pasoftxperts.covidetect.userhandler;

import java.io.Serializable;

public class Admin implements Serializable
{
    private String email;
    private String password;

    public Admin(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }
}
