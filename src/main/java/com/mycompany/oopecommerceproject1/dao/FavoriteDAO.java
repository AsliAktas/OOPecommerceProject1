package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.Favorite;
import com.mycompany.oopecommerceproject1.util.DBConnection;
import com.mycompany.oopecommerceproject1.model.Product;
import com.mycompany.oopecommerceproject1.dao.ProductDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing user favorites:
 * - isFavorite: checks if a record exists for userId+productId
 * - addFavorite: inserts a new favorite
 * - removeFavorite: deletes a favorite record
 * - getAllFavoritesByUser: returns a list of Product objects for a given user
 */
public class FavoriteDAO {

    /**
     * Checks if a favorite record exists for the given userId and productId.
     * @return true if already favorited, false otherwise
     */
    public static boolean isFavorite(int userId, int productId) {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a new favorite record.
     * @return true if insertion succeeds, false otherwise
     */
    public static boolean addFavorite(int userId, int productId) {
        String sql = "INSERT INTO favorites (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            // Unique constraint violation if already exists, or other error
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes the favorite record for the given userId and productId.
     * @return true if deletion succeeds, false otherwise
     */
    public static boolean removeFavorite(int userId, int productId) {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all favorite products for the given user.
     * @return List<Product> (empty list if none found)
     */
    public static List<Product> getAllFavoritesByUser(int userId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product_id FROM favorites WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int pid = rs.getInt("product_id");
                    Product p = ProductDAO.getProductById(pid);
                    if (p != null) {
                        list.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
