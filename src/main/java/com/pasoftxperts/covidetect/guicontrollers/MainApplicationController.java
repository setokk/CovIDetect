package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.history.HistoryManager;
import com.pasoftxperts.covidetect.loginsession.LoginSession;
import com.pasoftxperts.covidetect.simulation.Simulation;
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

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private ListView historyListView;

    @FXML
    private Button runSimulationButton;

    @FXML
    private Label simulationLabel;

    @FXML
    private AnchorPane simulationMessageHover;

    private static boolean simulationPressed = false; // Indicates whether the simulation button was pressed or not

    public static final String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/";

    // We need the width and height for other classes
    public static double width;

    public static double height;

    public static String selectedHistoryOption = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        if (simulationPressed)
        {
            runSimulationButton.setDisable(true);
            runSimulationButton.setStyle("-fx-background-color:#323232");
        }

        // Simulation Info Pane
        simulationMessageHover.visibleProperty().bind(runSimulationButton.hoverProperty());

        // Statistics chart video
        Media media;

        MediaPlayer mediaPlayer;

        MediaView mediaView;

        media = new Media(RunApplication.class.getResource("icons/bigData.mp4").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        // Loop it
        mediaPlayer.setOnEndOfMedia(() ->
        {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        mediaView = new MediaView(mediaPlayer);
        mediaView.setFitHeight(450);
        mediaView.setFitWidth(650);

        mainBorderPane.setCenter(mediaView);


        usernameLabel.setText("Welcome, " +  LoginSession.getUsername());


        // History List View

        // Make path directories
        new File(HistoryManager.HISTORY_PATH).mkdirs();

        // We go through the files
        File folder = new File(HistoryManager.HISTORY_PATH);
        List<File> listOfFiles = Arrays.asList(folder.listFiles());

        // Sort based on the last date modified
        Collections.sort(listOfFiles, (a, b) -> a.lastModified() > b.lastModified() ? -1 : a.lastModified() == b.lastModified() ? 0 : 1);

        List<String> fileNames = listOfFiles.stream()
                                            .map(File::getName)
                                            .collect(Collectors.toList());

        historyListView.getItems().addAll(fileNames);

        historyListView.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) ->
        {
            loadHistory();
        });

    }

    @FXML
    protected void openStatistics(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        width = window.getWidth();
        height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
        {
            resourceName = "mainApplicationGUI-1600x900-statistics.fxml";
            width = 1600;
            height = 900;
        }
        else
        {
            resourceName = "mainApplicationGUI-1000x600-statistics.fxml";
            width = 1000;
            height = 600;
        }

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        window.getScene().setRoot(visualizationParent);

        window.setTitle("Statistical Analysis - CovIDetect©");

        window.show();
    }

    @FXML
    protected void openSeatsView(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        width = window.getWidth();
        height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
        {
            resourceName = "mainApplicationGUI-1600x900-viewSeats.fxml";
            width = 1600;
            height = 900;
        }
        else
        {
            resourceName = "mainApplicationGUI-1000x600-viewSeats.fxml";
            width = 1000;
            height = 600;
        }

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        window.getScene().setRoot(visualizationParent);

        window.setTitle("Room Visualization - CovIDetect©");

        window.show();
    }

    @FXML
    protected void openUpdateCovidCase(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        width = window.getWidth();
        height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
        {
            resourceName = "mainApplicationGUI-1600x900-updateStatus.fxml";
            width = 1600;
            height = 900;
        }
        else
        {
            resourceName = "mainApplicationGUI-1000x600-updateStatus.fxml";
            width = 1000;
            height = 600;
        }

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        window.getScene().setRoot(visualizationParent);

        window.setTitle("Update Student's Covid Status - CovIDetect© - CovIDetect©");

        window.show();
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException
    {
        LoginSession.resetSession();

        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("loginGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("CovIDetect Login");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));
        stage.setScene(scene);
        stage.setResizable(false);

        // Get previous window and hide it
        Stage previousWindow = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        previousWindow.hide();

        stage.show();
    }

    @FXML
    protected void loadHistory()
    {
        selectedHistoryOption = (String) historyListView.getSelectionModel().getSelectedItem();

        if (selectedHistoryOption == null)
            return;

        HistoryManager.setSelectedHistoryStatus(true);

        if (selectedHistoryOption.contains("Statistics"))
        {
            statisticsButton.fireEvent(new ActionEvent()); // Open Statistics page with history status set to true
        }
        else
        {

        }
    }

    @FXML
    protected void runSimulationProgress(ActionEvent event) throws IOException
    {
        String resourceName;

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        width = window.getWidth();
        height = window.getHeight();

        if ((width >= 1600) && (height >= 900))
        {
            resourceName = "loadingSimulationGUI-1600x900.fxml";
            width = 1600;
            height = 900;
        }
        else
        {
            resourceName = "loadingSimulationGUI-1000x600.fxml";
            width = 1000;
            height = 600;
        }

        Parent visualizationParent = FXMLLoader.load(RunApplication.class.getResource(resourceName));
        Scene visualizationScene = new Scene(visualizationParent, width, height);

        window.setScene(visualizationScene);

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
            new File(HistoryManager.HISTORY_PATH).mkdirs();

            // We go through the files
            File folder = new File(HistoryManager.HISTORY_PATH);
            File[] listOfFiles = folder.listFiles();

            // Delete them
            for (int i = 0; i < listOfFiles.length; i++)
            {
                listOfFiles[i].delete();
            }

            // Go back to the home page
            String name;

            if ((width >= 1600) && (height >= 900))
            {
                name = "mainApplicationGUI-1600x900.fxml";
                width = 1600;
                height = 900;
            }
            else
            {
                name = "mainApplicationGUI-1000x600.fxml";
                width = 1000;
                height = 600;
            }

            Parent parent = null;
            try
            {
                parent = FXMLLoader.load(RunApplication.class.getResource(name));
                Scene scene = new Scene(parent, width, height);
                window.setScene(scene);
            }
            catch (IOException ex)
            {
                System.exit(1);
            }
        });

        simulation.start();
    }
}
