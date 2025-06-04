package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.CreditCardDAO;
import com.mycompany.oopecommerceproject1.dao.UserDAO;
import com.mycompany.oopecommerceproject1.model.CreditCard;
import com.mycompany.oopecommerceproject1.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the Profile screen:
 * - Updates existing user information (email, password)
 * - Allows adding/updating credit cards (multiple cards supported)
 * - “Back” button returns to the main menu
 */
public class ProfileController {

    // ----- Profile fields -----
    @FXML private TextField usernameField;      // For display only
    @FXML private TextField emailField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updateButton;
    @FXML private Label messageLabel;

    // ----- Credit Card fields -----
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryMonthField;
    @FXML private TextField expiryYearField;
    @FXML private PasswordField cvvField;
    @FXML private Button updateCardButton;
    @FXML private Button addCardButton;
    @FXML private Label cardMessageLabel;

    // ----- Common (Back) button -----
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        int currentUserId = Session.getCurrentUserId();
        // If session userId is invalid, print error
        if (currentUserId <= 0) {
            System.err.println("Session.getCurrentUserId() returned 0 or negative! Please log in first.");
            return;
        }

        // Display the username (read-only)
        usernameField.setText(Session.getCurrentUsername());

        // Check if user has any saved credit cards
        CreditCardDAO cardDao = new CreditCardDAO();
        List<CreditCard> userCards = cardDao.getAllCardsByUserId(currentUserId);

        // If no cards exist: disable “Update Card”, enable “Add Card”
        if (userCards.isEmpty()) {
            updateCardButton.setDisable(true);
            addCardButton.setDisable(false);
        } else {
            // If multiple cards exist, show only the first one (as an example)
            CreditCard existingCard = userCards.get(0);
            cardNumberField.setText(existingCard.getCardNumber());
            expiryMonthField.setText(String.valueOf(existingCard.getExpiryMonth()));
            expiryYearField.setText(String.valueOf(existingCard.getExpiryYear()));
            cvvField.setText(existingCard.getCvv());

            updateCardButton.setDisable(false);
            addCardButton.setDisable(false); // Allow adding multiple cards
        }
    }

    // --------------- Profile Update ----------------
    /**
     * Updates user profile (email and password):
     * - Checks that all fields are filled
     * - Verifies old password matches
     * - Updates password, then updates email
     * - Displays success or failure message in messageLabel
     */
    @FXML
    private void handleUpdateAction(ActionEvent event) {
        int currentUserId = Session.getCurrentUserId();
        if (currentUserId <= 0) {
            messageLabel.setText("Unable to retrieve user information.");
            return;
        }

        String email = emailField.getText().trim();
        String oldPwd = oldPasswordField.getText().trim();
        String newPwd = newPasswordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();

        if (email.isEmpty() || oldPwd.isEmpty() || newPwd.isEmpty() || confirm.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }
        if (!newPwd.equals(confirm)) {
            messageLabel.setText("New passwords do not match.");
            return;
        }

        UserDAO userDao = new UserDAO();
        boolean pwdUpdated = userDao.updatePassword(currentUserId, oldPwd, newPwd);
        if (!pwdUpdated) {
            messageLabel.setText("Old password is incorrect or update failed.");
            return;
        }

        boolean emailUpdated = userDao.updateEmail(currentUserId, email);
        if (emailUpdated) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Profile information updated successfully.");
        } else {
            messageLabel.setText("An error occurred while updating email.");
        }
    }

    // ------------- Update Card ------------------
    /**
     * Updates an existing credit card.
     * - Validates that month and year are numeric
     * - Updates the most recently added card for the user
     */
    @FXML
    private void handleCardUpdateAction(ActionEvent event) {
        int currentUserId = Session.getCurrentUserId();
        if (currentUserId <= 0) {
            cardMessageLabel.setText("Unable to retrieve user information.");
            return;
        }

        String number   = cardNumberField.getText().trim();
        String monthStr = expiryMonthField.getText().trim();
        String yearStr  = expiryYearField.getText().trim();
        String cvv      = cvvField.getText().trim();

        if (number.isEmpty() || monthStr.isEmpty() || yearStr.isEmpty() || cvv.isEmpty()) {
            cardMessageLabel.setText("Please fill in all card fields.");
            return;
        }

        int month, year;
        try {
            month = Integer.parseInt(monthStr);
            year  = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            cardMessageLabel.setText("Month and year must be numeric.");
            return;
        }

        // Update the most recently added card for this user
        CreditCard updated = new CreditCard(currentUserId, number, month, year, cvv);
        boolean success = new CreditCardDAO().updateCard(updated);

        if (success) {
            cardMessageLabel.setStyle("-fx-text-fill: green;");
            cardMessageLabel.setText("Card updated successfully.");
        } else {
            cardMessageLabel.setText("An error occurred while updating the card.");
        }
    }

    // --------------- Add Card -------------------
    /**
     * Adds a new credit card.
     * - Validates that month and year are numeric
     * - Inserts the new card using DAO
     */
    @FXML
    private void handleCardAddAction(ActionEvent event) {
        int currentUserId = Session.getCurrentUserId();
        if (currentUserId <= 0) {
            cardMessageLabel.setText("Unable to retrieve user information.");
            return;
        }

        String number   = cardNumberField.getText().trim();
        String monthStr = expiryMonthField.getText().trim();
        String yearStr  = expiryYearField.getText().trim();
        String cvv      = cvvField.getText().trim();

        if (number.isEmpty() || monthStr.isEmpty() || yearStr.isEmpty() || cvv.isEmpty()) {
            cardMessageLabel.setText("Please fill in all card fields.");
            return;
        }

        int month, year;
        try {
            month = Integer.parseInt(monthStr);
            year  = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            cardMessageLabel.setText("Month and year must be numeric.");
            return;
        }

        CreditCard newCard = new CreditCard(currentUserId, number, month, year, cvv);
        boolean success = new CreditCardDAO().addCard(newCard);

        if (success) {
            cardMessageLabel.setStyle("-fx-text-fill: green;");
            cardMessageLabel.setText("New card added successfully.");
        } else {
            cardMessageLabel.setText("An error occurred while adding the card.");
        }
    }

    // ----------------- Back ----------------------
    /**
     * Called when the “Back” button is pressed.
     * Loads MainMenu.fxml.
     */
    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml")
            );
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
