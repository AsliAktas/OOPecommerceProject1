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
 * Controller for the “My Favorites” screen.
 * - Lists the current user’s favorite products
 * - Allows removing a selected product from favorites
 * - Handles “Back” button to return to the main menu
 */
public class FavoritesController {

    @FXML private TableView<Product> favoritesTable;
    @FXML private TableColumn<Product, Integer> favIdColumn;
    @FXML private TableColumn<Product, String>  favNameColumn;
    @FXML private TableColumn<Product, Double>  favPriceColumn;
    @FXML private Button removeFavButton;
    @FXML private Button backButton;

    // Current logged-in user’s ID
    private final int currentUserId = Session.getCurrentUserId();

    @FXML
    public void initialize() {
        // (A) Map table columns to Product fields
        favIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        favNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        favPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // (B) Load favorites into the table (this method also adjusts table height)
        loadFavorites();
    }

    /**
     * Fetches all favorite products for the current user and populates the TableView.
     * Also adjusts table height based on number of rows.
     */
    private void loadFavorites() {
        // 1) Get list of favorite products from DAO
        List<Product> favProducts = FavoriteDAO.getAllFavoritesByUser(currentUserId);

        // 2) Debug: print count and IDs to console
        System.out.println(">> Number of favorites returned by DAO: " + favProducts.size());
        for (Product p : favProducts) {
            System.out.println("   - Favorite Product ID = " + p.getId() + ", Name = " + p.getName());
        }

        // 3) Fix cell height (e.g., 28 px)
        favoritesTable.setFixedCellSize(28);

        // 4) Set table height: (row count × 28) + 32 padding
        double newHeight = favProducts.size() * 28 + 32;
        favoritesTable.setPrefHeight(newHeight);

        // 5) Convert to ObservableList and set items
        ObservableList<Product> obs = FXCollections.observableArrayList(favProducts);
        favoritesTable.setItems(obs);
    }

    /**
     * Removes the selected product from favorites and reloads the table.
     */
    @FXML
    private void handleRemoveFavoriteAction(ActionEvent event) {
        Product selected = favoritesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            FavoriteDAO.removeFavorite(currentUserId, selected.getId());
            // Reload the list (and adjust size)
            loadFavorites();
        }
    }

    /**
     * Called when the “Back” button is pressed.
     * Returns to the MainMenu screen.
     */
    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            // For example, open MainMenu in a 600×500 window
            stage.setScene(new Scene(root, 600, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
