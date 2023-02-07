/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is the gui controller of the student list window (the bottom right button of room visualization scene).
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator;
import com.pasoftxperts.covidetect.guicontrollers.dataholder.DataHolder;
import com.pasoftxperts.covidetect.guicontrollers.font.FontInitializer;
import com.pasoftxperts.covidetect.university.Seat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentListWindowController implements Initializable
{
    @FXML
    private AnchorPane pane;

    @FXML
    private VBox studentVbox;

    @FXML
    private Label studentWindowLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        FontInitializer.initializeFont(pane);

        // Get the instance of DataLoader singleton class
        DataHolder dataHolder = DataHolder.getInstance();
        ArrayList<Object> dataList = (ArrayList<Object>) dataHolder.getObjectData(); // In this case, we know we have an ArrayList of objects

        // Cast the appropriate types
        String date = (String) dataList.get(0);
        List<Seat> seats = (List<Seat>) dataList.get(1);

        // Check if the seats have been loaded
        if (seats == null)
            return;

        // We set the date
        studentWindowLabel.setText("Student ID List (" + date + ")");

        // Go through the labels and update them accordingly with student ids
        for (int i = 0; i < studentVbox.getChildren().size(); i++)
        {
            HBox currentHBox = (HBox) studentVbox.getChildren().get(i);

            for (int j = 0; j < currentHBox.getChildren().size(); j++)
            {
                Label currentLabel = (Label) currentHBox.getChildren().get(j);

                Seat currSeat = null;

                for (Seat seat : seats)
                {
                    if (seat.getSeatNumber() == GraphNeighboursGenerator.numberOfSeat(j, i, currentHBox.getChildren().size()))
                    {
                        currSeat = seat;
                        break;
                    }
                }

                // Set the appropriate colors
                if (currSeat.isOccupied())
                {
                    currentLabel.setText(currSeat.getStudent().getStudentId());

                    if (currSeat.getStudent().getHealthIndicator() == 0)
                        currentLabel.setTextFill(Color.GRAY);
                    if (currSeat.getStudent().getHealthIndicator() == 1)
                        currentLabel.setTextFill(Color.RED);
                    if (currSeat.getStudent().getHealthIndicator() == 2)
                        currentLabel.setTextFill(Color.YELLOW);
                }
                else
                {
                    currentLabel.setText("");
                }
            }
        }
    }
}
