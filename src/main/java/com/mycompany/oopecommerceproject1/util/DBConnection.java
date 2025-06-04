package com.mycompany.oopecommerceproject1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for database connection:
 * - Uses a single Connection object (singleton pattern)
 * - getConnection() opens a new connection if none exists or if closed
 * - MySQL JDBC URL includes useSSL=false, allowPublicKeyRetrieval=true, serverTimezone=UTC
 */
public class DBConnection {
    // Added allowPublicKeyRetrieval=true to the JDBC URL:
    private static final String URL =
        "jdbc:mysql://localhost:3306/oop_ecommerce"
        + "?useSSL=false"
        + "&allowPublicKeyRetrieval=true"
        + "&serverTimezone=UTC";

    // MySQL root user and password:
    private static final String USER = "root";
    private static final String PASSWORD = "1234";  // Updated to "1234"

    private static Connection conn;

    /**
     * Returns a Connection object.
     * - If conn is null or closed, opens a new connection
     * - Otherwise returns the existing connection
     * @return Connection instance
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }
}
