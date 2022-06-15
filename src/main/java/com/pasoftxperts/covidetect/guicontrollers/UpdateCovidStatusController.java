/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 |
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.filemanager.FileWrapper;
import com.pasoftxperts.covidetect.filemanager.TaskObjectReader;
import com.pasoftxperts.covidetect.graphanalysis.SingleCaseNeighbourCalculator;
import com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader.CacheFXMLLoader;
import com.pasoftxperts.covidetect.loginsession.LoginSession;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;
import com.pasoftxperts.covidetect.university.Seat;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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

import static com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator.isCovidCase;
import static com.pasoftxperts.covidetect.guicontrollers.RoomVisualizationController.DEFAULT_ROOM_CAPACITY;
import static com.pasoftxperts.covidetect.guicontrollers.RoomVisualizationController.DEFAULT_SEAT_ROWS;

public class UpdateCovidStatusController implements Initializable
{

    public static final int DAYS_TO_LOOK_BACK = 2; // Number of days to look back for possible cases after finding a covid case

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
    private Label logoutLabel;

    @FXML
    private Label usernameLabel;

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
    private List<TaskObjectReader> objectReaderList = new ArrayList<>();

    private ArrayList<String> roomNames; // Room names

    List<Room> rooms = new ArrayList<>(); // All rooms available

    // Arraylist to save the rooms in which we found the student
    // (improves performance because we don't save ALL rooms after updating the status)
    List<Room> modifiedRooms = new ArrayList<>();

    private List<Seat> seats; // Seat list

    private List<Service> readFiles = new ArrayList<>();

    // Date Formatter to create a TimeStamp Object
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM,d,yyyy", Locale.US);

    // Path to write the last updated file to
    private static String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/lastupdate/";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        usernameLabel.setText("Welcome, " + LoginSession.getUsername());

        // Using platform.runLater() to initialize all the fields once the initialize phase has finished (faster scene transitions)
        Platform.runLater(() ->
        {
            //
            // Update button enter event listener
            //
            Scene scene = updateButton.getScene();

            scene.setOnKeyPressed((keyEvent ->
            {
                if (keyEvent.getCode() == KeyCode.ENTER)
                    updateButton.fire();
            }));


            // Initialize the last update label
            File lastUpdated = new File(path + "date.txt");

            try
            {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(lastUpdated));

                lastUpdatedLabel.setText("Last Updated: " + bufferedReader.readLine());

                bufferedReader.close();

            } catch (IOException ex) { lastUpdatedLabel.setText("Last Updated: "); }


            // Set date picker to not editable
            datePicker.setEditable(false);

            studentField.textProperty().addListener((observableValue, s, t1) -> statusLabel.setText(""));

            datePicker.valueProperty().addListener((observableValue, localDate, t1) -> statusLabel.setText(""));

            // Load All Rooms
            TaskObjectReader taskObjectReader = new TaskObjectReader(MainApplicationController.path + "roomNames.ser");
            taskObjectReader.readObjectFile();

            roomNames = (ArrayList<String>) taskObjectReader.getResult();


            // Create and start Threads using JavaFX service threads
            for (String name : roomNames)
                objectReaderList.add(new TaskObjectReader(MainApplicationController.path + name + ".ser"));

            //
            // Start services for loading each room object file (.ser) with JavaFX Task Concurrency
            //
            for (int i = 0; i < objectReaderList.size(); i++)
            {
                int finalI = i;

                readFiles.add(new Service() {
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
                });

                readFiles.get(i).start();
            }
        });
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


        // Target date
        TimeStamp targetTimeStamp = new TimeStamp(formatter.format(datePicker.getValue()));


        // Get Results from Threads
        for (TaskObjectReader objectReader : objectReaderList)
            rooms.add((Room) objectReader.getResult());


        // Find if the student exists,
        // and if they exist,
        // update the possible cases that surround him for the previous 2 days in every room
        String studentId = studentField.getText();


        // Indicates whether the student was found or not
        boolean found = false;

        // Indicates the number of times targetTimeStamp has failed to be in bounds for any room
        // If it equals the number of rooms in total, it means the targetTimeStamp is out of bounds.
        int failedAttempts = 0;

        // We now check for the student for the past 2 days in every room
        for (Room room : rooms)
        {
            ArrayList<TimeStamp> timeStamps = room.getTimeStampList();

            // Count as failed attempt to find the student
            if (timeStamps.size() == 0)
                failedAttempts++;

            int firstIndex = 0;
            int lastIndex = room.getTimeStampList().size() - 1;

            for (int j = 0; j < timeStamps.size(); j++)
            {
                // Check if target timestamp is in timestamps list
                if ((targetTimeStamp.isAfter(room.getTimeStampList().get(lastIndex)))
                        || (targetTimeStamp.isBefore(room.getTimeStampList().get(firstIndex))))
                {
                    failedAttempts++;
                    break;
                }

                int daysBefore = dayDistanceBetween(timeStamps.get(j), targetTimeStamp);

                // We are 2 days or less before
                if (daysBefore <= DAYS_TO_LOOK_BACK)
                {
                    seats = new ArrayList<>(timeStamps.get(j).getSeatGraph().vertexSet());

                    for (int k = 0; k < seats.size(); k++)
                    {
                        if (seats.get(k).isOccupied() && seats.get(k).getStudent().getStudentId().equals(studentId))
                        {
                            found = true;

                            if (!isCovidCase(seats.get(k).getStudent()))
                            {
                                seats.get(k).getStudent().setHealthIndicator(1);

                                DefaultUndirectedGraph<Seat, Integer> graph = SingleCaseNeighbourCalculator.calculateSingleCaseNeighbours
                                                                                            (seats.get(k),
                                                                                            seats,
                                                                                            DEFAULT_SEAT_ROWS,
                                                                                       DEFAULT_ROOM_CAPACITY/ DEFAULT_SEAT_ROWS);

                                timeStamps.get(j).addSeatGraph(graph);

                                if (!modifiedRooms.contains(room))
                                    modifiedRooms.add(room);
                            }
                        }
                    }

                    seats.clear();
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


        //
        // Thread to save only the rooms that were modified (modifiedRooms)
        //
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

        writeRooms.start();

        // Get date
        lastUpdatedLabel.setText("Last Updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")));

        // Write to file
        // Make dir
        new File(path).mkdirs();

        File lastUpdated = new File(path + "date.txt");
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(lastUpdated));

            bufferedWriter.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")));

            bufferedWriter.close();

        } catch (IOException ex) { return; }


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

        double width = window.getWidth();
        double height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
            resourceName = "mainApplicationGUI-1600x900-viewSeats.fxml";
        else
            resourceName = "mainApplicationGUI-1000x600-viewSeats.fxml";

        Parent visualizationParent = CacheFXMLLoader.load(resourceName);
        window.getScene().setRoot(visualizationParent);

        window.setTitle("Room Visualization - CovIDetect©");

        window.show();

        // Dereference objects
        readFiles = null;
        modifiedRooms = null;
        rooms = null;
        objectReaderList = null;

        System.gc();
    }

    @FXML
    protected void openHomePage(MouseEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        double width = window.getWidth();
        double height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
            resourceName = "mainApplicationGUI-1600x900.fxml";
        else
            resourceName = "mainApplicationGUI-1000x600.fxml";

        Parent visualizationParent = CacheFXMLLoader.load(resourceName);
        window.getScene().setRoot(visualizationParent);

        window.setTitle("CovIDetect© by PasoftXperts");

        window.show();

        // Dereference objects
        readFiles = null;
        modifiedRooms = null;
        rooms = null;
        objectReaderList = null;

        System.gc();
    }

    @FXML
    protected void openStatistics(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        double width = window.getWidth();
        double height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
            resourceName = "mainApplicationGUI-1600x900-statistics.fxml";
        else
            resourceName = "mainApplicationGUI-1000x600-statistics.fxml";

        Parent visualizationParent = CacheFXMLLoader.load(resourceName);
        window.getScene().setRoot(visualizationParent);

        window.setTitle("Statistical Analysis - CovIDetect©");

        window.show();

        // Dereference objects
        readFiles = null;
        modifiedRooms = null;
        rooms = null;
        objectReaderList = null;

        System.gc();
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException
    {
        LoginSession.resetSession();

        Stage stage = new Stage();

        Parent parent = CacheFXMLLoader.load("loginGUI.fxml");
        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.setTitle("CovIDetect Login");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));
        stage.setResizable(false);

        // Get previous window and hide it
        Stage previousWindow = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        previousWindow.hide();

        stage.show();

        // Dereference objects
        readFiles = null;
        modifiedRooms = null;
        rooms = null;
        objectReaderList = null;

        System.gc();
    }

    /*
    |  Calculates the backwards day distance between currentTimeStamp and targetTimeStamp
    |  Returns 100000 (arbitrary) if the days before are more than a month's length. (We only care about 1 or 0 month/year difference between the dates)
    |  We use it to see if we are 5 days before the target timestamp to properly update the
    |  covid status of the student, if they exist in that room and time.
    */
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
