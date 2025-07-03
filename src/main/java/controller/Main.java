package controller;

import dao.*;
import javafx.application.Application;
import model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1) Tabloları oluştur
        createTablesIfNotExists();

        // 2) Test amaçlı örnek kayıt (istersen yoruma al)
        OgrenciDAO ogrenciDAO = new OgrenciDAO();
        ogrenciDAO.ogrenciEkle(new Ogrenci(0, "Ali", "Yılmaz", "aliyilmaz", "1234"));

        OgretmenDAO ogretmenDAO = new OgretmenDAO();
        ogretmenDAO.ogretmenEkle(new Ogretmen(0, "Ali Hasan", "Mertoğlu", "ahm", "hahaha"));


        DersDAO dersDAO = new DersDAO();
        dersDAO.dersEkle(new Ders(0, "NDP", 1));

        // 3) JavaFX’i başlat  ⬅️  ***BURADA***
        Application.launch(GirisSecimView.class, args);

    }

    /* ---------- tablo oluşturan metod ---------- */
    private static void createTablesIfNotExists() {
        String ogrenciTable = """
                CREATE TABLE IF NOT EXISTS ogrenci (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad TEXT,
                    soyad TEXT,
                    kullanici_adi TEXT UNIQUE,
                    sifre TEXT)""";

        String ogretmenTable = """
                CREATE TABLE IF NOT EXISTS ogretmen (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad TEXT,
                    soyad TEXT,
                    kullanici_adi TEXT UNIQUE,
                    sifre TEXT)""";

        String dersTable = """
                CREATE TABLE IF NOT EXISTS ders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ad TEXT,
                    ogretmen_id INTEGER,
                    FOREIGN KEY (ogretmen_id) REFERENCES ogretmen(id))""";

        String notlarTable = """
                CREATE TABLE IF NOT EXISTS notlar (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ogrenci_id INTEGER NOT NULL,
                    ders_id INTEGER NOT NULL,
                    not_degeri REAL,
                    FOREIGN KEY (ogrenci_id) REFERENCES ogrenci(id),
                    FOREIGN KEY (ders_id) REFERENCES ders(id))""";

        String ogrenci_dersTable = """
        CREATE TABLE IF NOT EXISTS ogrenci_ders (
            ogrenci_id INTEGER,
            ders_id    INTEGER,
            PRIMARY KEY (ogrenci_id, ders_id),
            FOREIGN KEY (ogrenci_id) REFERENCES ogrenci(id),
            FOREIGN KEY (ders_id)    REFERENCES ders(id)
        )""";


        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement()) {


            System.out.println("DB yolu: "+
                    new java.io.File("ogrenci_takip.db").getAbsolutePath());

            s.execute(ogrenciTable);
            s.execute(ogretmenTable);
            s.execute(dersTable);
            s.execute(ogrenci_dersTable);
            s.execute(notlarTable);

            System.out.println("✅ Tablolar hazır.");
        } catch (SQLException e) {
            System.out.println("⛔ SQL Hatası: " + e.getMessage());
        }
    }
}
