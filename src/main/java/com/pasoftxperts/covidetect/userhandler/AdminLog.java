package com.pasoftxperts.covidetect.userhandler;

import java.io.File;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;

public class AdminLog implements Serializable
{
    private static ArrayList<Admin> adminList = new ArrayList<>();

    public static ArrayList<Admin> getAdminList(){ return adminList; }

    public static void addAdmin(Admin admin){ adminList.add(admin); } // protected?

    public static void removeAdmin(Admin admin){ adminList.remove(admin); } // protected?

    public static void updateAdminLog()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(new File(".\\src\\main\\java\\userhandler\\adminLog.ser"));
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            // Write object ArrayList
            objOut.writeObject(adminList);
            objOut.close();
        }
        catch (IOException i) { return; }
    }

    public static void readAdminLog()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(".\\src\\main\\java\\userhandler\\adminLog.ser");
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Read object ArrayList
            try { adminList = (ArrayList<Admin>) objIn.readObject(); }
            catch (ClassNotFoundException c){}

            objIn.close();
        }
        catch (IOException i) { return; }
    }

    public static void printAdminLog()
    {
        for (Admin a : adminList)
        {
            System.out.println("Admin: " + a.getEmail() + ", " + a.getPassword());
        }
    }
}
