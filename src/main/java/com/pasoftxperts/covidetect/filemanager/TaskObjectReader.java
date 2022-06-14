/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to just read a .ser file (not using threads) (used for JavaFX Service concurrency)
 |
 |
*/

package com.pasoftxperts.covidetect.filemanager;

import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(fileIn));

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
                PopupWindow.display("Could not read room file");
            }
            catch (IOException ex)
            {
                System.exit(1); // Something has gone really wrong
            }
        }

    }

    public Object getResult() { return result; }

}
