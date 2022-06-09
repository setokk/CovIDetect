package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.filemanager.ObjectListReader;
import com.pasoftxperts.covidetect.filemanager.ObjectReader;
import com.pasoftxperts.covidetect.imageslider.ImageSlider;
import com.pasoftxperts.covidetect.loginsession.LoginSession;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    @FXML
    private Label usernameLabel;

    @FXML
    private Label logoutLabel;

    public static final String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/";

    // We need the width and height for other classes
    public static double width;

    public static double height;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        usernameLabel.setText(LoginSession.getUsername());

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
    protected void openUpdateCovidCase(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        width = window.getWidth();
        height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
        {
            resourceName = "mainApplicationGUI-1600x900-updateStatus.fxml";
            width = 1600;
            height = 900;
        }
        else
        {
            resourceName = "mainApplicationGUI-1000x600-updateStatus.fxml";
            width = 1000;
            height = 600;
        }

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        Scene visualizationScene = new Scene(visualizationParent, width, height);

        window.setScene(visualizationScene);
        window.setTitle("Update Student's Covid Status - CovIDetect© - CovIDetect©");

        // Deallocate memory
        visualizationParent = null;
        visualizationScene = null;
        System.gc();

        window.show();
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException
    {
        LoginSession.resetSession();

        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("loginGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("CovIDetect Login");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));
        stage.setScene(scene);
        stage.setResizable(false);

        // Get previous window and hide it
        Stage previousWindow = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        previousWindow.hide();

        stage.show();
    }
}
