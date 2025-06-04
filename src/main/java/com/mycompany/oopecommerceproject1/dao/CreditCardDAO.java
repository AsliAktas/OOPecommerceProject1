package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.CreditCard;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for credit card operations:
 * - List all cards for a user
 * - Add a new card
 * - Update an existing card (updates the most recently added one)
 */
public class CreditCardDAO {

    private final Connection conn;

    public CreditCardDAO() {
        Connection tempConn;
        try {
            tempConn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection.", e);
        }
        conn = tempConn;
    }

    /**
     * Returns all credit cards for a given user.
     * @param userId The user’s ID
     * @return List of CreditCard objects (empty list if none)
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
     * Inserts a new credit card record.
     * @param card CreditCard object (userId, cardNumber, expiryMonth, expiryYear, cvv must be set)
     * @return true if insertion succeeds, false otherwise
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
     * Updates the most recently added credit card for the user.
     * - Finds the highest card ID for this user
     * - Updates that row with new card details
     * @param card Updated CreditCard object (userId and new details must be set)
     * @return true if update succeeds, false otherwise
     */
    public boolean updateCard(CreditCard card) {
        // 1) Find the ID of the most recently added card for this user
        String findLastSql = "SELECT id FROM credit_cards WHERE user_id = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement findStmt = conn.prepareStatement(findLastSql)) {
            findStmt.setInt(1, card.getUserId());
            ResultSet rs = findStmt.executeQuery();
            if (rs.next()) {
                int lastCardId = rs.getInt("id");

                // 2) Update that card by ID
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
