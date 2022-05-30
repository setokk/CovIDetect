package com.pasoftxperts.covidetect.filemanager;

import java.io.*;
import java.util.ArrayList;

public class ObjectListReader
{
    public static ArrayList<Object> readObjectListFile(String path)
    {
        ArrayList<Object> objectList = new ArrayList<>();

        try
        {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Write object ArrayList
            try { objectList = (ArrayList<Object>) objIn.readObject(); }
            catch (ClassNotFoundException e) { e.printStackTrace(); }

            objIn.close();
            fileIn.close();
        }
        catch (IOException e) {}

        return objectList;
    }
}
