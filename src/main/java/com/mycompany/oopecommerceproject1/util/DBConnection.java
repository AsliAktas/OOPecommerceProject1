
package com.mycompany.oopecommerceproject1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/oop_ecommerce?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";        // MySQL kullanıcı adı
    private static final String PASSWORD = "root123"; // MySQL şifren

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }
}
