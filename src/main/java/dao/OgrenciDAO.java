package dao;

import model.Ogrenci;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OgrenciDAO {

    public void ogrenciEkle(Ogrenci ogrenci) {
        String sql = "INSERT INTO ogrenci(ad, soyad, kullanici_adi, sifre) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ogrenci.getAd());
            pstmt.setString(2, ogrenci.getSoyad());
            pstmt.setString(3, ogrenci.getKullaniciAdi());
            pstmt.setString(4, ogrenci.getSifre());

            pstmt.executeUpdate();
            System.out.println("✅ Öğrenci başarıyla eklendi.");

        } catch (SQLException e) {
            System.out.println("⚠️ Öğrenci eklenirken hata oluştu: " + e.getMessage());
        }
    }

    public List<Ogrenci> ogrenciListele() {
        List<Ogrenci> ogrenciler = new ArrayList<>();
        String sql = "SELECT * FROM ogrenci";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ogrenci ogrenci = new Ogrenci(
                        rs.getInt("id"),
                        rs.getString("ad"),
                        rs.getString("soyad"),
                        rs.getString("kullanici_adi"),
                        rs.getString("sifre")
                );
                ogrenciler.add(ogrenci);
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Öğrenciler listelenirken hata oluştu: " + e.getMessage());
        }

        return ogrenciler;
    }
}
