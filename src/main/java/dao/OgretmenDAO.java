package dao;

import model.Ogretmen;

import java.sql.*;

/** Öğretmen tablo işlemleri */
public class OgretmenDAO {



    public void ogretmenEkle(Ogretmen o) {
        String sql = "INSERT INTO ogretmen(ad, soyad, kullanici_adi, sifre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, o.getAd());
            ps.setString(2, o.getSoyad());
            ps.setString(3, o.getKullaniciAdi());
            ps.setString(4, o.getSifre());
            ps.executeUpdate();

            System.out.println("✅ Öğretmen eklendi: " + o.getAd() + " " + o.getSoyad());
        } catch (SQLException e) {
            System.out.println("⛔ Öğretmen eklenemedi: " + e.getMessage());
        }
    }


    /** Doğruysa Ogretmen nesnesi, yanlışsa null döndürür */
    public Ogretmen girisYap(String kullaniciAdi, String sifre) {
        String sql = """
                     SELECT id, ad, soyad, kullanici_adi, sifre
                     FROM   ogretmen
                     WHERE  kullanici_adi = ? AND sifre = ?
                     """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, kullaniciAdi);
            ps.setString(2, sifre);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Ogretmen(
                        rs.getInt("id"),
                        rs.getString("ad"),
                        rs.getString("soyad"),
                        rs.getString("kullanici_adi"),
                        rs.getString("sifre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();   // prod’da log framework’ü tercih edin
        }
        return null;
    }
}
