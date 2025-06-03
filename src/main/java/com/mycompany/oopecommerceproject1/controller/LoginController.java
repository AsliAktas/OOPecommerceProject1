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

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("Lütfen kullanıcı adı ve şifre girin.");
            return;
        }

        UserDAO userDao = new UserDAO();
        User found = userDao.findByUsernameAndPassword(user, pass);

        if (found != null) {
            // Oturuma kullanıcı adını ve ID'sini kaydet:
            Session.setCurrentUsername(found.getUsername());

            try {
                // Giriş başarılı → Ana menü ekranını açalım
                Parent root = FXMLLoader.load(
                    getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml")
                );
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Geçersiz kullanıcı adı veya şifre.");
        }
    }
}
