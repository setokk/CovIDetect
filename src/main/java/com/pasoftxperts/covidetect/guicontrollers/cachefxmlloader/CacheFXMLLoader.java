/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to load the GUI scenes faster by caching the all the scenes in the login page (JavaFX FXMLLoader)
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader;

import com.pasoftxperts.covidetect.RunApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CacheFXMLLoader
{
    public static final FXMLLoader loader = new FXMLLoader();
    private static int c = 0; // Counts how many times it has been initialized

    public static Parent load(String resourceName) throws IOException
    {
        return loader.load(RunApplication.class.getResource(resourceName));
    }
}


