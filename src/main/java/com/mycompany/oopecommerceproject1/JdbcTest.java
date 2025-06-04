package com.mycompany.oopecommerceproject1;

import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple console application to test the database connection.
 * - Calls DBConnection.getConnection()
 * - Reads id, username, password from the users table and prints to console
 */
public class JdbcTest {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("DB connection successful. Now reading the users table:");
            ResultSet rs = stmt.executeQuery("SELECT id, username, password FROM users");

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                System.out.println(
                    "id=" + rs.getInt("id") +
                    ", username=" + rs.getString("username") +
                    ", password=" + rs.getString("password")
                );
            }
            System.out.println("Total records: " + rowCount);
        } catch (SQLException e) {
            System.err.println("Error occurred during JdbcTest:");
            e.printStackTrace();
        }
    }
}
