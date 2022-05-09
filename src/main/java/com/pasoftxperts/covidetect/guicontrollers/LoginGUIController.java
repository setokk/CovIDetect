package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.userhandler.Admin;
import com.pasoftxperts.covidetect.userhandler.AdminLog;
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

import java.io.IOException;

public class LoginGUIController
{
    public static Admin adminSession;

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
        if (emailField.getText().equals("") || passwordField.getText().equals(""))
            return;

        // Check if the email is present in admin log list (returns an admin)
        AdminLog.readAdminLog();
        Admin adminResult = AdminLog.emailIsNotRegistered(emailField.getText());

        if (adminResult.getEmail().equals("Not Registered"))
        {
            PopupWindow.display("No account registered with this email.");
            return;
        }

        // At this point, we found a registered admin email (adminResult email is not "Not Registered")
        // Check if password match
        if (!passwordField.getText().equals(adminResult.getPassword()))
        {
            PopupWindow.display("Password is incorrect.");
            return;
        }

        // We can now launch the main application window.
        Stage mainApplicationWindow = new Stage();
        Scene mainScene = new Scene(FXMLLoader.load(RunApplication.class.getResource("mainApplicationGUI.fxml")), 1600, 900);

        mainApplicationWindow.setTitle("CovIDetect© by PasoftXperts");
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