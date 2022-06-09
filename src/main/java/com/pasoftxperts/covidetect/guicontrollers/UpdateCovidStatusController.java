package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.filemanager.FileWrapper;
import com.pasoftxperts.covidetect.filemanager.ObjectListReader;
import com.pasoftxperts.covidetect.filemanager.ObjectTaskReader;
import com.pasoftxperts.covidetect.graphanalysis.SingleCaseNeighbourCalculator;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;
import com.pasoftxperts.covidetect.university.Seat;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator.isCovidCase;
import static com.pasoftxperts.covidetect.guicontrollers.ClassVisualizationController.DEFAULT_ROOM_CAPACITY;
import static com.pasoftxperts.covidetect.guicontrollers.ClassVisualizationController.DEFAULT_SEAT_ROWS;

public class UpdateCovidStatusController implements Initializable
{
    @FXML
    private Button statisticsButton;

    @FXML
    private Button updateCovidStatusButton;

    @FXML
    private Button viewSeatButton;

    @FXML
    private ImageView seatsIcon;

    @FXML
    private Label homeButton;

    @FXML
    private Button updateButton;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField studentField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lastUpdatedLabel;

    // Object Reader Threads List for reading rooms
    private List<ObjectTaskReader> objectReaderList;

    // Path to write the last updated file to
    private String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/lastupdate/";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Initialize the last update label
        File lastUpdated = new File(path + "date.txt");
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(lastUpdated));

            String lastUpdate = bufferedReader.readLine();
            lastUpdatedLabel.setText("Last Updated: " + lastUpdate);

        } catch (IOException ex) { lastUpdatedLabel.setText("Last Updated: "); }

        // Set date picker to not editable
        datePicker.setEditable(false);

        studentField.textProperty().addListener((observableValue, s, t1) ->
        {
            statusLabel.setText("");
        });

        datePicker.valueProperty().addListener((observableValue, localDate, t1) ->
        {
            statusLabel.setText("");
        });

        // Load All Rooms
        ArrayList<Object> objectList = ObjectListReader.readObjectListFile(MainApplicationController.path + "roomNames.ser");

        List<String> roomNames = objectList.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        // Create and start Threads using JavaFX service threads
        objectReaderList = new ArrayList<>();

        for (String name : roomNames)
        {
            objectReaderList.add(new ObjectTaskReader(MainApplicationController.path + name + ".ser"));
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

        // Deallocate memory
        objectList = null;
        roomNames = null;
        System.gc();
    }

    @FXML
    protected void updateCovidStatus()
    {
        // Check if student id field has input
        if (studentField.getText().equals(""))
        {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Please enter a Student ID");
            return;
        }

        // Check if date field has input
        if (datePicker.getValue() == null)
        {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Please pick a Target Date");
            return;
        }

        // Date Formatter to create a TimeStamp Object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM,d,yyyy", Locale.US);
        String targetDate = formatter.format(datePicker.getValue());

        TimeStamp targetTimeStamp = new TimeStamp(targetDate);

        // Get Results from Threads
        ArrayList<Room> rooms = new ArrayList<>();

        for (ObjectTaskReader objectReader : objectReaderList)
        {
            rooms.add((Room) objectReader.getResult());
        }


        // Find if the student exists,
        // and if they exist,
        // update the possible cases that surround him for the previous 5 days in every room
        String studentId = studentField.getText();

        // Indicates whether the student was found or not
        boolean found = false;

        // Indicates the number of times targetTimeStamp has failed to be in bounds for any room
        // If it equals the number of rooms in total, it means the targetTimeStamp is out of bounds.
        int failedAttempts = 0;

        // Arraylist to save the rooms in which we found the student
        // (improves performance because we don't save ALL rooms after updating the status)
        ArrayList<Room> modifiedRooms = new ArrayList<>();

        for (int i = 0; i < rooms.size(); i++)
        {
            Room room = rooms.get(i);

            ArrayList<TimeStamp> timeStamps = room.getTimeStampList();

            // Count as failed attempt to find the student
            if (timeStamps.size() == 0)
                failedAttempts++;

            int firstIndex = 0;
            int lastIndex = timeStamps.size() - 1;

            for (int j = 0; j < timeStamps.size(); j++)
            {
                TimeStamp currentTimeStamp = timeStamps.get(j);

                // Check if target timestamp is in timestamps list
                if ((targetTimeStamp.isAfter(timeStamps.get(lastIndex))) || (targetTimeStamp.isBefore(timeStamps.get(firstIndex))))
                {
                    failedAttempts++;
                    break;
                }

                // We now are at the target date
                if (currentTimeStamp.equals(targetTimeStamp))
                {
                    ArrayList<Seat> seats = new ArrayList<>(currentTimeStamp.getSeatGraph().vertexSet());

                    for (int k = 0; k < seats.size(); k++)
                    {
                        Seat seat = seats.get(k);

                        if (seat.isOccupied() && seat.getStudent().getStudentId().equals(studentId))
                        {
                            found = true;

                            if (seat.getStudent().getHealthIndicator() == 1)
                            {
                                statusLabel.setTextFill(Color.RED);
                                statusLabel.setText("Student " + studentId + " is already\n registered as a covid case");
                                return;
                            }
                            else
                            {
                                seat.getStudent().setHealthIndicator(1);
                                DefaultUndirectedGraph<Seat, Integer> graph = SingleCaseNeighbourCalculator.calculateSingleCaseNeighbours
                                        (seat,
                                                seats,
                                                DEFAULT_SEAT_ROWS,
                                                DEFAULT_ROOM_CAPACITY/ DEFAULT_SEAT_ROWS);

                                currentTimeStamp.addSeatGraph(graph);

                                if (!modifiedRooms.contains(room))
                                    modifiedRooms.add(room);
                            }
                        }
                    }

                    break;
                }
            }
        }

        // Target date not found
        if (failedAttempts == rooms.size())
        {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Target Date was not found");
            return;
        }

        // Student was not found
        if (!found)
        {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Student " + studentId + " was\n not found");
            return;
        }

        // We now check for the student for the past 5 days in every room
        for (Room room : rooms)
        {
            ArrayList<TimeStamp> timeStamps = room.getTimeStampList();

            for (int j = 0; j < timeStamps.size(); j++)
            {
                TimeStamp currentTimeStamp = timeStamps.get(j);

                // Break from loop. We only want to check before that timestamp
                if (currentTimeStamp.isAfter(targetTimeStamp) || currentTimeStamp.equals(targetTimeStamp))
                    break;

                int daysBefore = dayDistanceBetween(currentTimeStamp, targetTimeStamp);

                // We are 5 days or less before
                if (daysBefore <= 5)
                {
                    ArrayList<Seat> seats = new ArrayList<>(currentTimeStamp.getSeatGraph().vertexSet());

                    for (int k = 0; k < seats.size(); k++)
                    {
                        Seat seat = seats.get(k);

                        if (seat.isOccupied() && seat.getStudent().getStudentId().equals(studentId))
                        {
                            if (!isCovidCase(seat.getStudent()))
                            {
                                seat.getStudent().setHealthIndicator(1);
                                DefaultUndirectedGraph<Seat, Integer> graph = SingleCaseNeighbourCalculator.calculateSingleCaseNeighbours
                                                                                            (seat,
                                                                                            seats,
                                                                                            DEFAULT_SEAT_ROWS,
                                                                                       DEFAULT_ROOM_CAPACITY/ DEFAULT_SEAT_ROWS);

                                currentTimeStamp.addSeatGraph(graph);

                                if (!modifiedRooms.contains(room))
                                    modifiedRooms.add(room);
                            }
                        }
                    }
                }
            }
        }

        // Thread to save only the rooms that were modified (modifiedRooms)
        Service writeRooms = new Service()
        {
            @Override
            protected Task createTask()
            {
                return new Task<Void>()
                {
                    @Override
                    protected Void call() throws Exception
                    {
                        FileWrapper.saveFilesByRoom("university of macedonia", "applied informatics", modifiedRooms);

                        return null;
                    }
                };
            }
        };

        writeRooms.setOnSucceeded((e) -> {});

        writeRooms.start();

        // Get date
        LocalDateTime localDate = LocalDateTime.now();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
        lastUpdatedLabel.setText("Last Updated: " + formattedDate);

        // Write to file
        // Make dir
        new File(path).mkdirs();

        File lastUpdated = new File(path + "date.txt");
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(lastUpdated));

            bufferedWriter.write(formattedDate);

            bufferedWriter.close();

        } catch (IOException ex) { ex.printStackTrace(); }


        studentField.setText("");
        datePicker.setValue(null);
        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setText("Student covid case was\nsuccessfully updated");
    }

    @FXML
    protected void openUpdateCovidCase(ActionEvent event)
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
        objectReaderList = null;
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
        objectReaderList = null;
        System.gc();

        window.show();
    }

    @FXML
    protected void openStatistics(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        if ((MainApplicationController.width >= 1600) && (MainApplicationController.height >= 900))
            resourceName = "mainApplicationGUI-1600x900-statistics.fxml";
        else
            resourceName = "mainApplicationGUI-1000x600-statistics.fxml";

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        Scene visualizationScene = new Scene(visualizationParent, MainApplicationController.width, MainApplicationController.height);

        window.setScene(visualizationScene);
        window.setTitle("Statistical Analysis - CovIDetect©");

        // Deallocate memory
        visualizationParent = null;
        visualizationScene = null;
        objectReaderList = null;
        System.gc();

        window.show();
    }

    // Calculates the backwards day distance between currentTimeStamp and targetTimeStamp
    // Returns 100000 (arbitrary) if the days before are more than a month's length. (We only care about 1 or 0 month/year difference between the dates)
    // We use it to see if we are 5 days before the target timestamp to properly update the
    // covid status of the student, if they exist in that room and time.
    protected int dayDistanceBetween(TimeStamp currentTimeStamp, TimeStamp targetTimeStamp)
    {
        int daysBefore = 100000;

        int currYear = currentTimeStamp.getYear();
        Month currMonth = currentTimeStamp.getMonth();
        int currDay = currentTimeStamp.getDay().getDayNumber();

        int targetYear = targetTimeStamp.getYear();
        Month targetMonth = targetTimeStamp.getMonth();
        int targetDay = targetTimeStamp.getDay().getDayNumber();

        // If same year, same month
        if ((currYear == targetYear) && (currMonth.getValue() == targetMonth.getValue()))
            daysBefore = targetDay - currDay;

        // If 1 month difference and either same year or next year
        if (targetMonth.getValue() - currMonth.getValue() == 1)
        {
            if ((targetYear == currYear) || (targetYear - currYear == 1))
            {
                daysBefore = currMonth.length((currYear%4==0) || (currYear%400==0)) - currDay + targetDay;
            }
        }

        return daysBefore;
    }
}
