
package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label messageLabel;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Kullanıcı adı ve şifre boş bırakılamaz.");
            return;
        }

        if (authenticateUser(username, password)) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Giriş başarılı!");
            // TODO: Başarılıysa bir sonraki ekrana geçiş kodunu ekle
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Kullanıcı adı veya şifre hatalı.");
        }
    }

    private boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
