/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is the GUI controller of the main application scene.
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.filemanager.RecursiveDeleter;
import com.pasoftxperts.covidetect.guicontrollers.cachefxmlloader.CacheFXMLLoader;
import com.pasoftxperts.covidetect.guicontrollers.font.FontInitializer;
import com.pasoftxperts.covidetect.guicontrollers.scenechanger.SceneChanger;
import com.pasoftxperts.covidetect.history.HistoryManager;
import com.pasoftxperts.covidetect.loginsession.LoginSession;
import com.pasoftxperts.covidetect.simulation.Simulation;
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

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainApplicationController implements Initializable
{
    @FXML
    private AnchorPane pane;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button updateCovidStatusButton;

    @FXML
    private Button viewSeatButton;

    @FXML
    private ImageView seatsIcon;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label logoutLabel;

    @FXML
    private ImageView memberInfoIcon;

    @FXML
    private ListView historyListView;

    @FXML
    private Button runSimulationButton;

    @FXML
    private Label simulationLabel;

    @FXML
    private AnchorPane simulationMessageHover;

    // Member info stage
    private Stage memberInfoStage = null;

    //
    // FLAGS
    //
    private static boolean simulationPressed = false; // Indicates whether the simulation button was pressed or not

    public static String selectedHistoryOption = null; // Indicates whether the user has selected to load statistics from history

    public static final String PATH = System.getProperty("user.dir") + "/university of macedonia/applied informatics/"; // Path in which all the simulation files are saved

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        FontInitializer.initializeFont(pane);

        usernameLabel.setText("Welcome, " + LoginSession.getUsername());

        // Using platform.runLater() to initialize all the fields once the initialize phase has finished (faster scene transitions)
        Platform.runLater(() ->
        {
            // Statistics chart video
            mainBorderPane.setCenter(LoginGUIController.mediaView);

            if (simulationPressed)
            {
                runSimulationButton.setDisable(true);
                runSimulationButton.setStyle("-fx-background-color:#323232");
            }

            // Simulation Info Pane
            simulationMessageHover.visibleProperty().bind(runSimulationButton.hoverProperty());

            // History List View

            // Make path directories
            new File(HistoryManager.HISTORY_PATH + LoginSession.getUsername()).mkdirs();

            // We go through the files
            File folder = new File(HistoryManager.HISTORY_PATH + LoginSession.getUsername());
            List<File> listOfFiles = Arrays.asList(folder.listFiles());


            // Sort based on the last date modified
            Collections.sort(listOfFiles, (a, b) -> a.lastModified() > b.lastModified() ? -1 : a.lastModified() == b.lastModified() ? 0 : 1);

            List<String> fileNames = listOfFiles.stream()
                    .map(File::getName)
                    .collect(Collectors.toList());

            historyListView.getItems().addAll(fileNames);

            historyListView.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> loadHistory());
        });
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
    protected void openSeatsView(ActionEvent event) throws IOException
    {
        SceneChanger.changeScene(event,
                "Room Visualization - CovIDetect©",
                "mainApplicationGUI-1600x900-viewSeats.fxml",
                "mainApplicationGUI-1000x600-viewSeats.fxml");
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

    // Load history of statistical report
    @FXML
    protected void loadHistory()
    {
        selectedHistoryOption = (String) historyListView.getSelectionModel().getSelectedItem();

        // Check if anything is selected
        if (selectedHistoryOption == null)
            return;

        // Set history status to true (flag that indicated to statistical controller that it should load from history)
        HistoryManager.setSelectedHistoryStatus(true);

        // Open Statistics page with history status set to true
        statisticsButton.fireEvent(new ActionEvent());
    }

    @FXML
    protected void runSimulationProgress(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        double width = window.getWidth();
        double height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
            resourceName = "loadingSimulationGUI-1600x900.fxml";
        else
            resourceName = "loadingSimulationGUI-1000x600.fxml";

        Parent visualizationParent = CacheFXMLLoader.load(resourceName);
        window.getScene().setRoot(visualizationParent);

        Service simulation = new Service()
        {
            @Override
            protected Task createTask()
            {
                return new Task<Void>()
                {
                    @Override
                    protected Void call() throws Exception
                    {
                        Simulation.runSimulation();

                        return null;
                    }
                };
            }
        };

        simulation.setOnSucceeded((e) ->
        {
            simulationPressed = true;

            // Delete previous history files, if any
            RecursiveDeleter.deleteDirectoryRecursively(HistoryManager.HISTORY_PATH);

            // Delete the last updated date file for update covid status
            RecursiveDeleter.deleteDirectoryRecursively(PATH + "lastupdate/");

            // Go back to the home page
            String name;

            if ((width >= 1600) && (height >= 900))
                name = "mainApplicationGUI-1600x900.fxml";
            else
                name = "mainApplicationGUI-1000x600.fxml";


            Parent parent = null;
            try
            {
                parent = CacheFXMLLoader.load(name);
                window.getScene().setRoot(parent);
            }
            catch (IOException ex)
            {
                System.exit(1);
            }
        });

        simulation.start();
    }
}
