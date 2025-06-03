package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.CartItem;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO {

    public static CartItem getCartItemByUserAndProduct(int userId, int productId) {
        String sql = "SELECT id, user_id, product_id, quantity "
                   + "FROM cart_items "
                   + "WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id       = rs.getInt("id");
                    int qty      = rs.getInt("quantity");
                    return new CartItem(id, userId, productId, qty, null);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertCartItem(CartItem item) {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) "
                   + "VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, item.getUserId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());

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

    public static boolean updateCartItemQuantity(int cartItemId, int newQuantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newQuantity);
            ps.setInt(2, cartItemId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<CartItem> getAllCartItemsByUser(int userId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT id, user_id, product_id, quantity "
                   + "FROM cart_items "
                   + "WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id        = rs.getInt("id");
                    int productId = rs.getInt("product_id");
                    int qty       = rs.getInt("quantity");
                    list.add(new CartItem(id, userId, productId, qty, null));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartItemId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
