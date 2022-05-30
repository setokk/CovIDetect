package com.pasoftxperts.covidetect.filemanager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReader extends Thread
{
    private String path;
    private Object result;

    public ObjectReader(String path)
    {
        this.path = path;
        this.result = null;
    }

    @Override
    public void run()
    {
        result = readObjectFile(path);
    }

    public Object readObjectFile(String path)
    {
        Object object = new Object();

        try
        {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Write object ArrayList
            try { object =(Object) objIn.readObject(); }
            catch (ClassNotFoundException e) { e.printStackTrace(); }

            objIn.close();
            fileIn.close();
        }
        catch (IOException e) { e.printStackTrace(); }

        return object;
    }

    public Object getResult() { return result; }
}
