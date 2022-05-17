package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainApplicationController implements Initializable
{
    @FXML
    private Button statisticsButton;

    @FXML
    private Button updateCovidCaseButton;

    @FXML
    private Button viewSeatButton;

    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        datePicker.setEditable(false);
    }

    @FXML
    protected void openStatistics(ActionEvent event)
    {

    }

    @FXML
    protected void openSeatsView(ActionEvent event) throws IOException
    {
        Parent seatsParent = FXMLLoader.load(RunApplication.class.getResource("seatsViewGUI.fxml"));
        Scene seatsScene = new Scene(seatsParent, 1600, 900);

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        window.setScene(seatsScene);
        window.setTitle("Room Seats View - CovIDetect©");

        // Deallocate memory
        seatsParent = null;
        seatsScene = null;
        System.gc();

        window.show();
    }

    @FXML
    protected void openUpdateCovidCase(ActionEvent event)
    {

    }
}
