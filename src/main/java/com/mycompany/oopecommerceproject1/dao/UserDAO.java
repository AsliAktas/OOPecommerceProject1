package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.User;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for operations on the users table:
 * - findByUsernameAndPassword: for login matching
 * - updateEmail: updates the user’s email address
 * - updatePassword: checks the current password and updates to a new password
 */
public class UserDAO {

    private final Connection conn;

    public UserDAO() {
        // If DBConnection.getConnection() throws SQLException, wrap in RuntimeException
        Connection tempConn;
        try {
            tempConn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection.", e);
        }
        conn = tempConn;
    }

    /**
     * For login: returns a User object if username/password match, otherwise null.
     * @param username The username
     * @param password The password
     * @return A User object or null
     */
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT id, username, email, password FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the user’s email address.
     * @param userId   The user’s ID
     * @param newEmail The new email address
     * @return true if update succeeds, false otherwise
     */
    public boolean updateEmail(int userId, String newEmail) {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks the current password and, if correct, updates it to the new password.
     * @param userId      The user’s ID
     * @param oldPassword The current (old) password
     * @param newPassword The new password
     * @return true if old password matches and update succeeds; false otherwise
     */
    public boolean updatePassword(int userId, String oldPassword, String newPassword) {
        // Step 1: Verify that the existing (old) password is correct
        String checkSql = "SELECT password FROM users WHERE id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                String currentPassInDb = rs.getString("password");
                if (!currentPassInDb.equals(oldPassword)) {
                    return false; // Old password is incorrect
                }
            } else {
                return false; // User not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Step 2: Perform the update with the new password
        String updateSql = "UPDATE users SET password = ? WHERE id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setString(1, newPassword);
            updateStmt.setInt(2, userId);
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
