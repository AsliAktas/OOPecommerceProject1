package com.mycompany.oopecommerceproject1.controller;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for the welcome window (“Welcome screen”).
 * - When “View Products” is clicked, opens ProductList.fxml in a new Stage
 * - If additional buttons are needed in the future (e.g., “View Cart”), similar @FXML handlers can be added
 */
public class WelcomeController {

    /**
     * Called when the “View Products” button is pressed.
     * Opens ProductList.fxml in a new window.
     */
    @FXML
    private void handleViewProductsAction(ActionEvent event) {
        try {
            // Get the path for ProductList.fxml under src/main/resources
            URL fxmlUrl = getClass().getResource(
                "/com/mycompany/oopecommerceproject1/view/ProductList.fxml"
            );
            if (fxmlUrl == null) {
                throw new IOException("ProductList.fxml not found on classpath");
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = new Stage();
            stage.setTitle("Product List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not open Product List screen.");
        }
    }

    // If “View Cart” or other buttons are added in the future, similar @FXML handler methods can be defined here.
}
