package com.pasoftxperts.covidetect.filemanager;

import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/* This class is used to read a single .ser file as a Thread */
public class ObjectReader extends Thread
{
    private String path; // Path to read file at
    private Object result; // Result of the file

    public ObjectReader(String path)
    {
        this.path = path;
        this.result = null;
    }

    @Override
    public void run()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Write object ArrayList
            try
            {
                result =(Object) objIn.readObject();
            }
            catch (ClassNotFoundException e)
            {
                PopupWindow.display("Could not read object file");
            }

            objIn.close();
            fileIn.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public Object getResult() { return result; }
}
