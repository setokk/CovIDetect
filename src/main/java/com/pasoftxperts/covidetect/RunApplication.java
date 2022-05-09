package com.pasoftxperts.covidetect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class RunApplication extends Application
{
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("loginGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("CovIDetect Login");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}