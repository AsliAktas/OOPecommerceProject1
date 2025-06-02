package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.CreditCard;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditCardDAO {

    /**
     * userId'ye ait tek bir kredi kartını döner. Kart yoksa null döner.
     */
    public static CreditCard getCardByUserId(int userId) {
        String sql = "SELECT * FROM credit_cards WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CreditCard(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("card_number"),
                        rs.getInt("expiry_month"),
                        rs.getInt("expiry_year"),
                        rs.getString("cvv")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * userId'ye ait tüm kredi kartlarını liste olarak döner. Yoksa boş liste döner.
     */
    public static List<CreditCard> getAllCardsByUserId(int userId) {
        List<CreditCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM credit_cards WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CreditCard card = new CreditCard(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("card_number"),
                        rs.getInt("expiry_month"),
                        rs.getInt("expiry_year"),
                        rs.getString("cvv")
                    );
                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * Yeni kredi kartı ekler. Eğer userId'ye ait bir kart zaten varsa false döner.
     */
    public static boolean insertCard(CreditCard card) {
        // Eğer zaten bir kart varsa ekleme yapma
        if (getCardByUserId(card.getUserId()) != null) {
            return false;
        }

        String insertSql = "INSERT INTO credit_cards (user_id, card_number, expiry_month, expiry_year, cvv) " +
                           "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {

            stmt.setInt(1, card.getUserId());
            stmt.setString(2, card.getCardNumber());
            stmt.setInt(3, card.getExpiryMonth());
            stmt.setInt(4, card.getExpiryYear());
            stmt.setString(5, card.getCvv());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * userId'ye ait mevcut kartı günceller. Yoksa false döner.
     */
    public static boolean updateCard(CreditCard card) {
        // Eğer kart yoksa güncelleme yapma
        if (getCardByUserId(card.getUserId()) == null) {
            return false;
        }

        String updateSql = "UPDATE credit_cards SET card_number = ?, expiry_month = ?, expiry_year = ?, cvv = ? " +
                           "WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, card.getCardNumber());
            stmt.setInt(2, card.getExpiryMonth());
            stmt.setInt(3, card.getExpiryYear());
            stmt.setString(4, card.getCvv());
            stmt.setInt(5, card.getUserId());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
