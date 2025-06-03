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
 * Profil ekranı:
 *  - Mevcut kullanıcı bilgilerini (e-posta, şifre) güncelleme
 *  - Kredi kartı ekleme/güncelleme (birden fazla kart ekleyebilme)
 *  - Geri dön butonu
 */
public class ProfileController {

    // ----- Profil alanları -----
    @FXML private TextField usernameField;      // Salt görüntüleme amaçlı
    @FXML private TextField emailField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button updateButton;
    @FXML private Label messageLabel;

    // ----- Kredi Kartı alanları -----
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryMonthField;
    @FXML private TextField expiryYearField;
    @FXML private PasswordField cvvField;
    @FXML private Button updateCardButton;
    @FXML private Button addCardButton;
    @FXML private Label cardMessageLabel;

    // ----- Ortak -----
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        int currentUserId = Session.getCurrentUserId();
        if (currentUserId <= 0) {
            System.err.println("Session.getCurrentUserId() 0 veya negatif döndü! Önce login olun.");
            return;
        }

        // Kullanıcı adını göstermek (sadece görüntüleme)
        usernameField.setText(Session.getCurrentUsername());

        // 2) Kredi kartı var mı kontrol et
        CreditCardDAO cardDao = new CreditCardDAO();
        List<CreditCard> userCards = cardDao.getAllCardsByUserId(currentUserId);

        // Eğer hiç kart yoksa: "Kart Ekle" aktif, "Kart Güncelle" pasif
        if (userCards.isEmpty()) {
            updateCardButton.setDisable(true);
            addCardButton.setDisable(false);
        } else {
            // Birden fazla kartı listeleyebileceğimiz detay ekranınız yoksa en son ekleneni veya ilkini gösterelim
            // Burada örnek olarak en son eklenenı değil, sadece ilkini doldurduk
            CreditCard existingCard = userCards.get(0);
            cardNumberField.setText(existingCard.getCardNumber());
            expiryMonthField.setText(String.valueOf(existingCard.getExpiryMonth()));
            expiryYearField.setText(String.valueOf(existingCard.getExpiryYear()));
            cvvField.setText(existingCard.getCvv());

            updateCardButton.setDisable(false);
            addCardButton.setDisable(false); // Birden fazla eklemeye izin veriyoruz
        }
    }

    // --------------- Profil Güncelleme ----------------
    @FXML
    private void handleUpdateAction(ActionEvent event) {
        int currentUserId = Session.getCurrentUserId();
        if (currentUserId <= 0) {
            messageLabel.setText("Kullanıcı bilgisi alınamadı.");
            return;
        }

        String email = emailField.getText().trim();
        String oldPwd = oldPasswordField.getText().trim();
        String newPwd = newPasswordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();

        if (email.isEmpty() || oldPwd.isEmpty() || newPwd.isEmpty() || confirm.isEmpty()) {
            messageLabel.setText("Lütfen tüm alanları doldurun.");
            return;
        }
        if (!newPwd.equals(confirm)) {
            messageLabel.setText("Yeni şifreler eşleşmiyor.");
            return;
        }

        UserDAO userDao = new UserDAO();
        boolean pwdUpdated = userDao.updatePassword(currentUserId, oldPwd, newPwd);
        if (!pwdUpdated) {
            messageLabel.setText("Eski şifre yanlış veya güncelleme başarısız.");
            return;
        }

        boolean emailUpdated = userDao.updateEmail(currentUserId, email);
        if (emailUpdated) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Profil bilgileri başarıyla güncellendi.");
        } else {
            messageLabel.setText("E-posta güncellenirken hata oluştu.");
        }
    }

    // ------------- Kartı Güncelleme ------------------
    @FXML
    private void handleCardUpdateAction(ActionEvent event) {
        int currentUserId = Session.getCurrentUserId();
        if (currentUserId <= 0) {
            cardMessageLabel.setText("Kullanıcı bilgisi alınamadı.");
            return;
        }

        String number   = cardNumberField.getText().trim();
        String monthStr = expiryMonthField.getText().trim();
        String yearStr  = expiryYearField.getText().trim();
        String cvv      = cvvField.getText().trim();

        if (number.isEmpty() || monthStr.isEmpty() || yearStr.isEmpty() || cvv.isEmpty()) {
            cardMessageLabel.setText("Lütfen tüm kart alanlarını doldurun.");
            return;
        }

        int month, year;
        try {
            month = Integer.parseInt(monthStr);
            year  = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            cardMessageLabel.setText("Ay ve yıl yalnızca rakam olmalı.");
            return;
        }

        // Burada kullanıcıya ait ilk kartı güncelliyoruz; eğer birden fazla ise, onları da listeleme ekranı eklemeniz gerekir
        CreditCard updated = new CreditCard(currentUserId, number, month, year, cvv);
        boolean success = new CreditCardDAO().updateCard(updated);

        if (success) {
            cardMessageLabel.setStyle("-fx-text-fill: green;");
            cardMessageLabel.setText("Kart başarıyla güncellendi.");
        } else {
            cardMessageLabel.setText("Kart güncelleme sırasında hata oluştu.");
        }
    }

    // --------------- Kartı Ekleme -------------------
    @FXML
    private void handleCardAddAction(ActionEvent event) {
        int currentUserId = Session.getCurrentUserId();
        if (currentUserId <= 0) {
            cardMessageLabel.setText("Kullanıcı bilgisi alınamadı.");
            return;
        }

        String number   = cardNumberField.getText().trim();
        String monthStr = expiryMonthField.getText().trim();
        String yearStr  = expiryYearField.getText().trim();
        String cvv      = cvvField.getText().trim();

        if (number.isEmpty() || monthStr.isEmpty() || yearStr.isEmpty() || cvv.isEmpty()) {
            cardMessageLabel.setText("Lütfen tüm kart alanlarını doldurun.");
            return;
        }

        int month, year;
        try {
            month = Integer.parseInt(monthStr);
            year  = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            cardMessageLabel.setText("Ay ve yıl yalnızca rakam olmalı.");
            return;
        }

        CreditCard newCard = new CreditCard(currentUserId, number, month, year, cvv);
        boolean success = new CreditCardDAO().addCard(newCard);

        if (success) {
            cardMessageLabel.setStyle("-fx-text-fill: green;");
            cardMessageLabel.setText("Yeni kart başarıyla eklendi.");
        } else {
            cardMessageLabel.setText("Kart ekleme sırasında hata oluştu.");
        }
    }

    // ----------------- Geri Dön ----------------------
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
