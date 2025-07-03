package dao;

import model.Not;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotDAO {

    public void notEkle(Not n) {



        if (n.getNot() < 0 || n.getNot() > 100) {
            System.out.println("⚠️ Hatalı not: 0 ile 100 arasında bir değer girilmelidir!");
            return;
        }

        String sql = "INSERT INTO notlar (ogrenci_id, ders_id, not_degeri) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, n.getOgrenciId());
            ps.setInt(2, n.getDersId());
            ps.setDouble(3, n.getNot());

            ps.executeUpdate();
            System.out.println(" Not eklendi.");

        } catch (SQLException e) {
            System.out.println(" Not eklenirken hata: " + e.getMessage());
        }
    }




    public List<Not> getOgrenciNotlari(int ogrenciId) {
        List<Not> notlar = new ArrayList<>();
        String sql = "SELECT * FROM notlar WHERE ogrenci_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ogrenciId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Not n = new Not(
                        rs.getInt("id"),
                        rs.getInt("ogrenci_id"),
                        rs.getInt("ders_id"),
                        rs.getDouble("not_degeri")
                );
                notlar.add(n);
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Öğrenci notları listelenirken hata: " + e.getMessage());
        }

        return notlar;
    }


    public List<Not> notlariListele() {
        List<Not> notlar = new ArrayList<>();
        String sql = "SELECT * FROM notlar";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Not n = new Not(
                        rs.getInt("id"),
                        rs.getInt("ogrenci_id"),
                        rs.getInt("ders_id"),
                        rs.getDouble("not_degeri")
                );
                notlar.add(n);
            }

        } catch (SQLException e) {
            System.out.println(" Notlar listelenirken hata: " + e.getMessage());
        }

        return notlar;
    }


    public void notGuncelle(Not n) {

        if (n.getNot() < 0 || n.getNot() > 100) {
            System.out.println("⚠️ Hatalı not: 0 ile 100 arasında bir değer girilmelidir!");
            return;
        }

        String sql = "UPDATE notlar SET not_degeri = ? WHERE ogrenci_id = ? AND ders_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, n.getNot());
            ps.setInt(2, n.getOgrenciId());
            ps.setInt(3, n.getDersId());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                System.out.println("✅ Not başarıyla güncellendi.");
            } else {
                System.out.println("⚠️ Güncellenecek not bulunamadı.");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Not güncelleme hatası: " + e.getMessage());
        }
    }


    public double ortalamaHesapla(int ogrenciId) {
        String sql = "SELECT AVG(not_degeri) as ortalama FROM notlar WHERE ogrenci_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ogrenciId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("ortalama");
            }

        } catch (SQLException e) {
            System.out.println("Ortalama hesaplama hatası: " + e.getMessage());
        }
        return 0.0;
    }


}




