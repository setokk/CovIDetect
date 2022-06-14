/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used for checking if a password fulfils the requirements of the system
 |
 |
 | Requirements:
 |     - Password length MUST be between MIN_PASSWORD_LENGTH (8) and MAX_PASSWORD_LENGTH (15)
 |     - Password MUST contain at least one special character (see List<String> SPECIAL_CHARACTERS)
 |     - Password MUST contain at least one uppercase character
 |
 |
 | Method Documentation:
 |     [*] public static boolean hasSpecialCharacter(String password)
 |         Returns true if password has at least one special character
 |
 |     [*] public static boolean hasAppropriateLength(String password)
 |         Returns true if password has appropriate length
 |
 |     [*] public static boolean hasUpperCase(String password)
 |         Returns true if password has at least one upper case character
 |
*/

package com.pasoftxperts.covidetect.passwordchecker;

import java.util.Arrays;
import java.util.List;

public class PasswordChecker
{
    public static final List<String> SPECIAL_CHARACTERS =
            Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*",
                    "(", ")", ";", ":", "'", "/", "\\", "|",
                    "{", "}", "[", "]", "-", "_", "+", "=",
                    "?", "`", "~", ",", ".", "<", ">");

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 15;

    public static boolean hasSpecialCharacter(String password)
    {
        for (String c : SPECIAL_CHARACTERS)
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
            if (Character.isUpperCase(password.charAt(i)))
                return true;
        }
        return false;
    }
}
