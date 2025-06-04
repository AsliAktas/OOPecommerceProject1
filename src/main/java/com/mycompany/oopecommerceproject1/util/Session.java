package com.mycompany.oopecommerceproject1.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Session utility class:
 * - Stores the username and ID of the logged-in user
 * - When setCurrentUsername() is called, also fetches the user’s ID from the database
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

    /**
     * Sets the current username and retrieves the userId from the DB.
     * @param username The user’s username
     */
    public static void setCurrentUsername(String username) {
        currentUsername = username;
        // Fetch currentUserId from the database
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
