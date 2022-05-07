/* ------------------------------------------------------------------
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
   ------------------------------------------------------------------ */

package com.pasoftxperts.covidetect.emailverification;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EmailVerifier
{
    // List of academic email domains
    public static final List<String> academicEmailList = Arrays.asList("@uom.edu.gr");

    public static boolean isValid(String email)
    {
        boolean result = false;

        String path = ".\\src\\main\\java\\emailverification\\";


        // API Email Verifier (Python JSON)
        try
        {
            Process process = new ProcessBuilder
                    (path + "emailvalidator.exe", email, path)
                    .start();
            process.waitFor();
        }
        catch (IOException|InterruptedException e) { System.exit(1); }


        // Reading the output of the email verifier script
        try
        {
            File resultFile = new File(path + "ValidEmailOutput.txt");

            FileReader fileReader = new FileReader(resultFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String temp = bufferedReader.readLine();
            result = Boolean.parseBoolean(temp);

            bufferedReader.close();
            fileReader.close();
            if (!resultFile.delete())
                throw new Exception("Could not delete api script result file");

            // Deallocate memory
            bufferedReader = null;
            fileReader = null;
            resultFile = null;
            temp = null;
            System.gc();

        } catch (Exception exception) { System.exit(1); }

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


