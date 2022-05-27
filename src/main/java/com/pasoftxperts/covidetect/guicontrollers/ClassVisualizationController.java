package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClassVisualizationController implements Initializable
{
    public final static int DEFAULT_ROOM_CAPACITY = 81;

    public final static int DEFAULT_SEAT_ROWS = 9;

    private ArrayList<ImageView> seatList = new ArrayList<>();

    @FXML
    private GridPane seatsGridPane;

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
        for (int i = 0; i < DEFAULT_ROOM_CAPACITY; i++)
        {
            seatList.add(new ImageView(new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/freeSeat.png"))));
            seatList.get(i).setFitWidth(25);
            seatList.get(i).setFitHeight(47);
        }

        int defaultColumns = DEFAULT_ROOM_CAPACITY / DEFAULT_SEAT_ROWS;

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(1.0 / DEFAULT_SEAT_ROWS);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(1.0 / defaultColumns);

        for (int i = 0; i < DEFAULT_SEAT_ROWS; i++)
        {
            for (int j = 0; j < defaultColumns; j++)
            {
                int seatImageIndex = GraphNeighboursGenerator.numberOfSeat(i, j, DEFAULT_ROOM_CAPACITY / DEFAULT_SEAT_ROWS) - 1;

                seatsGridPane.add(seatList.get(seatImageIndex), i, j, 1, 1);

                seatsGridPane.getColumnConstraints().add(cc);
            }

            seatsGridPane.getRowConstraints().add(rc);
        }

        seatsGridPane.setPrefHeight(400);
        seatsGridPane.setPrefWidth(400);

    }

    @FXML
    protected void openStatistics(ActionEvent event)
    {

    }

    @FXML
    protected void openSeatsView(ActionEvent event)
    {

    }

    @FXML
    protected void openUpdateCovidCase(ActionEvent event)
    {

    }
}
