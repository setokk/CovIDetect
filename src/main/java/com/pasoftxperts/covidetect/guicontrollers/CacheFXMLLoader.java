/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 |
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CacheFXMLLoader
{
    public static final FXMLLoader loader = new FXMLLoader();

    public static Parent load(String resourceName) throws IOException
    {
        return loader.load(RunApplication.class.getResource(resourceName));
    }
}
