package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.filemanager.ObjectListReader;
import com.pasoftxperts.covidetect.filemanager.ObjectReader;
import com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator;
import com.pasoftxperts.covidetect.student.Student;
import com.pasoftxperts.covidetect.time.HourSpan;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;
import com.pasoftxperts.covidetect.university.Seat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ClassVisualizationController implements Initializable
{
    public final static int DEFAULT_ROOM_CAPACITY = 81;

    public final static int DEFAULT_SEAT_ROWS = 9;

    // Seat list for grid pane
    private ArrayList<ImageView> seatList = new ArrayList<>();

    // File reader for rooms
    private ObjectReader objectReader = null;

    // Selected Room name in combobox
    private String roomName = null;

    // Selected timestamp list (same date can have more than 1 hourspan)
    private List<TimeStamp> timeStampList = null;

    // List of hourspan names
    private List<String> hourSpanNames = null;

    @FXML
    private GridPane seatsGridPane;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button updateCovidCaseButton;

    @FXML
    private Button viewSeatButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox roomComboBox;

    @FXML
    private ComboBox hourSpanComboBox;

    @FXML
    private ImageView seatsIcon;

    @FXML
    private Label roomLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Initialize room seats
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

        // Set path
        String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/";

        // Set datepicker as not editable
        datePicker.setEditable(false);

        // Add listeners (room combo box and date picker)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeStamp.DATE_FORMAT, Locale.US);

        // ROOM COMBO LISTENER
        roomComboBox.valueProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1)
            {
                datePicker.setValue(null);
                hourSpanComboBox.setValue(null);

                // Initiliaze seats
                for (int i = 0; i < DEFAULT_ROOM_CAPACITY; i++)
                    seatList.get(i).setImage(new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/freeSeat.png")));

                roomName = (String) roomComboBox.getValue();

                if (roomName == null)
                    return;

                roomLabel.setText(roomName);

                // Open .ser file for specific room
                objectReader = new ObjectReader(path + roomName + ".ser");
                objectReader.start();
            }
        });

        // DATE PICKER LISTENER
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue)
            {
                hourSpanComboBox.setValue(null);

                // Initiliaze seats
                for (int i = 0; i < DEFAULT_ROOM_CAPACITY; i++)
                    seatList.get(i).setImage(new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/freeSeat.png")));

                if (roomName == null)
                    return;

                LocalDate date = datePicker.getValue();
                String dateToString;

                if (date != null)
                    dateToString = formatter.format(date);
                else
                    return;

                // We now search all hourspans for this specific date
                List<HourSpan> hourSpanList = new ArrayList<>();

                if (objectReader.getResult() == null)
                    return;

                Room room = (Room) objectReader.getResult();
                timeStampList = new ArrayList<>();

                for (TimeStamp timeStamp : room.getTimeStampList())
                {
                    if (timeStamp.getDateToString().equals(dateToString))
                    {
                        timeStampList.add(timeStamp);
                        hourSpanList.add(timeStamp.getDay().getHourSpan());
                    }
                }

                // List of hourspan strings
                hourSpanNames = new ArrayList<>();

                for (HourSpan hourSpan : hourSpanList)
                {
                    hourSpanNames.add(hourSpan.getStartHour() + ":00 - " + hourSpan.getEndHour() + ":00");
                }

                hourSpanComboBox.setItems(FXCollections.observableList(hourSpanNames));
            }
        });

        // HOURSPAN COMBO BOX LISTENER
        hourSpanComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1)
            {
                String hourSpanValue = (String) hourSpanComboBox.getValue();

                if (hourSpanValue == null)
                    return;

                // Parallel lists. (same index)
                TimeStamp timeStamp = null;

                for (int i = 0; i < hourSpanNames.size(); i++)
                {
                    if (hourSpanNames.get(i).equals(hourSpanValue))
                        timeStamp = timeStampList.get(i);
                }

                // Update seats according to graph
                DefaultUndirectedGraph<Seat, Integer> seatGraph = timeStamp.getSeatGraph();

                List<Seat> seats = new ArrayList<>(seatGraph.vertexSet());

                for (int i = 0; i < seats.size(); i++)
                {
                    Seat seat = seats.get(i);

                    Student student = seat.getStudent();

                    if (seats.get(i).isOccupied())
                    {
                        int index = seat.getSeatNumber() - 1;

                        if (student.getHealthIndicator() == 0)
                            seatList.get(index).setImage(new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/takenSeat.png")));
                        else if (student.getHealthIndicator() == 1)
                            seatList.get(index).setImage(new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidSeat.png")));
                        else if (student.getHealthIndicator() == 2)
                            seatList.get(index).setImage(new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/possibleCaseSeat.png")));
                    }
                }
            }
        });

        // Read room names
        ArrayList<Object> objectList = ObjectListReader.readObjectListFile(path + "roomNames.ser");

        List<String> roomNames = objectList.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        // Initialize room combo box
        roomComboBox.setItems(FXCollections.observableList(roomNames));
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
