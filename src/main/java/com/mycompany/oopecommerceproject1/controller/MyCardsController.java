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
 * Kullanıcının sahip olduğu tüm kartları listeler.
 */
public class MyCardsController {

    @FXML private ListView<CreditCard> cardsListView;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        int currentUserId = Session.getCurrentUserId();
        if (currentUserId <= 0) {
            messageLabel.setText("Önce giriş yapmalısınız.");
            return;
        }

        CreditCardDAO cardDao = new CreditCardDAO();
        List<CreditCard> kartlar = cardDao.getAllCardsByUserId(currentUserId);

        if (kartlar.isEmpty()) {
            messageLabel.setText("Henüz kayıtlı kartınız yok.");
        } else {
            ObservableList<CreditCard> obs = FXCollections.observableArrayList(kartlar);
            cardsListView.setItems(obs);
        }
    }

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
