package controller;

import dao.DBConnection;
import dao.OgrenciDAO;
import model.Ogrenci;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Veritabanında tablo yoksa oluşturalım
        createTableIfNotExists();

        OgrenciDAO ogrenciDAO = new OgrenciDAO();

        // 1. Yeni öğrenci ekleyelim
        Ogrenci ogr1 = new Ogrenci(0, "Ali", "Yılmaz", "aliyilmaz", "1234");
        ogrenciDAO.ogrenciEkle(ogr1);

        // 2. Tüm öğrencileri listeleyelim
        List<Ogrenci> ogrenciler = ogrenciDAO.ogrenciListele();
        System.out.println("📋 Tüm Öğrenciler:");
        for (Ogrenci ogr : ogrenciler) {
            System.out.println(ogr.getId() + " - " + ogr.getAd() + " " + ogr.getSoyad());
        }
    }

    private static void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS ogrenci (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad TEXT NOT NULL,
                    soyad TEXT NOT NULL,
                    kullanici_adi TEXT NOT NULL,
                    sifre TEXT NOT NULL
                );
                """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("📌 Tablo kontrolü tamamlandı (varsa geçildi, yoksa oluşturuldu).");
        } catch (SQLException e) {
            System.out.println("⚠️ Tablo oluşturulurken hata: " + e.getMessage());
        }
    }
}
