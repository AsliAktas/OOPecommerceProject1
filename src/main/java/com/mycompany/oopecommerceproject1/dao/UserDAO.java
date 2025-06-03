package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.User;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Kullanıcı tablosu ile ilgili CRUD işlemlerini yapar.
 */
public class UserDAO {

    private final Connection conn;

    public UserDAO() {
        // DBConnection.getConnection() SQLException fırlatıyorsa burada yakalayalım:
        Connection tempConn;
        try {
            tempConn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Veritabanı bağlantısı oluşturulamadı.", e);
        }
        conn = tempConn;
    }

    /**
     * Giriş işlemi için: username ve password eşleşiyorsa User objesi döner, yoksa null.
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
     * Kullanıcının e-posta adresini günceller.
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
     * Kullanıcının mevcut şifresini kontrol edip, yeni şifre ile değiştirir.
     * @return true: eski şifre doğru ve güncelleme başarılı. false: değilse.
     */
    public boolean updatePassword(int userId, String oldPassword, String newPassword) {
        // 1. Adım: Mevcut eski şifre doğru mu?
        String checkSql = "SELECT password FROM users WHERE id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                String currentPassInDb = rs.getString("password");
                if (!currentPassInDb.equals(oldPassword)) {
                    return false; // Eski şifre yanlış
                }
            } else {
                return false; // Kullanıcı bulunamadı
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // 2. Adım: Yeni şifre ile update işlemi
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
