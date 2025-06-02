
package com.mycompany.oopecommerceproject1;

import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTest {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("DB bağlantısı başarılı. Şimdi users tablosunu okuyorum:");
            ResultSet rs = stmt.executeQuery("SELECT id, username, password FROM users");

            int satirSayisi = 0;
            while (rs.next()) {
                satirSayisi++;
                System.out.println(
                    "id=" + rs.getInt("id") +
                    ", username=" + rs.getString("username") +
                    ", password=" + rs.getString("password")
                );
            }
            System.out.println("Toplam kayıt: " + satirSayisi);
        } catch (SQLException e) {
            System.err.println("JdbcTest sırasında hata oluştu:");
            e.printStackTrace();
        }
    }
}
