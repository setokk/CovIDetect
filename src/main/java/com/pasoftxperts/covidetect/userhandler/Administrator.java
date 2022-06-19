/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class represents an admin (user).
 | Each user has an email and a password.
 |
 |
*/

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
