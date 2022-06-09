package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.filemanager.ObjectListReader;
import com.pasoftxperts.covidetect.filemanager.ObjectReader;
import com.pasoftxperts.covidetect.filemanager.ObjectTaskReader;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.statistics.StatisticalAnalysis;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
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
    private Label homeButton;

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

    @FXML
    private Button proceedButton;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private Label minField;

    @FXML
    private Label maxField;

    @FXML
    private Label averageField;

    private XYChart.Series<String, Number> series;

    // Selected room name from room combo box list
    private String selectedRoom = null;

    // Single ObjectReader (1 Room)
    private ObjectReader objectReader = null;

    // List of ObjectReaders (All Rooms)
    private List<ObjectTaskReader> objectReaderList = new ArrayList<>();

    // Start Date LocalDate
    private LocalDate startDate = null;

    // Start Date String
    private String startDateString = null;

    // End Date String
    private String endDateString = null;

    // List for Show By ComboBox
    private List<String> showByOptions = new ArrayList<>();

    // Selected Show By Value
    private String selectedShowByOption = null;

    // List for Data Category ComboBox
    private List<String> dataCategoryOptions = new ArrayList<>();

    // Selected Data Category Value
    private String selectedDataCategory = null;

    // List for Statistical Methods ComboBox
    private List<String> statisticalMethodOptions = new ArrayList<>();

    // Selected Statistical Method Value
    private String selectedStatisticalMethod = null;

    // Show By Values (Line Chart X Axis)
    private ArrayList<String> showByElements = new ArrayList<>();

    // We use a list with a capacity of 1 so that changes made to it can be returned.
    private ArrayList<Double> statisticalMethod = new ArrayList<>(1);

    // Min, Max, Average
    private ArrayList<Double> minMaxAverage = new ArrayList<>(3);

    // Rooms List to apply statistical analysis to
    private ArrayList<Room> rooms;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Add Show By Options List to its ComboBox
        showByOptions.add("Yearly");
        showByOptions.add("Monthly");
        showByOptions.add("Weekly");
        showByOptions.add("By Day");
        showByOptions.add("Hourly");
        showByOptions.add("By Professor");

        showByComboBox.setItems(FXCollections.observableList(showByOptions));


        // Add Data Category Options List to its ComboBox
        dataCategoryOptions.add("Attendance Stats");
        dataCategoryOptions.add("Covid Cases Stats");

        dataCategoryComboBox.setItems(FXCollections.observableList(dataCategoryOptions));


        // Add Data Category Options List to its ComboBox
        statisticalMethodOptions.add("Average");
        statisticalMethodOptions.add("Max");
        statisticalMethodOptions.add("Min");

        methodsComboBox.setItems(FXCollections.observableList(statisticalMethodOptions));


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

        // Deallocate memory
        objectList = null;
        System.gc();

        // ADD LISTENERS
        // ROOM COMBO BOX LISTENER
        roomComboBox.valueProperty().addListener((observableValue, o, t1) ->
        {
            // Set date picker selected values to null
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            endDateString = null;
            startDate = null;

            if (roomComboBox.getValue() == null)
                return;

            selectedRoom = (String) roomComboBox.getValue();

            objectReaderList = new ArrayList<>();

            minField.setText("Min:");
            maxField.setText("Max:");
            averageField.setText("Average:");

            if (selectedRoom.equals("All Rooms"))
            {
                objectReader = null;

                // Start thread for read each room object file (.ser) with JavaFX Task Concurrency
                for (String name : roomNames)
                {
                    if (!name.equals("All Rooms"))
                    {
                        objectReaderList.add(new ObjectTaskReader(MainApplicationController.path + name + ".ser"));
                    }
                }

                Service readFiles = new Service()
                {
                    @Override
                    protected Task createTask()
                    {
                        return new Task<Void>()
                        {
                            @Override
                            protected Void call() throws Exception
                            {
                                for (int i = 0; i < objectReaderList.size(); i++)
                                {
                                    objectReaderList.get(i).readObjectFile();
                                }

                                return null;
                            }
                        };
                    }
                };

                readFiles.setOnSucceeded((e) -> {});

                readFiles.start();
            }
            else
            {
                objectReader = new ObjectReader(MainApplicationController.path + selectedRoom + ".ser");
                objectReader.start();
            }
        });


        // START DATE LISTENER
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM,d,yyyy", Locale.US);

        startDatePicker.valueProperty().addListener((observableValue, localDate, t1) ->
        {
            if (startDatePicker.getValue() == null)
                return;

            startDate = startDatePicker.getValue();

            // Check if Start Date is before End Date
            if ((endDatePicker.getValue() != null) && (startDate.isAfter(endDatePicker.getValue())))
            {
                startDatePicker.setValue(null);

                try {
                    PopupWindow.display("Start Date can't be after End Date");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            startDateString = formatter.format(startDate);
        });


        // END DATE LISTENER
        endDatePicker.valueProperty().addListener((observableValue, localDate, t1) ->
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
        });


        // SHOW BY COMBO BOX LISTENER
        showByComboBox.valueProperty().addListener((observableValue, o, t1) ->
        {
            if (showByComboBox.getValue() == null)
                return;

            selectedShowByOption = (String) showByComboBox.getValue();
        });


        // DATA CATEGORY COMBO BOX LISTENER
        dataCategoryComboBox.valueProperty().addListener((observableValue, o, t1) ->
        {
            if (dataCategoryComboBox.getValue() == null)
                return;

            selectedDataCategory = (String) dataCategoryComboBox.getValue();
        });


        // STATISTICAL METHODS COMBO BOX
        methodsComboBox.valueProperty().addListener((observableValue, o, t1) ->
        {
            if (methodsComboBox.getValue() == null)
                return;

            selectedStatisticalMethod = (String) methodsComboBox.getValue();
        });
    }

    @FXML
    protected void calculateStatistics()
    {
        // First check if there is any empty field left
        if ((selectedRoom == null)
                || (selectedDataCategory == null)
                || (selectedShowByOption == null)
                || (startDateString == null)
                || (endDateString == null))
            return;

        rooms = new ArrayList<>();

        if (!selectedRoom.equals("All Rooms")) // Means that the user selected only one room
        {
            rooms.add((Room) objectReader.getResult());
        }
        else
        {
            for (ObjectTaskReader reader : objectReaderList)
                rooms.add((Room) reader.getResult());
        }

        lineChart.getData().clear();
        lineChart.setTitle(selectedDataCategory);
        series = new XYChart.Series<String, Number>();


        minMaxAverage = new ArrayList<>(3);

        showByElements = new ArrayList<>();

        ArrayList<Double> yAxisData;
        StatisticalAnalysis statisticalAnalysis = new StatisticalAnalysis();

        // If we want to show Attendance Rates, we want to multiply the rates by 100
        // If we want to show Covid Cases, we don't want to multiply it by anything
        int percentageFactor;

        // Takes the "%" value if we want to show [Min,Max,Average] in Attendance Stats
        // Takes "" value if we want to show [Min,Max,Average] in Covid Cases
        String percentSymbol;

        if (selectedDataCategory.equals("Attendance Stats"))
        {
            yAxisData = statisticalAnalysis.calculateAttendanceRates(startDateString,
                    endDateString,
                    minMaxAverage,
                    selectedShowByOption,
                    showByElements,
                    rooms);

            percentageFactor = 100;
            percentSymbol = "%";
        }
        else
        {
            yAxisData = statisticalAnalysis.calculateCovidCases(startDateString,
                    endDateString,
                    minMaxAverage,
                    selectedShowByOption,
                    showByElements,
                    rooms);

            percentageFactor = 1;
            percentSymbol = "";
        }

            for (int i = 0; i < yAxisData.size(); i++)
                series.getData().add(new XYChart.Data<String, Number>(showByElements.get(i), yAxisData.get(i) * percentageFactor));

            lineChart.getData().add(series);

            // Remove the decimals
            DecimalFormat decimalFormat = new DecimalFormat(".");
            decimalFormat.setGroupingUsed(false);
            decimalFormat.setDecimalSeparatorAlwaysShown(false);

            minField.setText("Min:\n" + decimalFormat.format(minMaxAverage.get(0) * percentageFactor) + percentSymbol);
            maxField.setText("Max:\n" + decimalFormat.format(minMaxAverage.get(1) * percentageFactor) + percentSymbol);
            averageField.setText("Average:\n" + decimalFormat.format(minMaxAverage.get(2) * percentageFactor) + percentSymbol);

            if (minMaxAverage.get(1) == -1)
            {
                minField.setText("Min:\n0");
                maxField.setText("Max:\n0");
            }

            // Deallocate memory
            rooms = null;
            System.gc();
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
        rooms = null;
        System.gc();

        window.show();
    }

    @FXML
    protected void openHomePage(MouseEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        if ((MainApplicationController.width >= 1600) && (MainApplicationController.height >= 900))
            resourceName = "mainApplicationGUI-1600x900.fxml";
        else
            resourceName = "mainApplicationGUI-1000x600.fxml";

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        Scene visualizationScene = new Scene(visualizationParent, MainApplicationController.width, MainApplicationController.height);

        window.setScene(visualizationScene);
        window.setTitle("CovIDetect© by PasoftXperts");

        // Deallocate memory
        visualizationParent = null;
        visualizationScene = null;
        rooms = null;
        System.gc();

        window.show();
    }

    @FXML
    protected void openUpdateCovidCase(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        if ((MainApplicationController.width >= 1600) && (MainApplicationController.height >= 900))
            resourceName = "mainApplicationGUI-1600x900-updateStatus.fxml";
        else
            resourceName = "mainApplicationGUI-1000x600-updateStatus.fxml";

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        Scene visualizationScene = new Scene(visualizationParent, MainApplicationController.width, MainApplicationController.height);

        window.setScene(visualizationScene);
        window.setTitle("Update Student's Covid Status - CovIDetect©");

        // Deallocate memory
        visualizationParent = null;
        visualizationScene = null;
        rooms = null;
        System.gc();

        window.show();
    }
}
