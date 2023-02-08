package com.pasoftxperts.covidetect.passwordchecker;

import com.pasoftxperts.covidetect.client.passwordchecker.PasswordChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordCheckerTest
{

    @Test
    void hasSpecialCharacter()
    {
        // We test if a specific password has at least one special character
        String password = "Abcdefo2";

        assertEquals(false, PasswordChecker.hasSpecialCharacter(password));
    }

    @Test
    void hasAppropriateLength()
    {
        // We test if a specific password has the appropriate length (this case 8 between 15)
        String password = "bf432!A";

        assertEquals(false, PasswordChecker.hasAppropriateLength(password));
    }

    @Test
    void hasUpperCase()
    {
        // We test if a specific password has at least one upper case
        String password = "32p@#^&c";

        assertEquals(false, PasswordChecker.hasUpperCase(password));
    }
}