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


    public Ogrenci girisYap(String kullaniciAdi, String sifre) {
        String sql = "SELECT * FROM ogrenci WHERE kullanici_adi = ? AND sifre = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kullaniciAdi);
            ps.setString(2, sifre);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Ogrenci(
                        rs.getInt("id"),
                        rs.getString("ad"),
                        rs.getString("soyad"),
                        rs.getString("kullanici_adi"),
                        rs.getString("sifre")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Giriş hatası: " + e.getMessage());
        }
        return null;
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



    // OgrenciDAO.java içine ekleyin
    public List<Ogrenci> ogrencileriDerseGore(int dersId) {
        List<Ogrenci> list = new ArrayList<>();
        String sql = """
        SELECT o.* FROM ogrenci o
        JOIN ogrenci_ders od ON o.id = od.ogrenci_id
        WHERE od.ders_id = ?
    """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, dersId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Ogrenci(
                        rs.getInt("id"),
                        rs.getString("ad"),
                        rs.getString("soyad"),
                        rs.getString("kullanici_adi"),
                        rs.getString("sifre")));
            }
        } catch (SQLException e) {
            System.out.println("Öğrenci çekme hatası: " + e.getMessage());
        }
        return list;
    }

}
