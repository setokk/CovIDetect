package com.pasoftxperts.covidetect.userhandler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorLogTest
{

    @Test
    void emailIsNotRegistered()
    {
        // We test if a specific email is not registered.
        // We expect to get a dummy admin which has an email of "Not Registered" if not registered.
        Administrator admin = new Administrator("testemail@test.com", "junittest");

        AdministratorLog.addAdmin(admin);

        AdministratorLog.removeAdmin(admin);

        String testEmail = "TTestemail@test.com";

        assertEquals(new Administrator("Not Registered", ""), AdministratorLog.emailIsNotRegistered(testEmail));
    }
}