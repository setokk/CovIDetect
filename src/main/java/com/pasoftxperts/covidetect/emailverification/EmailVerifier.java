/* -------------------------------------------------------------------
This class contains methods for verifying the email address of a user.
    Method Documentation:
        [*] boolean isValid(String email)
            -> Takes an email as input.
            -> Runs an email validator script (uses an email verification API).
            == Returns whether the email is valid or not.

        [*] String checkAcademicEmail(String email)
            -> Takes an email as input.
            -> Checks whether email contains one of the academic email domains.
            == Returns a string with that academic domain name.
               If it is not an academic email, returns "Not Academic".
   ------------------------------------------------------------------- */

package com.pasoftxperts.covidetect.emailverification;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.executablefile.ExecuteExeFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class EmailVerifier
{
    // List of academic email domains
    public static final List<String> academicEmailList = Arrays.asList("@uom.edu.gr");

    // Api Key for email verification.
    public static final String apiKey = "65a83ee9a9dce5af8a939d3dc2dea5cd";

    public static boolean isValid(String email) throws IOException
    {
        boolean result = false;

        // Run API Email Verifier Script (Python JSON)
        OutputStream os = ExecuteExeFile.copyToTempDir("emailvalidator.exe");
        File script = new File("emailvalidator.exe");

        Process process;

        try
        {
            // We say substring(1) because path here starts off / (python script does not understand it)
            process = new ProcessBuilder
                    ("emailvalidator.exe", email, apiKey)
                    .start();
            process.waitFor();
            System.out.println();
        }
        catch (IOException|InterruptedException e)
        {
            // Retry again
            e.printStackTrace();
        }

        // Reading the output of the Email Verifier Script
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

            // Deallocate memory
            bufferedReader = null;
            temp = null;
            System.gc();

        } catch (Exception e) { e.printStackTrace(); }

        return result;
    }

    public static String checkAcademicEmail(String email)
    {
        return academicEmailList.stream()
                .filter(e -> email.contains(e))
                .findFirst()
                .orElse("Not Academic");
    }
}


