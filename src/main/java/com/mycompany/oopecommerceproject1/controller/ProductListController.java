package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.CartItemDAO;
import com.mycompany.oopecommerceproject1.dao.FavoriteDAO;
import com.mycompany.oopecommerceproject1.dao.ProductDAO;
import com.mycompany.oopecommerceproject1.model.CartItem;
import com.mycompany.oopecommerceproject1.model.Product;
import com.mycompany.oopecommerceproject1.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the “Product List” screen:
 * - Loads all products into a table
 * - Provides “Add to Cart”, “Remove from Cart”, and “Favorite” buttons dynamically for each row
 * - Bottom has “Back” and “View Cart” buttons for navigation
 */
public class ProductListController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> stockColumn;
    @FXML private TableColumn<Product, Void> addColumn;       // “Add to Cart”
    @FXML private TableColumn<Product, Void> removeColumn;    // “Remove from Cart”
    @FXML private TableColumn<Product, Void> favoriteColumn;  // “Favorite” button

    @FXML private Button backButton;
    @FXML private Button viewCartButton;  // “View Cart” button

    private final int currentUserId = Session.getCurrentUserId();

    @FXML
    public void initialize() {
        // 1) Bind basic columns to Product fields using PropertyValueFactory
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // 2) Cell factory for “Add to Cart” button
        addColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Add to Cart");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product prod = getTableView().getItems().get(getIndex());
                            int productId = prod.getId();

                            // If stock is 0, do nothing
                            if (prod.getStock() <= 0) {
                                return;
                            }

                            // 1) If already in cart, increment quantity; otherwise insert new
                            CartItem existing = CartItemDAO.getCartItemByUserAndProduct(currentUserId, productId);
                            if (existing != null) {
                                int newQuantity = existing.getQuantity() + 1;
                                CartItemDAO.updateCartItemQuantity(existing.getId(), newQuantity);
                            } else {
                                CartItemDAO.insertCartItem(new CartItem(currentUserId, productId, 1));
                            }

                            // 2) Decrement product stock by 1
                            prod.setStock(prod.getStock() - 1);
                            ProductDAO.updateProduct(prod);

                            // 3) Reload products to update stock and button states
                            loadProducts();
                        });
                        setPadding(new Insets(2, 2, 2, 2));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Product p = getTableView().getItems().get(getIndex());
                            // Disable “Add to Cart” if stock is 0
                            btn.setDisable(p.getStock() <= 0);
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

        // 3) Cell factory for “Remove from Cart” button
        removeColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Remove from Cart");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product prod = getTableView().getItems().get(getIndex());
                            int productId = prod.getId();

                            // Check if this product is in the cart
                            CartItem existing = CartItemDAO.getCartItemByUserAndProduct(currentUserId, productId);
                            if (existing == null) {
                                // If not in cart, do nothing
                                return;
                            }

                            // 1) If quantity > 1, decrement; otherwise delete record
                            if (existing.getQuantity() > 1) {
                                CartItemDAO.updateCartItemQuantity(existing.getId(), existing.getQuantity() - 1);
                            } else {
                                CartItemDAO.deleteCartItem(existing.getId());
                            }

                            // 2) Increment product stock by 1
                            prod.setStock(prod.getStock() + 1);
                            ProductDAO.updateProduct(prod);

                            // 3) Reload products
                            loadProducts();
                        });
                        setPadding(new Insets(2, 2, 2, 2));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Product p = getTableView().getItems().get(getIndex());
                            // Enable button only if this product is in the cart
                            CartItem existing = CartItemDAO.getCartItemByUserAndProduct(currentUserId, p.getId());
                            btn.setDisable(existing == null);
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

        // 4) Cell factory for “Favorite” button
        favoriteColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product prod = getTableView().getItems().get(getIndex());
                            int productId = prod.getId();

                            boolean alreadyFav = FavoriteDAO.isFavorite(currentUserId, productId);
                            if (alreadyFav) {
                                // Remove from favorites if already favorited
                                FavoriteDAO.removeFavorite(currentUserId, productId);
                            } else {
                                // Add to favorites otherwise
                                FavoriteDAO.addFavorite(currentUserId, productId);
                            }
                            // Update button text based on favorite status
                            updateButtonText(prod);
                        });
                        setPadding(new Insets(2, 2, 2, 2));
                    }

                    /**
                     * Updates the button text depending on whether the product
                     * is already in the favorites. “★” = favorited, “☆” = not favorited.
                     */
                    private void updateButtonText(Product p) {
                        boolean fav = FavoriteDAO.isFavorite(currentUserId, p.getId());
                        btn.setText(fav ? "★" : "☆");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Product p = getTableView().getItems().get(getIndex());
                            updateButtonText(p);
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

        // 5) Finally, load all products into the table
        loadProducts();
    }

    /**
     * Retrieves all products from the database and displays them in the TableView.
     */
    private void loadProducts() {
        List<Product> productList = ProductDAO.getAllProducts();
        ObservableList<Product> products = FXCollections.observableArrayList(productList);
        productTable.setItems(products);
    }

    /**
     * Called when the “Back” button is pressed.
     * Returns to MainMenu.fxml.
     */
    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 350));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the “View Cart” button is pressed.
     * Opens Cart.fxml.
     */
    @FXML
    private void handleViewCartAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/Cart.fxml"));
            Stage stage = (Stage) viewCartButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
