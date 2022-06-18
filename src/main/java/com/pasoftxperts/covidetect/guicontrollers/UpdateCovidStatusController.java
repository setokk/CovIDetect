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

import com.pasoftxperts.covidetect.covidcaseupdater.CovidCaseUpdater;
import com.pasoftxperts.covidetect.filemanager.FileWrapper;
import com.pasoftxperts.covidetect.filemanager.TaskObjectReader;
import com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader.CacheFXMLLoader;
import com.pasoftxperts.covidetect.guicontrollers.fileschecker.FilesChecker;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.guicontrollers.scenechanger.SceneChanger;
import com.pasoftxperts.covidetect.loginsession.LoginSession;
import com.pasoftxperts.covidetect.university.Room;
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

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    private ArrayList<String> roomNames;

    // Object Reader Threads List for reading rooms
    private List<TaskObjectReader> objectReaderList = new ArrayList<>();

    List<Room> rooms = new ArrayList<>(); // All rooms available

    // Arraylist to save the rooms in which we found the student
    // (improves performance because we don't save ALL rooms after updating the status)
    List<Room> modifiedRooms = new ArrayList<>();

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

            // Create and start Threads using JavaFX service threads
            for (String name : roomNames)
                objectReaderList.add(new TaskObjectReader(MainApplicationController.PATH + name + ".ser"));

            roomNames.clear();

            //
            // Load All Rooms
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


        // Get Results from Threads
        for (TaskObjectReader objectReader : objectReaderList)
            rooms.add((Room) objectReader.getResult().orElse(new Room("No Room Loaded", 15, 5)));


        // Find if the student exists,
        // and if they exist,
        // update the possible cases that surround him for the previous 2 days in every room
        String studentId = studentField.getText();

        // Get the result of if the student was found or not and update their status appropriately, if found
        boolean found = CovidCaseUpdater.updateStudentCovidCase(studentId,
                                                                formatter.format(datePicker.getValue()),
                                                                rooms,
                                                                modifiedRooms);

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
    protected void openStatistics(ActionEvent event) throws IOException
    {
        SceneChanger.changeScene(event,
                "Statistical Analysis - CovIDetect©",
                "mainApplicationGUI-1600x900-statistics.fxml",
                "mainApplicationGUI-1000x600-statistics.fxml");

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
    }
}
