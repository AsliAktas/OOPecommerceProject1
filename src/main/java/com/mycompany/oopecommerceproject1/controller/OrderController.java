package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.CartItemDAO;
import com.mycompany.oopecommerceproject1.dao.CreditCardDAO;
import com.mycompany.oopecommerceproject1.dao.OrderDAO;
import com.mycompany.oopecommerceproject1.dao.OrderItemDAO;
import com.mycompany.oopecommerceproject1.dao.ProductDAO;
import com.mycompany.oopecommerceproject1.model.CartItem;
import com.mycompany.oopecommerceproject1.model.CreditCard;
import com.mycompany.oopecommerceproject1.model.Order;
import com.mycompany.oopecommerceproject1.model.OrderItem;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the “Create Order” window:
 * 1) Loads the user’s saved credit cards into a ComboBox
 * 2) Lists the items in the cart and calculates the total amount
 * 3) When “Confirm Order” is pressed:
 *    • Inserts a new Order into the orders table
 *    • Creates order_items rows for each cart item
 *    • Clears the cart_items table
 *    • If successful, shows a confirmation alert
 *    • Returns to the main menu
 * 4) When “Cancel” is pressed, returns to the main menu without making changes
 */
public class OrderController {

    @FXML private ComboBox<CreditCard> cardComboBox;
    @FXML private TableView<CartRow> cartTable;
    @FXML private TableColumn<CartRow, String> prodNameCol;
    @FXML private TableColumn<CartRow, Integer> qtyCol;
    @FXML private TableColumn<CartRow, Double> unitPriceCol;
    @FXML private TableColumn<CartRow, Double> subtotCol;
    @FXML private Label totalLabel;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    private int currentUserId = Session.getCurrentUserId();
    private double calculatedTotal = 0.0;

    @FXML
    public void initialize() {
        // 1) Load the user’s credit cards into the ComboBox
        List<CreditCard> cards = new CreditCardDAO().getAllCardsByUserId(currentUserId);
        cardComboBox.setItems(FXCollections.observableArrayList(cards));

        // 2) Bind columns to CartRow properties
        prodNameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        qtyCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        unitPriceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getUnitPrice()).asObject());
        subtotCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSubtotal()).asObject());

        // Load the cart items and total
        loadCartItems();
    }

    /**
     * Fetches the current user’s cart items, populates the TableView,
     * and calculates the total amount, updating totalLabel.
     */
    private void loadCartItems() {
        List<CartItem> items = CartItemDAO.getAllCartItemsByUser(currentUserId);
        ObservableList<CartRow> rows = FXCollections.observableArrayList();
        calculatedTotal = 0.0;

        for (CartItem ci : items) {
            Product p = ProductDAO.getProductById(ci.getProductId());
            if (p != null) {
                double unitPrice = p.getPrice();
                int quantity = ci.getQuantity();
                double subtotal = unitPrice * quantity;
                calculatedTotal += subtotal;

                rows.add(new CartRow(
                    ci.getId(),
                    p.getId(),
                    p.getName(),
                    unitPrice,
                    quantity,
                    subtotal
                ));
            }
        }
        cartTable.setItems(rows);
        totalLabel.setText(String.format("%.2f", calculatedTotal));
    }

    /**
     * Called when the “Confirm Order” button is pressed:
     * - If no card is selected, shows an error on totalLabel
     * - Creates a new Order and inserts it
     * - Creates OrderItem rows for each cart row
     * - Deletes cart items from cart_items table
     * - Shows an information alert if successful
     * - Returns to the main menu
     */
    @FXML
    private void handleConfirmOrderAction(ActionEvent event) {
        CreditCard selectedCard = cardComboBox.getSelectionModel().getSelectedItem();
        if (selectedCard == null) {
            totalLabel.setText("Select a card!");
            return;
        }

        // 1) Create a new Order object
        Order newOrder = new Order(currentUserId, selectedCard.getUserId(), calculatedTotal);
        boolean orderInserted = OrderDAO.insertOrder(newOrder);
        if (!orderInserted) {
            totalLabel.setText("Order could not be placed.");
            return;
        }

        int orderId = newOrder.getId();

        // 2) Create OrderItem entries for each CartRow
        List<CartRow> rows = new ArrayList<>(cartTable.getItems());
        for (CartRow r : rows) {
            OrderItem item = new OrderItem(orderId, r.getProductId(), r.getQuantity(), r.getUnitPrice());
            OrderItemDAO.insertOrderItem(item);
        }

        // 3) Clear the cart
        for (CartRow r : rows) {
            CartItemDAO.deleteCartItem(r.getCartItemId());
        }

        // 4) Show a success alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been placed successfully!");
        alert.showAndWait();

        // 5) Return to the main menu
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml"));
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the “Cancel” button is pressed.
     * Returns to the main menu without making any changes.
     */
    @FXML
    private void handleCancelAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/MainMenu.fxml"));
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper inner class: holds data for each row in the cart TableView.
     */
    public static class CartRow {
        private final int cartItemId;
        private final int productId;
        private final SimpleStringProperty name;
        private final SimpleDoubleProperty unitPrice;
        private final SimpleIntegerProperty quantity;
        private final SimpleDoubleProperty subtotal;

        public CartRow(int cartItemId, int productId, String name, double unitPrice, int quantity, double subtotal) {
            this.cartItemId = cartItemId;
            this.productId = productId;
            this.name = new SimpleStringProperty(name);
            this.unitPrice = new SimpleDoubleProperty(unitPrice);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.subtotal = new SimpleDoubleProperty(subtotal);
        }

        public int getCartItemId() { return cartItemId; }
        public int getProductId() { return productId; }
        public String getName() { return name.get(); }
        public SimpleStringProperty nameProperty() { return name; }
        public double getUnitPrice() { return unitPrice.get(); }
        public int getQuantity() { return quantity.get(); }
        public double getSubtotal() { return subtotal.get(); }
    }
}
