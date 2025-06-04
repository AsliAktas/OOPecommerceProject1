package com.mycompany.oopecommerceproject1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the application’s main menu.
 * - Provides buttons to navigate to different sections (Product List, Profile, Create Order, etc.)
 * - “Logout” button exits the application
 */
public class MainMenuController {

    @FXML private Label welcomeLabel;
    @FXML private Button productListButton;
    @FXML private Button profileButton;
    @FXML private Button myCardsButton;
    @FXML private Button createOrderButton;
    @FXML private Button orderHistoryButton;   // “Order History” button

    /**
     * Called when the “Logout” button is pressed.
     * Exits the application.
     */
    @FXML
    private void handleLogoutAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Called when the “Product List” button is pressed.
     * Loads ProductList.fxml.
     */
    @FXML
    private void handleProductListAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/ProductList.fxml"));
            Stage stage = (Stage) productListButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 450));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the “Profile” button is pressed.
     * Loads Profile.fxml.
     */
    @FXML
    private void handleProfileAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/Profile.fxml"));
            Stage stage = (Stage) profileButton.getScene().getWindow();
            // Profile screen size is set to 500×520
            stage.setScene(new Scene(root, 500, 520));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the “Create Order” button is pressed.
     * Loads Order.fxml.
     */
    @FXML
    private void handleCreateOrderAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/Order.fxml"));
            Stage stage = (Stage) createOrderButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 450));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the “Order History” button is pressed.
     * Loads OrderHistory.fxml.
     */
    @FXML
    private void handleOrderHistoryAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/OrderHistory.fxml"));
            Stage stage = (Stage) orderHistoryButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 450));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the “My Cards” button is pressed.
     * Loads MyCards.fxml.
     */
    @FXML
    private void handleMyCardsAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/MyCards.fxml"));
            Stage stage = (Stage) myCardsButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
