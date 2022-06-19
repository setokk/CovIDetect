/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class contains methods for verifying the email address of a user.
 |   Method Documentation:
 |        [*] boolean isValid(String email)
 |            -> Takes an email as input.
 |           -> Runs an email validator script (uses an email verification API).
 |            == Returns whether the email is valid or not.
 |
 |        [*] String checkAcademicEmail(String email)
 |            -> Takes an email as input.
 |            -> Checks whether email contains one of the academic email domains.
 |            == Returns a string with that academic domain name.
 |               If it is not an academic email, returns "Not Academic".
 |
*/

package com.pasoftxperts.covidetect.emailverification;

import com.pasoftxperts.covidetect.executablefile.ExecuteExeFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class EmailVerifier
{
    // List of academic email domains
    public static final List<String> ACADEMIC_EMAIL_LIST = Arrays.asList("@uom.edu.gr", "@uom.gr");

    // Api Key for email verification.
    // Previous API key 65a83ee9a9dce5af8a939d3dc2dea5cd
    public static final String API_KEY = "c1e6325e9eaa014f01fdc04151cd6bae";

    public static boolean isValid(String email) throws IOException
    {
        boolean result = false;

        // Run API Email Verifier Script (Python JSON)
        ExecuteExeFile.copyToTempDir("emailvalidator.exe");
        File script = new File("emailvalidator.exe");

        Process process;

        try
        {
            process = new ProcessBuilder
                    ("emailvalidator.exe", email, API_KEY)
                    .start();
            process.waitFor();
        }
        catch (IOException|InterruptedException e)
        {
            e.printStackTrace();
        }


        //
        // Reading the output of the Email Verifier Script
        //
        try
        {
            File output = new File("ValidEmailOutput.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(output));

            String temp = bufferedReader.readLine();
            result = Boolean.parseBoolean(temp);

            bufferedReader.close();

            // Delete files
            boolean scriptDel = script.delete();
            boolean outputDel = output.delete();
            if (!scriptDel || !outputDel)
                throw new Exception("Cannot delete files.");

        } catch (Exception e) { return false; }

        return result;
    }


    public static String checkAcademicEmail(String email)
    {
        return ACADEMIC_EMAIL_LIST.stream()
                                  .filter(email::contains)
                                  .findFirst()
                                  .orElse("Not Academic");
    }
}


