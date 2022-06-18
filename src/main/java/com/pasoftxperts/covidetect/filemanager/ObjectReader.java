/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to read a single .ser file as a Thread
 |
 |
*/

package com.pasoftxperts.covidetect.filemanager;

import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

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
            ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(fileIn));

            // Write object ArrayList
            try
            {
                result = objIn.readObject();
            }
            catch (ClassNotFoundException e)
            {
                PopupWindow.display("Simulation files not found. Please run simulation from the home page.");
            }

            objIn.close();
            fileIn.close();
        }
        catch (IOException e)
        {
            try
            {
                PopupWindow.display("Simulation files not found. Please run simulation from the home page.");
            }
            catch (IOException ex)
            {
                System.exit(1);
            }
        }
    }

    public Optional<Object> getResult() {
        return Optional.ofNullable(result);
    }
}
