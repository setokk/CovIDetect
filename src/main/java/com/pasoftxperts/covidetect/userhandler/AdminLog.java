package com.pasoftxperts.covidetect.userhandler;

import com.pasoftxperts.covidetect.RunApplication;

import java.io.*;

import java.util.ArrayList;

public class AdminLog implements Serializable
{
    private static ArrayList<Admin> adminList = new ArrayList<>();

    public static ArrayList<Admin> getAdminList(){ return adminList; }

    public static void addAdmin(Admin admin){ adminList.add(admin); } // protected?

    public static void removeAdmin(Admin admin){ adminList.remove(admin); } // protected?

    // We save the admin log file to the user directory of the jar file.
    private static String path = System.getProperty("user.dir");

    public static void updateAdminLog()
    {
        try
        {
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
            try { adminList = (ArrayList<Admin>) objIn.readObject(); }
            catch (ClassNotFoundException c){}

            objIn.close();
            fileIn.close();
        }
        catch (IOException i) { return; }
    }

    // Checks if the email is not registered. If registered, returns the admin with that email.
    // Otherwise, it returns an admin with an email of "Not Registered"
    public static Admin emailIsNotRegistered(String email)
    {
        return adminList.stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst()
                .orElse(new Admin("Not Registered", ""));
    }
}
