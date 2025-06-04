package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.Order;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * “orders” tablosuna yeni sipariş eklemek ve sipariş bilgisi getirmek için DAO.
 */
public class OrderDAO {

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

    /**
     * Verilen kullanıcıya ait tüm siparişleri döner.
     */
    public static List<Order> getAllOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, user_id, card_id, total_amount, order_date FROM orders WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("card_id"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("order_date")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
