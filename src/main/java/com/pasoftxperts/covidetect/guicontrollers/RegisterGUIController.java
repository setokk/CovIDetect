package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.emailverification.EmailVerifier;
import com.pasoftxperts.covidetect.guicontrollers.passwordchecker.PasswordChecker;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.userhandler.Admin;
import com.pasoftxperts.covidetect.userhandler.AdminLog;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterGUIController implements Initializable
{
    @FXML
    private AnchorPane pane;

    @FXML
    private Label registerLabel;

    @FXML
    private Label goBackLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private ImageView infoIcon;

    @FXML
    private Button registerButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField passwordRepeatField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        infoLabel.visibleProperty().bind(infoIcon.hoverProperty());
    }

    // This method switches from the register/any other page, to the login page
    @FXML
    protected void openLoginPage(MouseEvent event) throws IOException
    {
        Parent loginParent = FXMLLoader.load(RunApplication.class.getResource("loginGUI.fxml"));
        Scene loginScene = new Scene(loginParent, 600, 500);

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        window.setScene(loginScene);
        window.setTitle("CovIDetect Login");

        // Deallocate memory
        loginParent = null;
        loginScene = null;
        System.gc();

        window.show();
    }

    @FXML
    protected void registerAdmin(ActionEvent event) throws Exception {
        // First we check if the user has not input anything
        if (emailField.getText().equals("")
                || passwordField.getText().equals("")
                || passwordRepeatField.getText().equals(""))
            return;

        // Here, we start off by checking the password in order of importance [length - special character - upper case character]
        if (!PasswordChecker.hasAppropriateLength(passwordField.getText()))
        {
            PopupWindow.display("Password length has to be between " +
                    PasswordChecker.MIN_PASSWORD_LENGTH + " and " +
                    PasswordChecker.MAX_PASSWORD_LENGTH);
            return;
        }

        if (!PasswordChecker.hasSpecialCharacter(passwordField.getText()))
        {
            PopupWindow.display("Password requires at least one special character.");
            return;
        }

        if (!PasswordChecker.hasUpperCase(passwordField.getText()))
        {
            PopupWindow.display("Password must contain at least one upper case.");
            return;
        }

        // We are sure about the email format. Now we check if they are the same.
        if (!passwordField.getText().equals(passwordRepeatField.getText()))
        {
            PopupWindow.display("Passwords do not match");
            return;
        }

        // We can now check if the email has an academic email domain
        if (EmailVerifier.checkAcademicEmail(emailField.getText()).equals("Not Academic"))
        {
            PopupWindow.display("Email is not academic.");
            return;
        }

        // We check whether or not this email is already registered.
        // emailIsNotRegistered returns an admin with an email of "Not Registered"
        // if it does not find an admin with a particular email
        AdminLog.readAdminLog();

        if (!AdminLog.emailIsNotRegistered(emailField.getText())
                .getEmail()
                .equals("Not Registered"))
        {
            PopupWindow.display("Email is already registered.");
            return;
        }

        // We get the main stage and scene.
        Stage registerWindow = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        // Show the loading screen scene.
        Scene loadingScene = new Scene(FXMLLoader.load(RunApplication.class.getResource("loadingGUI.fxml")), 600, 500);
        Stage loadingStage = new Stage();
        loadingStage.setScene(loadingScene);
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.show();
        registerWindow.centerOnScreen();

        // We create a task class so that
        Task<Boolean> emailVerifierTask = new Task<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return EmailVerifier.isValid(emailField.getText());
            }
        };

        // Here we put all the code that validates the email and changes GUI components.
        emailVerifierTask.setOnSucceeded(e ->
        {
            loadingStage.close();
            boolean result = emailVerifierTask.getValue();
            if (!result)
            {
                try
                {
                    PopupWindow.display("Email does not exist.");
                } catch (IOException ex) { ex.printStackTrace(); }
                return;
            }

            try
            {
                PopupWindow.display("Registration was successful.");
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

            emailField.setText("");
            passwordField.setText("");
            passwordRepeatField.setText("");

            // After all those validations, we can now register the admin.
            AdminLog.addAdmin(new Admin(emailField.getText(), passwordField.getText()));
            AdminLog.updateAdminLog();
        });

        Thread taskThread = new Thread(emailVerifierTask);
        taskThread.start();

        // Deallocate memory
        loadingScene = null;
        System.gc();
    }
}
