package com.mycompany.oopecommerceproject1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // JDBC URL’e allowPublicKeyRetrieval=true ekledik:
    private static final String URL =
        "jdbc:mysql://localhost:3306/oop_ecommerce"
        + "?useSSL=false"
        + "&allowPublicKeyRetrieval=true"
        + "&serverTimezone=UTC";

    // MySQL root kullanıcısı ve parolası:
    private static final String USER = "root";
    private static final String PASSWORD = "1234";  // Burayı "1234" olarak güncelledik

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }
}
