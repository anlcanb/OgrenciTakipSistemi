package dao;

import model.Not;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotDAO {

    public void notEkle(Not n) {
        String sql = "INSERT INTO notlar (ogrenci_id, ders_id, not) VALUES (?, ?, ?)";

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
                        rs.getDouble("not")
                );
                notlar.add(n);
            }

        } catch (SQLException e) {
            System.out.println(" Notlar listelenirken hata: " + e.getMessage());
        }

        return notlar;
    }


    public void notGuncelle(Not n) {
        String sql = "UPDATE notlar SET not = ? WHERE ogrenci_id = ? AND ders_id = ?";

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

}




