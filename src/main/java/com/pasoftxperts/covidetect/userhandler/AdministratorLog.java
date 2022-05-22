package com.pasoftxperts.covidetect.userhandler;

import java.io.*;

import java.util.ArrayList;

public class AdministratorLog implements Serializable
{
    private static ArrayList<Administrator> adminList = new ArrayList<>();

    public static ArrayList<Administrator> getAdminList(){ return adminList; }

    public static void addAdmin(Administrator admin){ adminList.add(admin); }

    public static void removeAdmin(Administrator admin){ adminList.remove(admin); }

    // We save the admin log file to the user directory of the jar file.
    private static String path = System.getProperty("user.dir");

    public static void updateAdminLog()
    {
        try
        {
            // Make directory adminlog if it does not exist
            new File(path + "/adminlog").mkdirs();

            FileOutputStream fileOut = new FileOutputStream(path + "/adminlog/adminLog.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            // Write object ArrayList
            objOut.writeObject(adminList);

            objOut.close();
            fileOut.close();
        }
        catch (IOException i) { return; }
    }

    public static void readAdminLog()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(path + "/adminlog/adminLog.ser");
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Read object ArrayList
            try { adminList = (ArrayList<Administrator>) objIn.readObject(); }
            catch (ClassNotFoundException c){}

            objIn.close();
            fileIn.close();
        }
        catch (IOException i) { return; }
    }

    // Checks if the email is not registered. If registered, returns the admin with that email.
    // Otherwise, it returns an admin with an email of "Not Registered"
    public static Administrator emailIsNotRegistered(String email)
    {
        return adminList.stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst()
                .orElse(new Administrator("Not Registered", ""));
    }
}
