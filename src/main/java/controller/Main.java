package controller;

import dao.DBConnection;
import dao.DersDAO;
import dao.OgrenciDAO;
import model.Ders;
import model.Ogrenci;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Veritabanında tablo yoksa oluşturalım
        createTablesIfNotExists();

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

        DersDAO dersDAO = new DersDAO();
        Ders d1 = new Ders(0, "Nesneye Dayalı Programlama", 1);
        dersDAO.dersEkle(d1);

        System.out.println("\n Ders Listesi:");
        dersDAO.dersListele()
                .forEach(ders -> System.out.println(ders.getId()+"-"+ ders.getAd()));



    }

    public static void createTablesIfNotExists() {
        String ogrenciTable = "CREATE TABLE IF NOT EXISTS ogrenci (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ad TEXT," +
                "soyad TEXT," +
                "kullanici_adi TEXT UNIQUE," +
                "sifre TEXT)";

        String ogretmenTable = "CREATE TABLE IF NOT EXISTS ogretmen (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ad TEXT," +
                "soyad TEXT," +
                "kullanici_adi TEXT UNIQUE," +
                "sifre TEXT)";

        String dersTable = "CREATE TABLE IF NOT EXISTS ders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ad TEXT," +
                "ogretmen_id INTEGER," +
                "FOREIGN KEY (ogretmen_id) REFERENCES ogretmen(id))";

        String notlarTable = "CREATE TABLE IF NOT EXISTS notlar (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ogrenci_id INTEGER NOT NULL, " +
                "ders_id INTEGER NOT NULL, " +
                "not REAL, " +
                "FOREIGN KEY (ogrenci_id) REFERENCES ogrenci(id), " +
                "FOREIGN KEY (ders_id) REFERENCES ders(id)" +
                ");";



        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(ogrenciTable);
            stmt.execute(ogretmenTable);
            stmt.execute(dersTable);
            stmt.execute(notlarTable);

            System.out.println("✅ Tablolar oluşturuldu veya zaten vardı.");
        } catch (SQLException e) {
            System.out.println("⛔ Tablo oluşturma hatası: " + e.getMessage());
        }
    }}
