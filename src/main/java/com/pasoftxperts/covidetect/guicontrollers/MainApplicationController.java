package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.imageslider.ImageSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainApplicationController implements Initializable
{
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button updateCovidStatusButton;

    @FXML
    private Button viewSeatButton;

    @FXML
    private ImageView seatsIcon;

    public static final String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/";

    // We need the width and height for other classes
    public static double width;

    public static double height;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ImageSlider sliderCreator = new ImageSlider();

        GridPane imageSlider = sliderCreator.createImageSlider();

        mainBorderPane.setCenter(imageSlider);
    }

    @FXML
    protected void openStatistics(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        width = window.getWidth();
        height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
        {
            resourceName = "mainApplicationGUI-1600x900-statistics.fxml";
            width = 1600;
            height = 900;
        }
        else
        {
            resourceName = "mainApplicationGUI-1000x600-statistics.fxml";
            width = 1000;
            height = 600;
        }

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        Scene visualizationScene = new Scene(visualizationParent, width, height);

        window.setScene(visualizationScene);
        window.setTitle("Statistical Analysis - CovIDetect©");

        // Deallocate memory
        visualizationParent = null;
        visualizationScene = null;
        System.gc();

        window.show();
    }

    @FXML
    protected void openSeatsView(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        width = window.getWidth();
        height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
        {
            resourceName = "mainApplicationGUI-1600x900-viewSeats.fxml";
            width = 1600;
            height = 900;
        }
        else
        {
            resourceName = "mainApplicationGUI-1000x600-viewSeats.fxml";
            width = 1000;
            height = 600;
        }

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        Scene visualizationScene = new Scene(visualizationParent, width, height);

        window.setScene(visualizationScene);
        window.setTitle("Room Visualization - CovIDetect©");

        // Deallocate memory
        visualizationParent = null;
        visualizationScene = null;
        System.gc();

        window.show();
    }

    @FXML
    protected void openUpdateCovidCase(ActionEvent event)
    {

    }
}
