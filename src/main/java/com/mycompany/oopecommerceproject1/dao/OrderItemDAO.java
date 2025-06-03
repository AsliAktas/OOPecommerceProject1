package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.OrderItem;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;

/**
 * “order_items” tablosuna satır eklemek için DAO.
 * Aşağıdaki tablo yapısı varsayılıyor:
 *
 * CREATE TABLE order_items (
 *   id INT AUTO_INCREMENT PRIMARY KEY,
 *   order_id INT NOT NULL,     -- orders(id) foreign key
 *   product_id INT NOT NULL,   -- products(id) foreign key
 *   quantity INT NOT NULL,
 *   unit_price DOUBLE NOT NULL
 * );
 */
public class OrderItemDAO {

    /**
     * Verilen OrderItem nesnesini “order_items” tablosuna ekler.
     * Eklenen satırın id’si objeye set edilir.
     * @param item Yeni sipariş kalemi (orderId, productId, quantity, unitPrice dolu)
     * @return Başarılıysa true, aksi halde false.
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

