package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator;
import com.pasoftxperts.covidetect.university.Seat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentListWindowController implements Initializable
{
    @FXML
    private VBox studentVbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        if (ClassVisualizationController.seats == null)
            return;

        List<Seat> currentSeats = ClassVisualizationController.seats;

        // Go through the labels and update them accordingly with student ids
        for (int i = 0; i < studentVbox.getChildren().size(); i++)
        {
            HBox currentHBox = (HBox) studentVbox.getChildren().get(i);

            for (int j = 0; j < currentHBox.getChildren().size(); j++)
            {
                Label currentLabel = (Label) currentHBox.getChildren().get(j);

                Seat currSeat = null;

                for (Seat seat : currentSeats)
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
