package com.mycompany.oopecommerceproject1.controller;

import com.mycompany.oopecommerceproject1.dao.OrderDAO;
import com.mycompany.oopecommerceproject1.model.Order;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller for the “Order History” screen:
 * - Lists all orders of the logged-in user in a table
 * - “Back” button returns to the main menu
 */
public class OrderHistoryController {

    @FXML private TableView<OrderRow> ordersTable;
    @FXML private TableColumn<OrderRow, Integer> orderIdColumn;
    @FXML private TableColumn<OrderRow, String> dateColumn;
    @FXML private TableColumn<OrderRow, Double> amountColumn;
    @FXML private javafx.scene.control.Button backButton;

    private final int currentUserId = Session.getCurrentUserId();

    @FXML
    public void initialize() {
        // 1) Set up how each column gets its cell data
        orderIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getOrderId()).asObject());
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());
        amountColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalAmount()).asObject());

        // 2) Load the order history into the table
        loadOrderHistory();
    }

    /**
     * Fetches all orders for the current user, formats their dates,
     * and populates the TableView.
     */
    private void loadOrderHistory() {
        List<Order> orders = OrderDAO.getAllOrdersByUser(currentUserId);
        ObservableList<OrderRow> rows = FXCollections.observableArrayList();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Order o : orders) {
            String formattedDate = sdf.format(o.getOrderDate());
            rows.add(new OrderRow(
                o.getId(),
                formattedDate,
                o.getTotalAmount()
            ));
        }
        ordersTable.setItems(rows);
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
            stage.setScene(new Scene(root, 400, 350));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper inner class: holds data for each row in the order history TableView.
     */
    public static class OrderRow {
        private final int orderId;
        private final SimpleStringProperty date;
        private final SimpleDoubleProperty totalAmount;

        public OrderRow(int orderId, String date, double totalAmount) {
            this.orderId = orderId;
            this.date = new SimpleStringProperty(date);
            this.totalAmount = new SimpleDoubleProperty(totalAmount);
        }

        public int getOrderId() {
            return orderId;
        }
        public String getDate() {
            return date.get();
        }
        public SimpleStringProperty dateProperty() {
            return date;
        }
        public double getTotalAmount() {
            return totalAmount.get();
        }
    }
}
