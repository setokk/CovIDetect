package com.pasoftxperts.covidetect.guicontrollers.passwordchecker;

import java.util.Arrays;
import java.util.List;

public class PasswordChecker
{
    public static final List<String> specialCharacters =
            Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*",
                    "(", ")", ";", ":", "'", "/", "\\", "|",
                    "{", "}", "[", "]", "-", "_", "+", "=",
                    "?", "`", "~", ",", ".", "<", ">");

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 15;

    public static boolean hasSpecialCharacter(String password)
    {
        for (String c : specialCharacters)
        {
            if (password.contains(c))
                return true;
        }
        return false;
    }

    public static boolean hasAppropriateLength(String password)
    {
        return ((password.length() >= MIN_PASSWORD_LENGTH) && (password.length() <= MAX_PASSWORD_LENGTH));
    }

    public static boolean hasUpperCase(String password)
    {
        for(int i=0; i<password.length(); i++)
        {
            if(Character.isUpperCase(password.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }
}
