package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.util.DBConnection;
import com.mycompany.oopecommerceproject1.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updateButton;
    @FXML private Label messageLabel;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        String currentUser = Session.getCurrentUsername();
        usernameField.setText(currentUser);

        String sql = "SELECT email FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, currentUser);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    emailField.setText(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Profil bilgileri yüklenirken hata oluştu.");
        }
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        String currentUser = Session.getCurrentUsername();
        String newEmail = emailField.getText().trim();
        String oldPass = oldPasswordField.getText().trim();
        String newPass = newPasswordField.getText().trim();
        String confirmPass = confirmPasswordField.getText().trim();

        if (newEmail.isEmpty()) {
            messageLabel.setText("E-posta boş bırakılamaz.");
            return;
        }

        boolean changePassword = false;
        if (!oldPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty()) {
            changePassword = true;
            String checkSql = "SELECT password FROM users WHERE username = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(checkSql)) {

                stmt.setString(1, currentUser);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String existingPass = rs.getString("password");
                        if (!existingPass.equals(oldPass)) {
                            messageLabel.setText("Eski şifre yanlış.");
                            return;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                messageLabel.setText("Şifre doğrulanırken hata oluştu.");
                return;
            }

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                messageLabel.setText("Yeni şifreyi ve tekrarını doldurun.");
                return;
            }
            if (!newPass.equals(confirmPass)) {
                messageLabel.setText("Yeni şifreler eşleşmiyor.");
                return;
            }
        }

        String updateSql;
        if (changePassword) {
            updateSql = "UPDATE users SET email = ?, password = ? WHERE username = ?";
        } else {
            updateSql = "UPDATE users SET email = ? WHERE username = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, newEmail);
            if (changePassword) {
                stmt.setString(2, newPass);
                stmt.setString(3, currentUser);
            } else {
                stmt.setString(2, currentUser);
            }

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Profil güncellendi.");
                oldPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Güncelleme başarısız.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Veritabanı hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

