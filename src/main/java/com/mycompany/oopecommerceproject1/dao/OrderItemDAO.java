package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.OrderItem;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;

/**
 * DAO class for the order_items table:
 * The table schema is assumed as:
 *
 * CREATE TABLE order_items (
 *   id INT AUTO_INCREMENT PRIMARY KEY,
 *   order_id INT NOT NULL,     -- REFERENCES orders(id)
 *   product_id INT NOT NULL,   -- REFERENCES products(id)
 *   quantity INT NOT NULL,
 *   unit_price DOUBLE NOT NULL
 * );
 */
public class OrderItemDAO {

    /**
     * Inserts a new OrderItem into the order_items table.
     * Sets the generated ID back into the OrderItem object.
     * @param item New OrderItem (orderId, productId, quantity, unitPrice must be set)
     * @return true if insertion succeeds, false otherwise
     */
    public static boolean insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());
            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            // Assign generated ID back to the object
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    item.setId(keys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
