/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is the GUI controller of the loading simulation video scene.
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.guicontrollers.helpers.font.FontInitializer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingSimulationController implements Initializable
{
    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane centerPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        FontInitializer.initializeFont(pane);

        // Pie Chart Video
        Media media;

        MediaPlayer mediaPlayer;

        MediaView mediaView;

        media = new Media(RunApplication.class.getResource("icons/statisticsPieChart.mp4").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        // Loop it
        mediaPlayer.setOnEndOfMedia(() ->
        {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        mediaView = new MediaView(mediaPlayer);

        // Get default monitor resolution
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        if (width >= 1600 && height >= 900)
        {
            mediaView.setFitHeight(600);
            mediaView.setFitWidth(800);
        }
        else
        {
            mediaView.setFitHeight(300);
            mediaView.setFitWidth(400);
        }

        centerPane.getChildren().add(mediaView);

    }
}

