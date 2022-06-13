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
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.loginsession.LoginSession;
import com.pasoftxperts.covidetect.userhandler.Administrator;
import com.pasoftxperts.covidetect.userhandler.AdministratorLog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginGUIController implements Initializable
{
    public static Administrator adminSession;

    @FXML
    private Button loginButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    // Element to load mp4
    public static MediaView mediaView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Media media;
        MediaPlayer mediaPlayer;

        // Load the mp4 for main application page
        media = new Media(RunApplication.class.getResource("icons/bigData.mp4").toExternalForm());

        mediaPlayer = new MediaPlayer(media); // Already loaded media
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
            mediaView.setFitHeight(250);
            mediaView.setFitWidth(450);
        }
        else
        {
            mediaView.setFitHeight(450);
            mediaView.setFitWidth(650);
        }

        //
        // Login button enter event listener
        //
        Platform.runLater(() ->
        {
            Scene scene = loginButton.getScene();

            scene.setOnKeyPressed((keyEvent ->
            {
                if (keyEvent.getCode() == KeyCode.ENTER)
                    loginButton.fire();
            }));
        });
    }


    //
    // This method switches from the login/any other page, to the register page
    //
    @FXML
    protected void openRegisterPage(MouseEvent event) throws IOException
    {
        Parent registerParent = FXMLLoader.load(RunApplication.class.getResource("registerGUI.fxml"));
        Scene registerScene = new Scene(registerParent, 600, 500);

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        window.setScene(registerScene);
        window.setTitle("CovIDetect Register");

        window.show();
    }

    @FXML
    protected void loginAdmin(ActionEvent event) throws IOException
    {
        // Empty fields
        if (emailField.getText().trim().equals(""))
        {
            PopupWindow.display("Please provide an email.");
            return;
        }

        if (passwordField.getText().trim().equals(""))
        {
            PopupWindow.display("Please provide a password.");
            return;
        }

        // Check if the email is present in admin log list (returns an admin)
        Administrator adminResult = AdministratorLog.emailIsNotRegistered(emailField.getText().trim());

        if (adminResult.getEmail().equals("Not Registered"))
        {
            PopupWindow.display("No account registered with this email.");
            return;
        }

        // At this point, we found a registered admin email (adminResult email is not "Not Registered")
        // Check if password match
        if (!passwordField.getText().trim().equals(adminResult.getPassword()))
        {
            PopupWindow.display("Password is incorrect.");
            return;
        }

        // Create a login session
        LoginSession.setEmail(emailField.getText().trim());

        // We can now launch the main application window.
        Stage mainApplicationWindow = new Stage();
        Scene mainScene;

        //
        // First we get the monitor resolution. (Checks default screen in case of multiple monitors)
        //
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        // We can now check if the monitor resolution is higher that 1600x900 or less.
        if ((width >= 1600) && (height >= 900))
            mainScene = new Scene(CacheFXMLLoader.load("mainApplicationGUI-1600x900.fxml"));
        else
            mainScene = new Scene(CacheFXMLLoader.load("mainApplicationGUI-1000x600.fxml"));


        mainApplicationWindow.setTitle("CovIDetect© by PasoftXperts");
        mainApplicationWindow.setResizable(false);
        mainApplicationWindow.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasoftxperts/covidetect/icons/covidDetectWindowIcon.png")));
        mainApplicationWindow.setScene(mainScene);

        // We close the previous stage (login GUI)
        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        window.hide();

        // Show main application
        mainApplicationWindow.show();

        // Deallocate memory
        window = null;
        mainScene = null;
        System.gc();
    }
}