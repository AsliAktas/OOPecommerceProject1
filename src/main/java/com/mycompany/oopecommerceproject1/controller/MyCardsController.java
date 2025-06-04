package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.CreditCardDAO;
import com.mycompany.oopecommerceproject1.model.CreditCard;
import com.mycompany.oopecommerceproject1.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the “My Cards” screen:
 * - Lists all credit cards the user has saved
 * - If no cards are found, displays a message
 * - “Back” button returns to the main menu
 */
public class MyCardsController {

    @FXML private ListView<CreditCard> cardsListView;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        int currentUserId = Session.getCurrentUserId();
        // If user is not logged in, show a message
        if (currentUserId <= 0) {
            messageLabel.setText("Please log in first.");
            return;
        }

        CreditCardDAO cardDao = new CreditCardDAO();
        List<CreditCard> cards = cardDao.getAllCardsByUserId(currentUserId);

        // If the user has no saved cards, show a message
        if (cards.isEmpty()) {
            messageLabel.setText("You have no saved cards yet.");
        } else {
            // Convert the list to ObservableList and set it on the ListView
            ObservableList<CreditCard> obs = FXCollections.observableArrayList(cards);
            cardsListView.setItems(obs);
        }
    }

    /**
     * Called when the “Back” button is pressed.
     * Returns to MainMenu.fxml.
     */
    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml")
            );
            Stage stage = (Stage) cardsListView.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
