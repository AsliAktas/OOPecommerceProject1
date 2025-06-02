// src/main/java/com/mycompany/oopecommerceproject1/controller/MyCardsController.java

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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MyCardsController {

    @FXML private TableView<CreditCard> cardsTable;
    @FXML private TableColumn<CreditCard, Integer> idColumn;
    @FXML private TableColumn<CreditCard, String> cardNumberColumn;
    @FXML private TableColumn<CreditCard, Integer> monthColumn;
    @FXML private TableColumn<CreditCard, Integer> yearColumn;
    @FXML private TableColumn<CreditCard, String> cvvColumn;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        // 1) Sütunları model alanlarıyla eşle
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("expiryMonth"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("expiryYear"));
        cvvColumn.setCellValueFactory(new PropertyValueFactory<>("cvv"));

        // 2) Veritabanından o anki kullanıcıya ait tüm kartları çek
        int userId = Session.getCurrentUserId();
        List<CreditCard> list = CreditCardDAO.getAllCardsByUserId(userId);

        // 3) ObservableList’e dönüştür ve tabloya ata
        ObservableList<CreditCard> data = FXCollections.observableArrayList(list);
        cardsTable.setItems(data);
    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            // Dilerseniz Profil ekranına dönebilirsiniz. Burada ana menüye dönme örneği var:
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

