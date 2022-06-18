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
import java.util.Optional;

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
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileIn);
            ObjectInputStream objIn = new ObjectInputStream(bufferedInputStream);

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
            bufferedInputStream.close();
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
                System.exit(1); // Something has gone really wrong
            }
        }

    }

    public Optional<Object> getResult() {
        return Optional.ofNullable(result);
    }

    public String getPath() {
        return path;
    }


    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof TaskObjectReader))
            return false;

        TaskObjectReader otherTaskReader = (TaskObjectReader) o;

        return (this.path.equals(otherTaskReader.getPath()));
    }

}
