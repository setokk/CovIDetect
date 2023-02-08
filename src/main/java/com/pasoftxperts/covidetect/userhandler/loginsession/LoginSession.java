/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to keep the logged in user's email and username as a session from the login page
 | so that they can be used in the main application. (see user in header of application GUI)
 |
 |
*/

package com.pasoftxperts.covidetect.userhandler.loginsession;

public final class LoginSession
{
    private static String email = "";
    private static String username = "";


    public static String getEmail() { return email; }

    public static void setEmail(String email)
    {
        LoginSession.email = email;

        for (int i = 0; i < LoginSession.email.length(); i++)
        {
            if (LoginSession.email.charAt(i) == '@')
                break;

            LoginSession.username += LoginSession.email.charAt(i);
        }
    }

    public static void resetSession()
    {
        email = "";
        username = "";
    }

    public static String getUsername() { return username; }
}
