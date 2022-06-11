package com.pasoftxperts.covidetect.filemanager;

import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/* This class is used to just read a .ser file (not using threads) (used for JavaFX Service concurrency) */
public class TaskObjectReader
{
    private String path;
    private Object result;

    public TaskObjectReader(String path)
    {
        this.path = path;
        this.result = null;
    }

    public void readObjectFile()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Write object ArrayList
            try
            {
                result = (Object) objIn.readObject();
            }
            catch (ClassNotFoundException e) { e.printStackTrace(); }

            objIn.close();
            fileIn.close();
        }
        catch (IOException e)
        {
            try
            {
                PopupWindow.display("Could not read task object file");
            }
            catch (IOException ex)
            {
                System.exit(1); // Something has gone really wrong
            }
        }

    }

    public Object getResult() { return result; }

}
