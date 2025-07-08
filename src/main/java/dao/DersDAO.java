package dao;



import model.Ders;
import java.sql.SQLException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class DersDAO {
    public void dersEkle(Ders d){
        String sql = "INSERT INTO ders (ad,ogretmen_id) VALUES(?,?)";
        try(Connection c = DBConnection.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, d.getAd());
            ps.setInt(2, d.getOgretmenid());
            ps.executeUpdate();
            System.out.println("Ders eklendi."+ d.getAd());}
        catch (SQLException e){
            System.out.println("Ders ekleme hatası"+ e.getMessage());


        }
    }
public List<Ders> dersListele(){
        List<Ders> list = new ArrayList<>();
        String sql = "SELECT * FROM ders";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)){

            while (rs.next()){

                list.add(new Ders(rs.getInt("id"),rs.getString("ad"),rs.getInt("ogretmen_id")));
            }
        }catch(SQLException e){
            System.out.println("Ders güncelleme hatası :"+ e.getMessage());
        }

    return list;
}

    // DersDAO.java
    public String getDersAdiById(int id) {
        String sql = "SELECT ad FROM ders WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("ad");
        } catch (SQLException e) {
            System.out.println("⛔ Ders adı çekme hatası: " + e.getMessage());
        }
        return "Bilinmeyen Ders";
    }






    public void dersSil(int dersId) {
        String sql = "DELETE FROM ders WHERE ders_id = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, dersId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Ders silme hatası: " + e.getMessage());
        }
    }

}
