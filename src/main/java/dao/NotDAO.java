package dao;

import model.Not;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Not (puan) işlemleri için DAO katmanı
 */
public class NotDAO {

    /* -------------------------------------------------- */
    /* 1. UPSERT  (varsa güncelle, yoksa ekle)            */
    /* -------------------------------------------------- */
    public void notEkleOrGuncelle(int ogrenciId, int dersId, double puan) {
        if (!gecerliPuan(puan)) return;

        String sqlUpdate = "UPDATE notlar SET not_degeri = ? WHERE ogrenci_id = ? AND ders_id = ?";
        String sqlInsert = "INSERT INTO notlar (ogrenci_id, ders_id, not_degeri) VALUES (?,?,?)";

        try (Connection c = DBConnection.getConnection()) {
            // önce güncellemeyi dene
            try (PreparedStatement ps = c.prepareStatement(sqlUpdate)) {
                ps.setDouble(1, puan);
                ps.setInt   (2, ogrenciId);
                ps.setInt   (3, dersId);
                if (ps.executeUpdate() > 0) {
                    System.out.println("✅ Not güncellendi (UPSERT).");
                    return;
                }
            }
            // kayıt yoksa ekle
            try (PreparedStatement ps2 = c.prepareStatement(sqlInsert)) {
                ps2.setInt   (1, ogrenciId);
                ps2.setInt   (2, dersId);
                ps2.setDouble(3, puan);
                ps2.executeUpdate();
                System.out.println("✅ Not eklendi (UPSERT).");
            }
        } catch (SQLException e) {
            System.out.println("⛔ UPSERT hata: " + e.getMessage());
        }
    }

    /* -------------------------------------------------- */
    /* 2. SADECE EKLE                                     */
    /* -------------------------------------------------- */
    public void notEkle(Not n) {
        if (!gecerliPuan(n.getNot())) return;
        String sql = "INSERT INTO notlar (ogrenci_id, ders_id, not_degeri) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, n.getOgrenciId());
            ps.setInt   (2, n.getDersId());
            ps.setDouble(3, n.getNot());
            ps.executeUpdate();
            System.out.println("✅ Not eklendi.");
        } catch (SQLException e) {
            System.out.println("⛔ Not ekleme hatası: " + e.getMessage());
        }
    }

    /* -------------------------------------------------- */
    /* 3. YALNIZCA GÜNCELLE                               */
    /* -------------------------------------------------- */
    public void notGuncelle(Not n) {
        if (!gecerliPuan(n.getNot())) return;
        String sql = "UPDATE notlar SET not_degeri = ? WHERE ogrenci_id = ? AND ders_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, n.getNot());
            ps.setInt   (2, n.getOgrenciId());
            ps.setInt   (3, n.getDersId());
            int aff = ps.executeUpdate();
            if (aff > 0) System.out.println("✅ Not güncellendi.");
            else         System.out.println("⚠️ Güncellenecek kayıt bulunamadı.");
        } catch (SQLException e) {
            System.out.println("⛔ Not güncelleme hatası: " + e.getMessage());
        }
    }

    /* -------------------------------------------------- */
    /* 4. TEKİL NOT GETİR                                 */
    /* -------------------------------------------------- */
    public Double getNotByOgrenciVeDers(int ogrenciId, int dersId) {
        String sql = "SELECT not_degeri FROM notlar WHERE ogrenci_id = ? AND ders_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ogrenciId);
            ps.setInt(2, dersId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("not_degeri");
        } catch (SQLException e) {
            System.out.println("⛔ Not çekme hatası: " + e.getMessage());
        }
        return null;
    }

    /* -------------------------------------------------- */
    /* 5. ÖĞRENCİ ORTALAMASI                              */
    /* -------------------------------------------------- */
    public double ogrenciOrtalamasi(int ogrenciId) {
        String sql = "SELECT AVG(not_degeri) AS ort FROM notlar WHERE ogrenci_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ogrenciId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("ort");
        } catch (SQLException e) {
            System.out.println("⛔ Ortalama hesap hatası: " + e.getMessage());
        }
        return 0.0;
    }

    /* -------------------------------------------------- */
    /* 6. DERS ORTALAMASI (🔥 Yeni)                       */
    /* -------------------------------------------------- */
    public double dersOrtalamasi(int dersId) {
        String sql = "SELECT AVG(not_degeri) AS ort FROM notlar WHERE ders_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dersId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("ort");
        } catch (SQLException e) {
            System.out.println("⛔ Ders ortalama hatası: " + e.getMessage());
        }
        return 0.0;
    }

    /* -------------------------------------------------- */
    /* 7. TOPLU LISTELEME METOTLARI                       */
    /* -------------------------------------------------- */
    public List<Not> getOgrenciNotlari(int ogrenciId) {
        List<Not> list = new ArrayList<>();
        String sql = "SELECT * FROM notlar WHERE ogrenci_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ogrenciId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("⛔ Öğrenci notları hata: " + e.getMessage());
        }
        return list;
    }


    /* -------------------------------------------------- */
    /* ESKİ ADIYLA GERİ-UYUMLU METOT                      */
    /* -------------------------------------------------- */
    public double ortalamaHesapla(int ogrenciId) {
        return ogrenciOrtalamasi(ogrenciId);
    }


    public List<Not> notlariListele() {
        List<Not> list = new ArrayList<>();
        String sql = "SELECT * FROM notlar";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("⛔ Not listesi hata: " + e.getMessage());
        }
        return list;
    }

    /* -------------------------------------------------- */
    /* Yardımcılar                                        */
    /* -------------------------------------------------- */
    private boolean gecerliPuan(double p) {
        if (p < 0 || p > 100) {
            System.out.println("⚠️ Hatalı not: 0‑100 arası olmalı!");
            return false;
        }
        return true;
    }

    private Not map(ResultSet rs) throws SQLException {
        return new Not(
                rs.getInt("id"),
                rs.getInt("ogrenci_id"),
                rs.getInt("ders_id"),
                rs.getDouble("not_degeri"));
    }
}
