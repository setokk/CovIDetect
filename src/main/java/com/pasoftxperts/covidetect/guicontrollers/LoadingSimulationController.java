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

import com.pasoftxperts.covidetect.RunApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingSimulationController implements Initializable
{
    @FXML
    private AnchorPane centerPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
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
        mediaView.setFitHeight(600);
        mediaView.setFitWidth(800);

        centerPane.getChildren().add(mediaView);

    }
}

