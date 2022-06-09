package com.pasoftxperts.covidetect.filemanager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/* This class is used to just read a .ser file (not using threads) (used for JavaFX Service concurrency) */
public class ObjectTaskReader
{
    private String path;
    private Object result;

    public ObjectTaskReader(String path)
    {
        this.path = path;
        this.result = null;
    }

    public void readObjectFile()
    {
        Object object = new Object();

        try
        {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Write object ArrayList
            try { object = (Object) objIn.readObject(); }
            catch (ClassNotFoundException e) { e.printStackTrace(); }

            objIn.close();
            fileIn.close();
        }
        catch (IOException e) { e.printStackTrace(); }

        result = object;
    }

    public Object getResult() { return result; }

}
