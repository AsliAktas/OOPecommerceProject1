package com.mycompany.oopecommerceproject1.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Session: 
 * - Oturum açan kullanıcının kullanıcı adı ve ID'sini saklar.
 * - setCurrentUsername() çağrıldığında, veritabanından ID'yi getirir.
 */
public class Session {
    private static String currentUsername;
    private static int currentUserId;

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
        // Aynı anda currentUserId'yi DB'den alıyoruz:
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    currentUserId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
