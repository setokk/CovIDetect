/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is the GUI controller of the register scene.
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers;

import com.pasoftxperts.covidetect.RunApplication;
import com.pasoftxperts.covidetect.emailverification.EmailVerifier;
import com.pasoftxperts.covidetect.passwordchecker.PasswordChecker;
import com.pasoftxperts.covidetect.guicontrollers.popupwindow.PopupWindow;
import com.pasoftxperts.covidetect.userhandler.Administrator;
import com.pasoftxperts.covidetect.userhandler.AdministratorLog;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private TextField visibleRepeatPasswordField;

    @FXML
    private CheckBox passwordCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        infoLabel.visibleProperty().bind(infoIcon.hoverProperty());
        visiblePasswordField.setVisible(false);
        visibleRepeatPasswordField.setVisible(false);
    }

    //
    // This method switches from the register page, to the login page
    //
    @FXML
    protected void openLoginPage(MouseEvent event) throws IOException
    {
        Parent loginParent = FXMLLoader.load(RunApplication.class.getResource("loginGUI.fxml"));

        Scene loginScene = new Scene(loginParent, 600, 500);

        Stage window = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        window.setScene(loginScene);
        window.setTitle("CovIDetect Login");

        window.show();
    }

    // Registers an admin after a series of checks and validations.
    @FXML
    protected void registerAdmin(ActionEvent event) throws Exception
    {
        // First we check if the user has not input an email
        if (emailField.getText().trim().equals(""))
        {
            PopupWindow.display("Please provide an email.");
            return;
        }

        // Check if the email has an academic email domain
        if (EmailVerifier.checkAcademicEmail(emailField.getText().trim()).equals("Not Academic"))
        {
            PopupWindow.display("Email is not academic.");
            return;
        }

        // We use a password field (hidden) and a text field for the password text field (toggled by checkbox).
        // In order to get the appropriate text, we have to check which one is visible.

        String password = passwordField.getText().trim();
        String repeatPassword = passwordRepeatField.getText().trim();

        if (!passwordField.isVisible())
        {
            password = visiblePasswordField.getText().trim();
            repeatPassword = visibleRepeatPasswordField.getText().trim();
        }

        // Check if the password fields are empty.
        if (password.equals("") || repeatPassword.equals(""))
        {
            PopupWindow.display("Please provide a password.");
            return;
        }

        // Here, we start off by checking the password in order of importance [length - special character - upper case character]
        if (!PasswordChecker.hasAppropriateLength(password))
        {
            PopupWindow.display("Password length has to be between " +
                    PasswordChecker.MIN_PASSWORD_LENGTH + " and " +
                    PasswordChecker.MAX_PASSWORD_LENGTH);
            return;
        }

        if (!PasswordChecker.hasSpecialCharacter(password))
        {
            PopupWindow.display("Password requires at least one special character.");
            return;
        }

        if (!PasswordChecker.hasUpperCase(password))
        {
            PopupWindow.display("Password must contain at least one upper case.");
            return;
        }

        // We are sure about the email format. Now we check if they are the same.
        if (!password.equals(repeatPassword))
        {
            PopupWindow.display("Passwords do not match");
            return;
        }

        // We check whether or not this email is already registered.
        // emailIsNotRegistered returns an admin with an email of "Not Registered"
        // if it does not find an admin with a particular email

        if (!AdministratorLog.emailIsNotRegistered(emailField.getText().trim())
                .getEmail()
                .equals("Not Registered"))
        {
            PopupWindow.display("Email is already registered.");
            return;
        }

        // We get the main stage and scene.
        Stage registerWindow = (Stage) ( (Node) event.getSource() ).getScene().getWindow();

        // Show the loading screen scene.
        Scene loadingScene = new Scene(FXMLLoader.load(RunApplication.class.getResource("loadingGUI.fxml")), 600, 550);
        Stage loadingStage = new Stage();
        loadingStage.setScene(loadingScene);
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.show();
        registerWindow.centerOnScreen();

        // Task to support multi threading for long processes (isValid) without the GUI freezing
        Task<Boolean> emailVerifierTask = new Task<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return EmailVerifier.isValid(emailField.getText().trim());
            }
        };

        // Here we put all the code that validates the email and changes GUI components.
        String finalPassword = password;
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

            // After all those validations, we can now register the admin.
            AdministratorLog.addAdmin(new Administrator(emailField.getText().trim(), finalPassword));

            emailField.setText("");
            passwordField.setText("");
            passwordRepeatField.setText("");
            visiblePasswordField.setText("");
            visibleRepeatPasswordField.setText("");
        });

        Thread taskThread = new Thread(emailVerifierTask);
        taskThread.start();
    }

    // Shows or hides the password fields depending on if the checkbox to show the password is on/off.
    @FXML
    protected void showHidePassword(ActionEvent event)
    {
        if (passwordCheckBox.isSelected())
        {
            visiblePasswordField.setText(passwordField.getText());
            visibleRepeatPasswordField.setText(passwordRepeatField.getText());

            // Show password field values.
            passwordField.setVisible(false);
            passwordRepeatField.setVisible(false);
            visiblePasswordField.setVisible(true);
            visibleRepeatPasswordField.setVisible(true);
        }
        else
        {
            passwordField.setText(visiblePasswordField.getText());
            passwordRepeatField.setText(visibleRepeatPasswordField.getText());

            // Hide password field values.
            visiblePasswordField.setVisible(false);
            visibleRepeatPasswordField.setVisible(false);
            passwordField.setVisible(true);
            passwordRepeatField.setVisible(true);
        }
    }
}
