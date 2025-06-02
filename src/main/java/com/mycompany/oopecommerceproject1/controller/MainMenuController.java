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

public class MainMenuController {

    @FXML private Label welcomeLabel;
    @FXML private Button productListButton;
    @FXML private Button profileButton;

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleProductListAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/com/mycompany/oopecommerceproject1/view/ProductList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) productListButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfileAction(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass()
            .getResource("/com/mycompany/oopecommerceproject1/view/Profile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) profileButton.getScene().getWindow();
        stage.setScene(new Scene(root, 500, 400));
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
