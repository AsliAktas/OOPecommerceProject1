package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.CartItemDAO;
import com.mycompany.oopecommerceproject1.dao.ProductDAO;
import com.mycompany.oopecommerceproject1.model.CartItem;
import com.mycompany.oopecommerceproject1.model.Product;
import com.mycompany.oopecommerceproject1.util.Session;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Sepet ekranı:
 *  - Kullanıcının sepete eklediği ürünleri listeler
 *  - “Kaldır” butonuna tıklanınca sepetten sil ve stok+1 yap
 */
public class CartController {

    @FXML private TableView<CartRow> cartTable;
    @FXML private TableColumn<CartRow, String> prodNameCol;
    @FXML private TableColumn<CartRow, Integer> qtyCol;
    @FXML private TableColumn<CartRow, Double> subtotCol;
    @FXML private TableColumn<CartRow, Void> removeCol; // “Kaldır” sütunu
    @FXML private Button closeButton;

    private int currentUserId = Session.getCurrentUserId();

    @FXML
    public void initialize() {
        // 1) Sütunların hücre değerlerini sağlayacak callback’leri tanımla
        prodNameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        qtyCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        subtotCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSubtotal()).asObject());

        // 2) “Kaldır” butonu için hücre fabrikası
        removeCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<CartRow, Void> call(final TableColumn<CartRow, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Kaldır");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            CartRow row = getTableView().getItems().get(getIndex());

                            // 1) Sepet kaydını sil
                            CartItemDAO.deleteCartItem(row.getCartItemId());

                            // 2) Ürünü stok tablosuna geri ekle (stok+1)
                            Product p = ProductDAO.getProductById(row.getProductId());
                            if (p != null) {
                                p.setStock(p.getStock() + 1);
                                ProductDAO.updateProduct(p);
                            }

                            // 3) Tabloyu yeniden yükle
                            loadCartItems();
                        });
                        setPadding(new Insets(2, 2, 2, 2));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

        // 3) Sepet öğelerini yükle
        loadCartItems();
    }

    private void loadCartItems() {
        List<CartItem> items = CartItemDAO.getAllCartItemsByUser(currentUserId);
        ObservableList<CartRow> rows = FXCollections.observableArrayList();

        for (CartItem ci : items) {
            Product p = ProductDAO.getProductById(ci.getProductId());
            if (p != null) {
                rows.add(new CartRow(
                    ci.getId(),
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    ci.getQuantity()
                ));
            }
        }
        cartTable.setItems(rows);
    }

    @FXML
    private void handleCloseAction(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Yardımcı iç sınıf: Tablo sütunlarına veri sağlar.
     */
    public static class CartRow {
        private final int cartItemId;
        private final int productId;
        private final SimpleStringProperty name;
        private final SimpleDoubleProperty price;
        private final SimpleIntegerProperty quantity;
        private final SimpleDoubleProperty subtotal;

        public CartRow(int cartItemId, int productId, String name, double price, int quantity) {
            this.cartItemId = cartItemId;
            this.productId = productId;
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleDoubleProperty(price);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.subtotal = new SimpleDoubleProperty(price * quantity);
        }

        public int getCartItemId() {
            return cartItemId;
        }
        public int getProductId() {
            return productId;
        }
        public String getName() {
            return name.get();
        }
        public SimpleStringProperty nameProperty() {
            return name;
        }
        public double getPrice() {
            return price.get();
        }
        public int getQuantity() {
            return quantity.get();
        }
        public double getSubtotal() {
            return subtotal.get();
        }
    }
}
