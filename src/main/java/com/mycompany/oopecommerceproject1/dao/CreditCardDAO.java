package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.CreditCard;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Kredi kartı işlemlerini (ekleme, güncelleme, var mı kontrol etme, listeleme) yapan DAO sınıfı.
 */
public class CreditCardDAO {

    private final Connection conn;

    public CreditCardDAO() {
        Connection tempConn;
        try {
            tempConn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Veritabanı bağlantısı oluşturulamadı.", e);
        }
        conn = tempConn;
    }

    /**
     * Kullanıcının bütün kartlarını döner.
     */
    public List<CreditCard> getAllCardsByUserId(int userId) {
        String sql = "SELECT * FROM credit_cards WHERE user_id = ?";
        List<CreditCard> cards = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cards.add(new CreditCard(
                    rs.getInt("user_id"),
                    rs.getString("card_number"),
                    rs.getInt("expiry_month"),
                    rs.getInt("expiry_year"),
                    rs.getString("cvv")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * Yeni bir kredi kartı kaydeder.
     */
    public boolean addCard(CreditCard card) {
        String sql = "INSERT INTO credit_cards(user_id, card_number, expiry_month, expiry_year, cvv) " +
                     "VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, card.getUserId());
            pstmt.setString(2, card.getCardNumber());
            pstmt.setInt(3, card.getExpiryMonth());
            pstmt.setInt(4, card.getExpiryYear());
            pstmt.setString(5, card.getCvv());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mevcut kredi kartını günceller (user_id’ye göre; eğer birden fazla varsa en sonuncuyu günceller).
     */
    public boolean updateCard(CreditCard card) {
        // Burada kullanıcıya ait EN SON eklenen kartı güncellemek için önce en son id'yi çekelim:
        String findLastSql = "SELECT id FROM credit_cards WHERE user_id = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement findStmt = conn.prepareStatement(findLastSql)) {
            findStmt.setInt(1, card.getUserId());
            ResultSet rs = findStmt.executeQuery();
            if (rs.next()) {
                int lastCardId = rs.getInt("id");

                String updateSql = "UPDATE credit_cards SET card_number = ?, expiry_month = ?, expiry_year = ?, cvv = ? WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, card.getCardNumber());
                    updateStmt.setInt(2, card.getExpiryMonth());
                    updateStmt.setInt(3, card.getExpiryYear());
                    updateStmt.setString(4, card.getCvv());
                    updateStmt.setInt(5, lastCardId);
                    return updateStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
