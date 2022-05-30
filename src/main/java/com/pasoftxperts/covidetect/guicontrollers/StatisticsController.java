package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.filemanager.ObjectListReader;
import com.pasoftxperts.covidetect.filemanager.ObjectReader;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.time.TimeStamp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsController implements Initializable
{
    @FXML
    private Button statisticsButton;

    @FXML
    private Button updateCovidStatusButton;

    @FXML
    private Button viewSeatButton;

    @FXML
    private ComboBox roomComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox showByComboBox;

    @FXML
    private ComboBox dataCategoryComboBox;

    @FXML
    private ComboBox methodsComboBox;

    // Selected room name from room combo box list
    private String selectedRoom = null;

    // Single ObjectReader (1 Room)
    private ObjectReader objectReader = null;

    // List of ObjectReaders (All Rooms)
    private List<ObjectReader> objectReaderList = new ArrayList<>();

    // Start Date String
    private String startDateString = null;

    // End Date String
    private String endDateString = null;

    // End Date LocalDate
    private LocalDate startDate = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Make date pickers not editable
        startDatePicker.setEditable(false);
        endDatePicker.setEditable(false);

        // Read room names
        ArrayList<Object> objectList = ObjectListReader.readObjectListFile(MainApplicationController.path + "roomNames.ser");

        List<String> roomNames = objectList.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        roomNames.add("All Rooms");

        Collections.sort(roomNames);

        // Initialize room combo box
        roomComboBox.setItems(FXCollections.observableList(roomNames));


        // ADD LISTENERS
        // ROOM COMBO BOX LISTENER
        roomComboBox.valueProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1)
            {
                // Set date picker selected values to null
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);

                if (roomComboBox.getValue() == null)
                    return;

                selectedRoom = (String) roomComboBox.getValue();

                objectReaderList = new ArrayList<>();

                if (selectedRoom.equals("All Rooms"))
                {
                    for (String name : roomNames)
                    {
                        if (!name.equals("All Rooms"))
                        {
                            objectReaderList.add(new ObjectReader(MainApplicationController.path + name + ".ser"));
                            objectReaderList.get(objectReaderList.size() - 1).start();
                        }
                    }
                }
                else
                {
                    objectReader = new ObjectReader(MainApplicationController.path + selectedRoom + ".ser");
                    objectReader.start();
                }
            }
        });


        // START DATE LISTENER
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeStamp.DATE_FORMAT, Locale.US);

        startDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>()
        {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1)
            {
                if (startDatePicker.getValue() == null)
                    return;

                LocalDate startDate = startDatePicker.getValue();

                startDateString = formatter.format(startDate);
            }
        });


        // END DATE LISTENER
        endDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>()
        {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1)
            {
                if (endDatePicker.getValue() == null)
                    return;

                LocalDate endDate = endDatePicker.getValue();

                endDateString = formatter.format(endDate);

                if (startDateString == null)
                    return;

                if (endDate.isBefore(startDate))
                {
                    try
                    {
                        PopupWindow.display("End Date can't be before Start Date");
                        endDatePicker.setValue(null);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });


        // SHOW BY COMBOBOX LISTENER
        showByComboBox.valueProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1)
            {

            }
        });
    }

    @FXML
    protected void openStatistics(ActionEvent event)
    {

    }

    @FXML
    protected void openSeatsView(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        if ((MainApplicationController.width >= 1600) && (MainApplicationController.height >= 900))
            resourceName = "mainApplicationGUI-1600x900-viewSeats.fxml";
        else
            resourceName = "mainApplicationGUI-1000x600-viewSeats.fxml";

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        Scene visualizationScene = new Scene(visualizationParent, MainApplicationController.width, MainApplicationController.height);

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
