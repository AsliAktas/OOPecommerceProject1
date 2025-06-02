package com.mycompany.oopecommerceproject1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainMenuController {

    @FXML private Label welcomeLabel;

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        System.exit(0);
    }
}
