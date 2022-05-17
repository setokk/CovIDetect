package com.pasoftxperts.covidetect.userhandler;

import java.io.Serializable;

public class Administrator implements Serializable
{
    private String email;
    private String password;

    public Administrator(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    @Override
    public boolean equals(Object o)
    {
        if ( !(o instanceof Administrator) )
            return false;

        Administrator otherAdmin = (Administrator) o;

        return (this.email.equals(otherAdmin.getEmail()) &&
                this.password.equals(otherAdmin.getPassword()));
    }
}
