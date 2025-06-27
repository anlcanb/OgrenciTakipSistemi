package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:ogrenci_takip.db";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("✅ Veritabanı bağlantısı başarılı.");
        } catch (SQLException e) {
            System.out.println("⚠️ Bağlantı hatası: " + e.getMessage());
        }
        return conn;
    }
}
