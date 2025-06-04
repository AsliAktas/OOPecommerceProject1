package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.FavoriteDAO;
import com.mycompany.oopecommerceproject1.model.Product;
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

/**
 * “Favorilerim” ekranının controller’ı.
 * Oturum açan kullanıcının favori ürünlerini listeleyip yönetir.
 */
public class FavoritesController {

    @FXML private TableView<Product> favoritesTable;
    @FXML private TableColumn<Product, Integer> favIdColumn;
    @FXML private TableColumn<Product, String>  favNameColumn;
    @FXML private TableColumn<Product, Double>  favPriceColumn;
    @FXML private Button removeFavButton;
    @FXML private Button backButton;

    // Oturum açan kullanıcının ID’si
    private final int currentUserId = Session.getCurrentUserId();

    @FXML
    public void initialize() {
        // (A) Sütunları tablo model alanlarıyla eşle
        favIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        favNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        favPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // (B) Favorileri yükle (bu metod içinde tablo yüksekliği de ayarlanacak)
        loadFavorites();
    }

    private void loadFavorites() {
        // 1) DAO’dan favori ürün listesini al
        List<Product> favProducts = FavoriteDAO.getAllFavoritesByUser(currentUserId);

        // 2) Debug: gerçekten gelen sayıyı ve ID’leri konsola yazdır
        System.out.println(">> DAO’dan dönen favori sayısı: " + favProducts.size());
        for (Product p : favProducts) {
            System.out.println("   - Favori Ürün ID = " + p.getId() + ", Adı = " + p.getName());
        }

        // 3) TableView’in satır yüksekliğini sabitle (örneğin 28 px)
        favoritesTable.setFixedCellSize(28);

        // 4) Tablo yükseklik ayarı: "satır sayısı × 28 + 32 ek"
        double newHeight = favProducts.size() * 28 + 32;
        favoritesTable.setPrefHeight(newHeight);

        // 5) Son olarak ObservableList’e dönüştür ve tabloya ata
        ObservableList<Product> obs = FXCollections.observableArrayList(favProducts);
        favoritesTable.setItems(obs);
    }

    @FXML
    private void handleRemoveFavoriteAction(ActionEvent event) {
        Product selected = favoritesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            FavoriteDAO.removeFavorite(currentUserId, selected.getId());
            // Silme sonrası listeyi yeniden yükle (yine boyutlandıracak)
            loadFavorites();
        }
    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            // Burada örnek olarak 600×500 gibi biraz daha geniş bir Scene açabilirsiniz:
            stage.setScene(new Scene(root, 600, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
