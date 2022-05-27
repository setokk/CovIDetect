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
    private Button updateCovidCaseButton;

    @FXML
    private Button viewSeatButton;

    @FXML
    private ImageView seatsIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ImageSlider sliderCreator = new ImageSlider();

        GridPane imageSlider = sliderCreator.createImageSlider();

        mainBorderPane.setCenter(imageSlider);
    }

    @FXML
    protected void openStatistics(ActionEvent event)
    {

    }

    @FXML
    protected void openSeatsView(ActionEvent event) throws IOException
    {
        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource("mainApplicationGUI-1600x900-viewSeats.fxml"));
        Scene visualizationScene = new Scene(visualizationParent, 1600, 900);

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        window.setScene(visualizationScene);
        window.setTitle("Class Visualization - CovIDetect©");

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
