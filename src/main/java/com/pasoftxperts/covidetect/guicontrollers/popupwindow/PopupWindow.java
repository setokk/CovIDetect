package com.pasoftxperts.covidetect.guicontrollers.popupwindow;

import com.pasoftxperts.covidetect.RunApplication;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PopupWindow
{
    public static void display(String message) throws IOException
    {
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Error");
        popupWindow.centerOnScreen();
        popupWindow.setResizable(false);
        popupWindow.getIcons().add(new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));

        AnchorPane pane = new AnchorPane();

        Label errorLabel = new Label(message);
        errorLabel.setFont(Font.font("Corbel Light", FontWeight.NORMAL, 18));

        pane.getChildren().add(errorLabel);

        Scene scene = new Scene(pane, 500, 80);
        popupWindow.setScene(scene);
        popupWindow.showAndWait();

    }
}
