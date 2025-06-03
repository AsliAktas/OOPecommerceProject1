package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.Order;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;

/**
 * “orders” tablosuna yeni sipariş eklemek ve sipariş bilgisi getirmek için DAO.
 * Aşağıdaki tablo yapısı varsayılıyor:
 *
 * CREATE TABLE orders (
 *   id INT AUTO_INCREMENT PRIMARY KEY,
 *   user_id INT NOT NULL,       -- users(id) foreign key
 *   card_id INT NOT NULL,       -- credit_cards(id) foreign key
 *   total_amount DOUBLE NOT NULL,
 *   order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 * );
 */
public class OrderDAO {

    /**
     * Verilen Order objesini DB’de “orders” tablosuna ekler.
     * Sipariş eklendikten sonra objenin id’si set edilir.
     * @param order Yeni sipariş nesnesi (id ve orderDate null)
     * @return Başarılıysa true, aksi halde false.
     */
    public static boolean insertOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, card_id, total_amount) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getCardId());
            ps.setDouble(3, order.getTotalAmount());
            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setId(keys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verilen orderId’ye ait Order bilgisini döner; bulunamazsa null.
     */
    public static Order getOrderById(int orderId) {
        String sql = "SELECT id, user_id, card_id, total_amount, order_date FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("card_id"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("order_date")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

