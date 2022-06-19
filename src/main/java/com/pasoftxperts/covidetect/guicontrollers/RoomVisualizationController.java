/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is the GUI controller of the room visualization scene.
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.course.Course;
import com.pasoftxperts.covidetect.filemanager.ObjectReader;
import com.pasoftxperts.covidetect.counters.CovidCasesCounter;
import com.pasoftxperts.covidetect.counters.FreeSeatsCounter;
import com.pasoftxperts.covidetect.counters.PossibleCasesCounter;
import com.pasoftxperts.covidetect.counters.StudentCounter;
import com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader.CacheFXMLLoader;
import com.pasoftxperts.covidetect.guicontrollers.dataholder.DataHolder;
import com.pasoftxperts.covidetect.guicontrollers.fileschecker.FilesChecker;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.guicontrollers.scenechanger.SceneChanger;
import com.pasoftxperts.covidetect.loginsession.LoginSession;
import com.pasoftxperts.covidetect.student.Student;
import com.pasoftxperts.covidetect.time.HourSpan;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;
import com.pasoftxperts.covidetect.university.Seat;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator.numberOfSeat;

public class RoomVisualizationController implements Initializable
{
    public final static int DEFAULT_ROOM_CAPACITY = 81;

    public final static int DEFAULT_SEAT_ROWS = 9;

    @FXML
    private GridPane seatsGridPane;

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
    private DatePicker datePicker;

    @FXML
    private ComboBox roomComboBox;

    @FXML
    private ComboBox hourSpanComboBox;

    @FXML
    private ImageView seatsIcon;

    @FXML
    private Label roomLabel;

    @FXML
    private Label courseLabel;

    @FXML
    private Label dateLabel;

    // General Room Info
    @FXML
    private Label totalStudents;

    @FXML
    private Label covidCases;

    @FXML
    private Label possibleCases;

    @FXML
    private Label freeSeats;

    @FXML
    private Button studentListButton;

    // Member info stage
    private Stage memberInfoStage = null;

    //
    // Non GUI fields
    //

    // Student ID List Window
    private Stage studentListWindow;

    // Seat list from graph set
    private static List<Seat> seats = null;

    // Seat list for grid pane
    private ArrayList<ImageView> seatList = new ArrayList<>();

    // File reader for rooms
    private ObjectReader objectReader = null;

    // Selected Room name in ComboBox
    private String roomName = null;

    private List<TimeStamp> timeStampList = null;

    // List of HourSpan names
    private List<String> hourSpanNames = null;

    // Selected Room
    private Room room;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        usernameLabel.setText("Welcome, " + LoginSession.getUsername());

        // Set width and height of a seat based on screen resolution
        int widthRatio;
        int heightRatio;

        // Get default monitor resolution
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        if (width >= 1600 && height >= 900)
        {
            widthRatio = 25;
            heightRatio = 53;
        }
        else
        {
            widthRatio = 16;
            heightRatio = 32;
        }

        // Create a single free seat image (optimizing runtime)
        Image freeSeatImage = new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/freeSeat.png"));

        for (int i = 0; i < DEFAULT_ROOM_CAPACITY; i++)
        {
            ImageView freeSeat = new ImageView(freeSeatImage); // change!!!!!!!!!!!
            seatList.add(freeSeat);
            seatList.get(i).setFitWidth(widthRatio);
            seatList.get(i).setFitHeight(heightRatio);
        }


        //
        // Using platform.runLater() to initialize all the fields once the initialize phase has finished (faster scene transitions)
        //
        Platform.runLater(() ->
        {
            int DEFAULT_COLUMNS = DEFAULT_ROOM_CAPACITY / DEFAULT_SEAT_ROWS;

            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(1.0 / DEFAULT_SEAT_ROWS);

            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(1.0 / DEFAULT_COLUMNS);

            for (int i = 0; i < DEFAULT_SEAT_ROWS; i++)
            {
                for (int j = 0; j < DEFAULT_COLUMNS; j++)
                {
                    int seatImageIndex = numberOfSeat(i, j, DEFAULT_ROOM_CAPACITY / DEFAULT_SEAT_ROWS) - 1;

                    seatsGridPane.add(seatList.get(seatImageIndex), i, j, 1, 1);

                    seatsGridPane.getColumnConstraints().add(cc);
                }

                seatsGridPane.getRowConstraints().add(rc);
            }

            if ((width>= 1600) && (height >= 900))
            {
                seatsGridPane.setPrefHeight(400);
                seatsGridPane.setPrefWidth(400);
            }
            else
            {
                seatsGridPane.setPrefHeight(270);
                seatsGridPane.setPrefWidth(300);
            }

            // Set date picker as not editable
            datePicker.setEditable(false);

            // Add listeners (room combo box and date picker)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeStamp.DATE_FORMAT, Locale.US);


            // ROOM COMBO LISTENER
            roomComboBox.valueProperty().addListener((observableValue, o, t1) ->
            {
                if (studentListWindow != null)
                {
                    studentListWindow.hide();
                    studentListWindow = null;
                }

                datePicker.setValue(null);
                dateLabel.setText("");
                hourSpanComboBox.setValue(null);
                hourSpanComboBox.setItems(null);

                // Clear General Room Info
                totalStudents.setText("Total Students:");
                covidCases.setText("Covid Cases:");
                possibleCases.setText("Possible Cases:");
                freeSeats.setText("Free Seats:");

                courseLabel.setText("");


                // Initiαlize seats
                for (int i = 0; i < DEFAULT_ROOM_CAPACITY; i++)
                    seatList.get(i).setImage(freeSeatImage);

                roomName = (String) roomComboBox.getValue();

                if (roomName == null)
                    return;

                roomLabel.setText(roomName);

                // Open .ser file for specific room
                objectReader = new ObjectReader(MainApplicationController.PATH + roomName + ".ser");
                objectReader.start();
            });


            // DATE PICKER LISTENER
            datePicker.valueProperty().addListener((observable, oldValue, newValue) ->
            {
                if (studentListWindow != null)
                {
                    studentListWindow.hide();
                    studentListWindow = null;
                }

                dateLabel.setText("");
                hourSpanComboBox.setValue(null);
                hourSpanComboBox.setItems(null);

                // Clear General Room Info
                totalStudents.setText("Total Students:");
                covidCases.setText("Covid Cases:");
                possibleCases.setText("Possible Cases:");
                freeSeats.setText("Free Seats:");


                // Initiαlize seats
                for (int i = 0; i < DEFAULT_ROOM_CAPACITY; i++)
                    seatList.get(i).setImage(freeSeatImage);

                if (roomName == null)
                    return;

                courseLabel.setText("");

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

                // Load Room
                room = (Room) objectReader.getResult().orElse(new Room("No Room Loaded", 15, 5));
                timeStampList = new ArrayList<>();

                for (TimeStamp timeStamp : room.getTimeStampList())
                {
                    if (timeStamp.getDateToString().equals(dateToString))
                    {
                        timeStampList.add(timeStamp);
                        hourSpanList.add(timeStamp.getDay().getHourSpan());
                    }
                }

                // List of hour span strings
                hourSpanNames = new ArrayList<>();

                for (HourSpan hourSpan : hourSpanList)
                {
                    hourSpanNames.add(hourSpan.getStartHour() + ":00 - " + hourSpan.getEndHour() + ":00");
                }

                hourSpanComboBox.setItems(FXCollections.observableList(hourSpanNames));
            });


            // HOURSPAN COMBO BOX LISTENER
            hourSpanComboBox.valueProperty().addListener((observableValue, o, t1) ->
            {
                if (studentListWindow != null)
                {
                    studentListWindow.hide();
                    studentListWindow = null;
                }

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

                Course course = timeStamp.getDay().getHourSpan().getCourse();
                courseLabel.setText(course.getCourseName() + " (" + course.getCourseId() + ")");

                // Update seats according to graph
                DefaultUndirectedGraph<Seat, Integer> seatGraph = timeStamp.getSeatGraph();

                seats = new ArrayList<>(seatGraph.vertexSet());


                // Create an image for each case
                Image takenSeatImage = new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/takenSeat.png"));
                Image covidCaseImage = new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidSeat.png"));
                Image possibleCaseImage = new Image(RunApplication.class.getResourceAsStream("/com/pasoftxperts/covidetect/icons/possibleCaseSeat.png"));

                for (int i = 0; i < seats.size(); i++)
                {
                    Seat seat = seats.get(i);

                    Student student = seat.getStudent();

                    if (seats.get(i).isOccupied())
                    {
                        int index = seat.getSeatNumber() - 1;

                        if (student.getHealthIndicator() == 0)
                            seatList.get(index).setImage(takenSeatImage);
                        else if (student.getHealthIndicator() == 1)
                            seatList.get(index).setImage(covidCaseImage);
                        else if (student.getHealthIndicator() == 2)
                            seatList.get(index).setImage(possibleCaseImage);
                    }
                }

                totalStudents.setText("Total Students: " + StudentCounter.countStudents(seatGraph));
                covidCases.setText("Covid Cases: " + CovidCasesCounter.countCovidCases(seatGraph));
                possibleCases.setText("Possible Cases: " + PossibleCasesCounter.countPossibleCases(seatGraph));
                freeSeats.setText("Free Seats: " + FreeSeatsCounter.countFreeSeats(seatGraph));

                dateLabel.setText(timeStamp.getDateToString());


                // We need to pass the seat list and the date to the StudentListWindowController
                // We use a singleton class which will hold that information everytime a new room state
                // is shown

                // We create an ArrayList of objects (we need to store 2 values)
                List<Object> dataList = new ArrayList<>(2);
                dataList.add(dateLabel.getText()); // We add the date
                dataList.add(seats); // We add the seats

                DataHolder dataHolder = DataHolder.getInstance(); // Get the singleton instance
                dataHolder.setObjectData(dataList); // Set the data
            });


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

            // Initialize room combo box
            roomComboBox.setItems(FXCollections.observableList(roomNames));


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


    //
    // Method that shows a small student ID list window of the current room
    //
    @FXML
    protected void showStudentList(ActionEvent event) throws IOException
    {
        // No visualization is selected
        if (hourSpanComboBox.getValue() == null)
            return;

        // Window is already opened
        if (studentListWindow != null)
            return;

        studentListWindow = new Stage();

        // Make it null again so that we can open it again only once at a time
        studentListWindow.setOnCloseRequest(windowEvent -> studentListWindow = null);

        Parent parent = CacheFXMLLoader.load("studentListWindow.fxml");
        Scene studentListScene = new Scene(parent, 600, 370);

        studentListWindow.setScene(studentListScene);
        studentListWindow.setTitle("Student ID List");
        studentListWindow.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));
        studentListWindow.setResizable(false);
        studentListWindow.setAlwaysOnTop(true);
        studentListWindow.show();
    }

    @FXML
    protected void openStatistics(ActionEvent event) throws IOException
    {
        SceneChanger.changeScene(event,
                "Statistical Analysis - CovIDetect©",
                "mainApplicationGUI-1600x900-statistics.fxml",
                "mainApplicationGUI-1000x600-statistics.fxml");
    }

    @FXML
    protected void openSeatsView(ActionEvent event)
    {

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
