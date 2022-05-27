package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.userhandler.Administrator;
import com.pasoftxperts.covidetect.userhandler.AdministratorLog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class LoginGUIController
{
    public static Administrator adminSession;

    @FXML
    private Button loginButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    // This method switches from the login/any other page, to the register page
    @FXML
    protected void openRegisterPage(MouseEvent event) throws IOException
    {
        Parent registerParent = FXMLLoader.load(RunApplication.class.getResource("registerGUI.fxml"));
        Scene registerScene = new Scene(registerParent, 600, 500);

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        window.setScene(registerScene);
        window.setTitle("CovIDetect Register");

        // Deallocate memory
        registerParent = null;
        registerScene = null;
        System.gc();

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

        // We can now launch the main application window.
        Stage mainApplicationWindow = new Stage();
        Scene mainScene;

        // First we get the monitor resolution. (Checks default screen in case of multiple monitors)
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        // We can now check if the monitor resolution is higher that 1600x900 or less.
        if ((width >= 1600) && (height >= 900))
            mainScene = new Scene(FXMLLoader.load(RunApplication.class.getResource("mainApplicationGUI-1600x900.fxml")), 1600, 900);
        else
            mainScene = new Scene(FXMLLoader.load(RunApplication.class.getResource("mainApplicationGUI-1280x800.fxml")), 1280, 800);

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