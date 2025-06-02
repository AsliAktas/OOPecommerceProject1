package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.CreditCardDAO;
import com.mycompany.oopecommerceproject1.model.CreditCard;
import com.mycompany.oopecommerceproject1.util.DBConnection;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ProfilController:
 * - Kullanıcının e-posta ve şifre bilgilerini günceller.
 * - Kullanıcının kredi kartı ekleme / güncelleme mantığını barındırır.
 * - Session üzerinden alınan userId ile CreditCardDAO çağrıları yapar.
 */
public class ProfileController {

    // --- Profil alanları ---
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updateButton;
    @FXML private Label messageLabel;
    @FXML private Button backButton;

    // --- Kredi kartı alanları ---
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryMonthField;
    @FXML private TextField expiryYearField;
    @FXML private PasswordField cvvField;
    @FXML private Button saveCardButton;
    @FXML private Label cardMessageLabel;

    /**
     * initialize(): FXML yüklendiğinde otomatik olarak çağrılır.
     * - Oturum açan kullanıcının kullanıcı adını ve e-posta bilgisini getirir.
     * - Varsa kredi kartı bilgilerini formda gösterir.
     */
    @FXML
    public void initialize() {
        // 1) Kullanıcı adı (salt okunur) ve e-posta alanını doldur
        String currentUser = Session.getCurrentUsername();
        usernameField.setText(currentUser);

        String emailSql = "SELECT email FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(emailSql)) {

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

        // 2) Kredi kartı bilgisini yükle (user_id üzerinden)
        int userId = Session.getCurrentUserId();
        loadCreditCardInfo(userId);
    }

    /**
     * handleUpdateAction(): “Profil Güncelle” butonuna tıklandığında çağrılır.
     * - E-posta veya şifre güncelleme işlemini yapar.
     */
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
            // 1) Eski şifre kontrolü
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

            // 2) Yeni şifre / tekrar validasyonu
            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                messageLabel.setText("Yeni şifreyi ve tekrarını doldurun.");
                return;
            }
            if (!newPass.equals(confirmPass)) {
                messageLabel.setText("Yeni şifreler eşleşmiyor.");
                return;
            }
        }

        // 3) UPDATE sorgusunu hazırlama
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

    /**
     * loadCreditCardInfo(int userId):
     * Veritabanından userId'ye ait kartı getirir ve form alanlarına yerleştirir.
     */
    private void loadCreditCardInfo(int userId) {
        CreditCard card = CreditCardDAO.getCardByUserId(userId);
        if (card != null) {
            cardNumberField.setText(card.getCardNumber());
            expiryMonthField.setText(String.valueOf(card.getExpiryMonth()));
            expiryYearField.setText(String.valueOf(card.getExpiryYear()));
            cvvField.setText(card.getCvv());
            saveCardButton.setText("Kartı Güncelle");
        } else {
            saveCardButton.setText("Kartı Kaydet");
        }
    }

    /**
     * handleCardSaveAction(): “Kartı Kaydet” / “Kartı Güncelle” butonuna tıklandığında çağrılır.
     * - Kart numarası, ay, yıl ve CVV validasyonunu yapar.
     * - Mevcut karta göre insert veya update işlemi yaptırtır.
     */
    @FXML
    private void handleCardSaveAction(ActionEvent event) {
        int userId = Session.getCurrentUserId();
        String cardNum = cardNumberField.getText().trim();
        String monthText = expiryMonthField.getText().trim();
        String yearText = expiryYearField.getText().trim();
        String cvv = cvvField.getText().trim();

        // Tüm alanların doluluğunu kontrol et
        if (cardNum.isEmpty() || monthText.isEmpty() || yearText.isEmpty() || cvv.isEmpty()) {
            cardMessageLabel.setStyle("-fx-text-fill: red;");
            cardMessageLabel.setText("Tüm kredi kartı alanlarını doldurun.");
            return;
        }

        int month, year;
        try {
            month = Integer.parseInt(monthText);
            year = Integer.parseInt(yearText);
            if (month < 1 || month > 12) {
                cardMessageLabel.setStyle("-fx-text-fill: red;");
                cardMessageLabel.setText("Geçerli bir ay girin (1-12).");
                return;
            }
            if (year < 2025) {
                cardMessageLabel.setStyle("-fx-text-fill: red;");
                cardMessageLabel.setText("Geçerli bir yıl girin (>=2025).");
                return;
            }
        } catch (NumberFormatException ex) {
            cardMessageLabel.setStyle("-fx-text-fill: red;");
            cardMessageLabel.setText("Ay ve yıl sayısal olmalı.");
            return;
        }

        // CreditCard nesnesini oluştur
        CreditCard card = new CreditCard(userId, cardNum, month, year, cvv);

        // Mevcut kart var mı kontrol et
        CreditCard existing = CreditCardDAO.getCardByUserId(userId);
        boolean success;
        if (existing == null) {
            // Yeni kart ekleme
            success = CreditCardDAO.insertCard(card);
        } else {
            // Mevcut kartı güncelleme
            success = CreditCardDAO.updateCard(card);
        }

        if (success) {
            cardMessageLabel.setStyle("-fx-text-fill: green;");
            cardMessageLabel.setText("Kredi kartı bilgileri güncellendi.");
            saveCardButton.setText("Kartı Güncelle");
        } else {
            cardMessageLabel.setStyle("-fx-text-fill: red;");
            cardMessageLabel.setText("Kart bilgileri kaydedilemedi.");
        }
    }

    /**
     * handleBackAction(): “Geri Dön” butonuna tıklandığında çağrılır.
     * - Ana Menü ekranını yeniden yükler.
     */
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
