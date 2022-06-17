package com.pasoftxperts.covidetect.guicontrollers.scenechanger;

import com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader.CacheFXMLLoader;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChanger
{
    // Changes a scene on the same window. Gets the event, a title and two resource names
    // of the scene (one for 1600x900 - r1, and one for 1000x600 - r2)
    public static void changeScene(Event event, String title, String r1, String r2) throws IOException
    {
        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        Parent visualizationParent;

        double width = window.getWidth();
        double height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
            visualizationParent = CacheFXMLLoader.load(r1); // load 1600x900
        else
            visualizationParent = CacheFXMLLoader.load(r2);


        window.getScene().setRoot(visualizationParent);
        window.setTitle(title);
        window.show();
    }
}
