package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.UserDAO;
import com.mycompany.oopecommerceproject1.model.User;
import com.mycompany.oopecommerceproject1.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Login screen.
 * - Validates username and password
 * - On success, saves user info to Session and opens MainMenu.fxml
 * - On failure, displays an error message
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    /**
     * Called when the “Login” button is pressed.
     * - Checks for empty fields
     * - Queries DAO for matching username/password
     * - On success: stores username and userId in Session, loads MainMenu.fxml
     * - On failure: shows error message
     */
    @FXML
    private void handleLoginAction(ActionEvent event) {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        // Check for empty fields
        if (user.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("Please enter username and password.");
            return;
        }

        UserDAO userDao = new UserDAO();
        User found = userDao.findByUsernameAndPassword(user, pass);

        if (found != null) {
            // Save username and ID to Session
            Session.setCurrentUsername(found.getUsername());

            try {
                // Login succeeded → open MainMenu.fxml
                Parent root = FXMLLoader.load(
                    getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml")
                );
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Invalid username or password
            messageLabel.setText("Invalid username or password.");
        }
    }
}
