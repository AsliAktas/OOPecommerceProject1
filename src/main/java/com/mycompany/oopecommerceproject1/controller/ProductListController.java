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
 * Ürün listesini gösterir. Her satırda:
 *   • “Sepete Ekle”
 *   • “Sepetten Kaldır”
 *   • “Favori Ekle/Kaldır” butonları var.
 * Ayrıca altta “Sepetimi Gör” butonu bulunuyor.
 */
public class ProductListController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> stockColumn;
    @FXML private TableColumn<Product, Void> addColumn;       // “Sepete Ekle”
    @FXML private TableColumn<Product, Void> removeColumn;    // “Sepetten Kaldır”
    @FXML private TableColumn<Product, Void> favoriteColumn;  // “Favori Ekle/Kaldır”

    @FXML private Button backButton;
    @FXML private Button viewCartButton;  // “Sepetimi Gör” butonu

    private final int currentUserId = Session.getCurrentUserId();

    @FXML
    public void initialize() {
        // 1) Temel sütunların PropertyValueFactory ile eşlenmesi
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // 2) “Sepete Ekle” butonu için hücre fabrikası
        addColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Sepete Ekle");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product prod = getTableView().getItems().get(getIndex());
                            int productId = prod.getId();

                            // Eğer stok 0 ise hiçbir şey yapma
                            if (prod.getStock() <= 0) {
                                return;
                            }

                            // 1) Sepette zaten varsa miktarı artır, yoksa yeni satır ekle
                            CartItem existing = CartItemDAO.getCartItemByUserAndProduct(currentUserId, productId);
                            if (existing != null) {
                                int yeniMiktar = existing.getQuantity() + 1;
                                CartItemDAO.updateCartItemQuantity(existing.getId(), yeniMiktar);
                            } else {
                                CartItemDAO.insertCartItem(new CartItem(currentUserId, productId, 1));
                            }

                            // 2) Ürünün stoğunu 1 azalt
                            prod.setStock(prod.getStock() - 1);
                            ProductDAO.updateProduct(prod);

                            // 3) Tabloyu yeniden yükle (stok ve buton durumu güncellenir)
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
                            // Eğer stok 0 ise “Ekle” butonu pasif
                            btn.setDisable(p.getStock() <= 0);
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

        // 3) “Sepetten Kaldır” butonu için hücre fabrikası
        removeColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Sepetten Kaldır");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product prod = getTableView().getItems().get(getIndex());
                            int productId = prod.getId();

                            // Sepette mevcut mu kontrol et
                            CartItem existing = CartItemDAO.getCartItemByUserAndProduct(currentUserId, productId);
                            if (existing == null) {
                                // Sepette yoksa hiçbir şey yapma
                                return;
                            }

                            // 1) Sepet kaydını sil veya miktarı 1 azalt
                            if (existing.getQuantity() > 1) {
                                CartItemDAO.updateCartItemQuantity(existing.getId(), existing.getQuantity() - 1);
                            } else {
                                CartItemDAO.deleteCartItem(existing.getId());
                            }

                            // 2) Ürünün stoğunu 1 artır
                            prod.setStock(prod.getStock() + 1);
                            ProductDAO.updateProduct(prod);

                            // 3) Tabloyu yeniden yükle
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
                            // Sepette bu üründen kaç adet var?
                            CartItem existing = CartItemDAO.getCartItemByUserAndProduct(currentUserId, p.getId());
                            // Eğer sepette yoksa buton pasif, varsa aktif
                            btn.setDisable(existing == null);
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

        // 4) “Favori Ekle/Kaldır” butonu için hücre fabrikası
        favoriteColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product prod = getTableView().getItems().get(getIndex());
                            int productId = prod.getId();

                            boolean zatenFavori = FavoriteDAO.isFavorite(currentUserId, productId);
                            if (zatenFavori) {
                                // Favoriden kaldır
                                FavoriteDAO.removeFavorite(currentUserId, productId);
                            } else {
                                // Favoriye ekle
                                FavoriteDAO.addFavorite(currentUserId, productId);
                            }
                            // Buton metnini güncelle
                            updateButtonText(prod);
                        });
                        setPadding(new Insets(2, 2, 2, 2));
                    }

                    private void updateButtonText(Product p) {
                        boolean fav = FavoriteDAO.isFavorite(currentUserId, p.getId());
                        btn.setText(fav ? "★" : "☆"); // “★” favoride, “☆” değil
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Product p = getTableView().getItems().get(getIndex());
                            // Buton metnini favori durumuna göre ayarla
                            updateButtonText(p);
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

        // 5) Ürünleri tabloya yükle
        loadProducts();
    }

    /** Ürünleri DB’den çekip tabloya koyan yardımcı metot */
    private void loadProducts() {
        List<Product> productList = ProductDAO.getAllProducts();
        ObservableList<Product> products = FXCollections.observableArrayList(productList);
        productTable.setItems(products);
    }

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
