package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.Favorite;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Kullanıcının favori ürünlerini eklemek/çıkarmak ve sorgulamak için DAO.
 * Veritabanında “favorites” tablosu olması gerekir.
 */
public class FavoriteDAO {

    /**
     * Verilen kullanıcı ve ürün için kaydın varlığını kontrol eder.
     * @return true ise zaten favoride, false ise favoride değil.
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
     * Yeni favori kaydı ekler.
     * @return true ise ekleme başarılı, false ise başarısız.
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
            // UNIQUE kısıtı ihlali (zaten varsa) veya başka bir hata
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Favori kaydını siler.
     * @return true ise silme başarılı, false ise başarısız.
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
     * Verilen kullanıcıya ait tüm favori ürünleri döner.
     * @return Liste boşsa[] kullanıcının favorisi yok demektir.
     */
    public static List<Favorite> getAllFavoritesByUser(int userId) {
        List<Favorite> list = new ArrayList<>();
        String sql = "SELECT id, user_id, product_id FROM favorites WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Favorite(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
