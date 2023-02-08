/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is the GUI controller for the Statistical Analysis scene.
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.client.filemanager.ObjectReader;
import com.pasoftxperts.covidetect.client.filemanager.TaskObjectReader;
import com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader.CacheFXMLLoader;
import com.pasoftxperts.covidetect.guicontrollers.helpers.fileschecker.FilesChecker;
import com.pasoftxperts.covidetect.guicontrollers.helpers.font.FontInitializer;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.guicontrollers.helpers.scenechanger.SceneChanger;
import com.pasoftxperts.covidetect.client.history.HistoryManager;
import com.pasoftxperts.covidetect.client.history.StatisticsValues;
import com.pasoftxperts.covidetect.userhandler.loginsession.LoginSession;
import com.pasoftxperts.covidetect.client.statistics.StatisticalAnalysis;
import com.pasoftxperts.covidetect.client.university.Room;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsController implements Initializable
{
    //
    // GUI fields
    //
    @FXML
    private AnchorPane pane;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button updateCovidStatusButton;

    @FXML
    private Button viewSeatButton;

    @FXML
    private Label homeButton;

    @FXML
    private Label logoutLabel;

    @FXML
    private ImageView memberInfoIcon;

    @FXML
    private Label usernameLabel;

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

    @FXML
    private Label statMethodLabel;

    // Member info stage
    private Stage memberInfoStage = null;


    //
    // Data Fields
    //
    private XYChart.Series<String, Number> series;

    // Selected room name from room combo box list
    private String selectedRoom = null;

    // Single ObjectReader (1 Room)
    private ObjectReader objectReader = null;

    // List of ObjectReaders (All Rooms)
    private List<TaskObjectReader> objectReaderList = new ArrayList<>();

    // Start Date LocalDate
    private LocalDate startDate = null;

    // End Date LocalDate
    private LocalDate endDate = null;

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


    //
    // Listener fields
    //
    private ChangeListener roomListener;

    private ChangeListener startDateListener;

    private ChangeListener endDateListener;

    private ChangeListener showByListener;

    private ChangeListener dataCategoryListener;

    private static ChangeListener statisticalMethodListener;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        FontInitializer.initializeFont(pane);

        usernameLabel.setText("Welcome, " + LoginSession.getUsername());

        /*
        | We check if we have loaded this page from the history option or not in main application home page
        | and we also load element values after initialization has completed (better performance, faster scene switching)
        | (We use Platform.runLater() method because it has to run as soon as the fields have been initialized)
        */
        Platform.runLater(() ->
        {
            // Add Show By Options List to its ComboBox
            showByOptions.add("Year");
            showByOptions.add("Month");
            showByOptions.add("Day");
            showByOptions.add("Hour");
            showByOptions.add("Professor");

            showByComboBox.setItems(FXCollections.observableList(showByOptions));


            // Add Data Category Options List to its ComboBox
            dataCategoryOptions.add("Attendance Stats");
            dataCategoryOptions.add("Covid Cases Stats");

            dataCategoryComboBox.setItems(FXCollections.observableList(dataCategoryOptions));


            // Add Data Category Options List to its ComboBox
            statisticalMethodOptions.add("Standard Deviation");

            methodsComboBox.setItems(FXCollections.observableList(statisticalMethodOptions));


            // Make date pickers not editable
            startDatePicker.setEditable(false);
            endDatePicker.setEditable(false);


            // Read room names
            ArrayList<String> roomNames = new ArrayList<>();

            // Get list of files in main folder (simulation files)
            new File(MainApplicationController.PATH).mkdirs();
            File mainFolder = new File(MainApplicationController.PATH);

            File[] listOfFiles = mainFolder.listFiles();

            for (File file : listOfFiles)
            {
                if (file.getName().contains("Room") && file.getName().contains(".ser"))
                    roomNames.add(file.getName().substring(0, file.getName().lastIndexOf('.'))); // Remove the .ser extension
            }

            roomNames.add("All Rooms");
            Collections.sort(roomNames);

            // Initialize room combo box
            roomComboBox.setItems(FXCollections.observableList(roomNames));


            //
            // ROOM COMBO BOX LISTENER
            //
            roomListener = (observableValue, o, t1) ->
            {
                // Set date picker selected values to null (reset the values)
                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                endDateString = null;
                startDate = null;

                if (roomComboBox.getValue() == null)
                    return;

                selectedRoom = (String) roomComboBox.getValue();


                minField.setText("Min:");
                maxField.setText("Max:");
                averageField.setText("Average:");

                if (selectedRoom.equals("All Rooms"))
                {
                    objectReader = null;

                    //
                    // Load all rooms
                    //
                    for (String name : roomNames)
                    {
                        if (!name.equals("All Rooms"))
                        {
                            objectReaderList.add(new TaskObjectReader(MainApplicationController.PATH + name + ".ser"));
                        }
                    }


                    //
                    // Start services for loading each room object file (.ser) with JavaFX Task Concurrency
                    //
                    for (int i = 0; i < objectReaderList.size(); i++)
                    {
                        int finalI = i;

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
                                        objectReaderList.get(finalI).readObjectFile();

                                        return null;
                                    }
                                };
                            }
                        };

                        readFiles.start();
                    }

                }
                else
                {
                    // Load only one object
                    objectReader = new ObjectReader(MainApplicationController.PATH + selectedRoom + ".ser");
                    objectReader.start();
                }
            };

            roomComboBox.valueProperty().addListener(roomListener);


            //
            // START DATE LISTENER
            //
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM,d,yyyy", Locale.US);

            startDateListener = (observableValue, localDate, t1) ->
            {
                if (startDatePicker.getValue() == null)
                    return;

                startDate = startDatePicker.getValue();

                // Check if Start Date is after End Date
                if ((endDatePicker.getValue() != null) && (startDate.isAfter(endDatePicker.getValue())))
                {
                    startDatePicker.setValue(null);

                    try
                    {
                        PopupWindow.display("Start Date can't be after End Date");
                    }
                    catch (IOException e)
                    {
                        return;
                    }
                }

                startDateString = formatter.format(startDate);
            };

            startDatePicker.valueProperty().addListener(startDateListener);


            //
            // END DATE LISTENER
            //
            endDateListener = (observableValue, localDate, t1) ->
            {
                if (endDatePicker.getValue() == null)
                    return;

                endDate = endDatePicker.getValue();

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
                        return;
                    }
                }
            };

            endDatePicker.valueProperty().addListener(endDateListener);


            //
            // SHOW BY COMBO BOX LISTENER
            //
            showByListener = (observableValue, o, t1) ->
            {
                if (showByComboBox.getValue() == null)
                    return;

                selectedShowByOption = (String) showByComboBox.getValue();
            };

            showByComboBox.valueProperty().addListener(showByListener);


            //
            // DATA CATEGORY COMBO BOX LISTENER
            //
            dataCategoryListener = (observableValue, o, t1) ->
            {
                if (dataCategoryComboBox.getValue() == null)
                    return;

                selectedDataCategory = (String) dataCategoryComboBox.getValue();
            };

            dataCategoryComboBox.valueProperty().addListener(dataCategoryListener);


            // STATISTICAL METHODS COMBO BOX
            statisticalMethodListener = (observableValue, o, t1) ->
            {
                if (methodsComboBox.getValue() == null)
                    return;

                selectedStatisticalMethod = (String) methodsComboBox.getValue();

            };

            methodsComboBox.valueProperty().addListener(statisticalMethodListener);


            //
            // True, if we have selected an option from the history list
            //
            if (HistoryManager.isSelectedHistoryStatus())
            {
                StatisticsValues statisticsValues = (StatisticsValues) HistoryManager.readHistory(MainApplicationController.selectedHistoryOption);

                // Read StatisticsValues class comments to understand functionality

                // Pass on the array from StatisticsValues object
                ArrayList<Object> fieldValues = statisticsValues.getFieldValues();

                roomComboBox.setValue(fieldValues.get(0));
                roomComboBox.fireEvent(new ActionEvent()); // Fire roomComboBox event to load the rooms

                startDatePicker.setValue((LocalDate) fieldValues.get(1));
                endDatePicker.setValue((LocalDate) fieldValues.get(2));
                showByComboBox.setValue(fieldValues.get(3));
                dataCategoryComboBox.setValue(fieldValues.get(4));
                methodsComboBox.setValue(fieldValues.get(5));
                minField.setText((String) fieldValues.get(6));
                maxField.setText((String) fieldValues.get(7));
                averageField.setText((String) fieldValues.get(8));
                statMethodLabel.setText((String) fieldValues.get(9));


                // Populate LineChart
                ArrayList<Double> yAxis = (ArrayList<Double>) fieldValues.get(10);
                showByElements = (ArrayList<String>) fieldValues.get(11);

                int percentageFactor;
                String seriesName;

                if (dataCategoryComboBox.getValue().equals("Attendance Stats"))
                {
                    percentageFactor = 100;
                    seriesName = "Attendance Rates";
                }
                else
                {
                    percentageFactor = 1;
                    seriesName = "Number of Covid Cases";
                }

                lineChart.getData().clear();
                lineChart.setTitle(selectedDataCategory);
                series = new XYChart.Series<String, Number>();

                for (int i = 0; i < yAxis.size(); i++)
                    series.getData().add(new XYChart.Data<String, Number>(showByElements.get(i), yAxis.get(i) * percentageFactor));

                series.setName(seriesName);

                lineChart.getData().add(series);

                //
                // Reset the status again
                //
                HistoryManager.setSelectedHistoryStatus(false);
            }

            // Check if simulation files exist
            if (!FilesChecker.checkSimulationFiles())
            {
                try
                {
                    PopupWindow.display("Simulation files not found. Please run simulation from the home page.");
                } catch (IOException e) {
                    System.exit(1);
                }
            }
        });
    }


    /*
    |  Calculates the statistics based on user input
    |  Checks if any of the necessary fields are null
    |  Based on what room a user selected, it loads the appropriate room/all rooms using Services (JavaFX Concurrency for Threading)
    |  All rooms are not loaded in whatever case for reducing space complexity and increasing performance
    |  No further field checking occurs in this method due to the value listeners and their checks in the initialize method (see line 135)
    |  Do not change the Services
    */
    @FXML
    protected void calculateStatistics()
    {
        // First check if there is any empty field left
        if ((selectedRoom == null)
                || (selectedDataCategory == null)
                || (selectedShowByOption == null)
                || (startDateString == null)
                || (endDateString == null)
                || selectedStatisticalMethod == null)
            return;

        // Rooms List to apply statistical analysis to
        ArrayList<Room> rooms = new ArrayList<>();


        // Load the Rooms
        if (!selectedRoom.equals("All Rooms")) // Means that the user selected only one room
        {
            rooms.add((Room) objectReader.getResult().orElse(new Room("No Room Loaded", 15, 5)));
        }
        else
        {
            for (TaskObjectReader reader : objectReaderList)
                rooms.add((Room) reader.getResult().orElse(new Room("No Room Loaded", 15, 5)));
        }


        // Clear line chart data
        lineChart.getData().clear();
        lineChart.setTitle(selectedDataCategory);
        series = new XYChart.Series<String, Number>();

        // Initialize statistical data
        ArrayList<Double> yAxisData;
        showByElements = new ArrayList<>();
        double min;
        double max;
        double average;

        StatisticalAnalysis statisticalAnalysis = new StatisticalAnalysis();

        // If we want to show Attendance Rates, we want to multiply the rates by 100
        // If we want to show Covid Cases, we don't want to multiply it by anything
        int percentageFactor;

        String seriesName;

        // Takes the "%" value if we want to show [Min,Max,Average] in Attendance Stats
        // Takes "" value if we want to show [Min,Max,Average] in Covid Cases
        String percentSymbol;

        if (selectedDataCategory.equals("Attendance Stats"))
        {
            yAxisData = statisticalAnalysis.calculateAttendanceRates(startDateString,
                    endDateString,
                    selectedShowByOption,
                    showByElements,
                    rooms);

            percentageFactor = 100;
            percentSymbol = "%";
            seriesName = "Attendance Stats";
        }
        else
        {
            yAxisData = statisticalAnalysis.calculateCovidCases(startDateString,
                    endDateString,
                    selectedShowByOption,
                    showByElements,
                    rooms);

            percentageFactor = 1;
            percentSymbol = "";
            seriesName = "Number of Covid Cases";
        }

        // Find min, max and average
        min = statisticalAnalysis.calculateMin(yAxisData);
        max = statisticalAnalysis.calculateMax(yAxisData);
        average = statisticalAnalysis.calculateAverage(yAxisData);

        for (int i = 0; i < yAxisData.size(); i++)
            series.getData().add(new XYChart.Data<String, Number>(showByElements.get(i), yAxisData.get(i) * percentageFactor));

        series.setName(seriesName);
        lineChart.getData().add(series);

        // Remove the decimals
        DecimalFormat decimalFormat = new DecimalFormat(".");
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setDecimalSeparatorAlwaysShown(false);


        //
        // In case ANY of the min, max, average and standard deviation fields are 0, we remove the '%' symbol, if any
        //
        // Min
        if (min == 0)
            minField.setText("Min:\n" + decimalFormat.format(min * percentageFactor));
        else
            minField.setText("Min:\n" + decimalFormat.format(min * percentageFactor) + percentSymbol);

        // Max
        if (max == 0)
            maxField.setText("Max:\n" + decimalFormat.format(max * percentageFactor));
        else
            maxField.setText("Max:\n" + decimalFormat.format(max * percentageFactor) + percentSymbol);

        // Average
        if (average == 0)
            averageField.setText("Average:\n" + decimalFormat.format(average * percentageFactor));
        else
            averageField.setText("Average:\n" + decimalFormat.format(average * percentageFactor) + percentSymbol);


        //
        // Calculate statistical method
        //
        double statMethodResult = 0;

        if (selectedStatisticalMethod.equals("Standard Deviation"))
            statMethodResult = statisticalAnalysis.calculateStandardDeviation(yAxisData, average, percentageFactor);

        // Update statistical method label

        // Standard Deviation
        if (statMethodResult == 0)
            statMethodLabel.setText(selectedStatisticalMethod + ":\n" + decimalFormat.format(statMethodResult));
        else
            statMethodLabel.setText(selectedStatisticalMethod + ":\n" + decimalFormat.format(statMethodResult) + percentSymbol);



        //
        // Update History
        // CAREFUL, add in appropriate ORDER (see StatisticsValues class)
        //
        StatisticsValues statisticsValues = new StatisticsValues();
        statisticsValues.addValue(selectedRoom);
        statisticsValues.addValue(startDate);
        statisticsValues.addValue(endDate);
        statisticsValues.addValue(selectedShowByOption);
        statisticsValues.addValue(selectedDataCategory);
        statisticsValues.addValue(selectedStatisticalMethod);
        statisticsValues.addValue(minField.getText());
        statisticsValues.addValue(maxField.getText());
        statisticsValues.addValue(averageField.getText());
        statisticsValues.addValue(statMethodLabel.getText());
        statisticsValues.addValue(yAxisData);
        statisticsValues.addValue(showByElements);

        // Write to file
        HistoryManager.updateHistory(statisticsValues);
    }

    @FXML
    protected void openStatistics(ActionEvent event)
    {

    }

    @FXML
    protected void openSeatsView(ActionEvent event) throws IOException
    {
        SceneChanger.changeScene(event,
                "Room Visualization - CovIDetect©",
                "mainApplicationGUI-1600x900-viewSeats.fxml",
                "mainApplicationGUI-1000x600-viewSeats.fxml");
    }

    @FXML
    protected void openHomePage(MouseEvent event) throws IOException
    {
        SceneChanger.changeScene(event,
                "CovIDetect© by PasoftXperts",
                "mainApplicationGUI-1600x900.fxml",
                "mainApplicationGUI-1000x600.fxml");
    }

    @FXML
    protected void openUpdateCovidCase(ActionEvent event) throws IOException
    {
        SceneChanger.changeScene(event,
                "Update Student's Covid Status - CovIDetect©",
                "mainApplicationGUI-1600x900-updateStatus.fxml",
                "mainApplicationGUI-1000x600-updateStatus.fxml");

    }

    @FXML
    protected void logout(MouseEvent event) throws IOException
    {
        LoginSession.resetSession();

        Stage stage = new Stage();

        Parent parent = CacheFXMLLoader.load("loginGUI.fxml");
        Scene scene = new Scene(parent, 600, 500);

        stage.setScene(scene);
        stage.setTitle("CovIDetect Login");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));
        stage.setResizable(false);

        // Get previous window and hide it
        Stage previousWindow = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        previousWindow.hide();

        stage.show();
    }

    @FXML
    protected void openMemberInfoWindow() throws IOException
    {
        // Can't open window more than once
        if (memberInfoStage != null)
            return;

        memberInfoStage = new Stage();

        Parent parent = CacheFXMLLoader.load("membersInfoWindow.fxml");
        Scene scene = new Scene(parent);

        memberInfoStage.setScene(scene);
        memberInfoStage.setAlwaysOnTop(true);
        memberInfoStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));
        memberInfoStage.setResizable(false);
        memberInfoStage.setTitle("Project Members Info");
        memberInfoStage.setOnCloseRequest(windowEvent ->
        {
            memberInfoStage.hide();
            memberInfoStage = null;
        });

        memberInfoStage.show();
    }
}
