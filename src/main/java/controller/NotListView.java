// NotListView.java
package controller;

import dao.DersDAO;
import dao.NotDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Not;

import java.util.List;

public class NotListView {

    /** Öğrenci ID’sine ait notları listeleyen pencere */
    public static void show(int ogrenciId) {

        // JavaFX temel sahne
        Stage stage = new Stage();
        VBox  root  = new VBox(10);
        root.setPadding(new Insets(20));

        Label baslik = new Label("Notlarınız:");

        /* ---------- NOT LİSTESİ ---------- */
        ListView<String> listView = new ListView<>();
        NotDAO  notDao  = new NotDAO();
        DersDAO dersDao = new DersDAO();

        List<Not> notlar = notDao.getOgrenciNotlari(ogrenciId);

        for (Not n : notlar) {
            String dersAdi = dersDao.getDersAdiById(n.getDersId());
            listView.getItems().add(
                    dersAdi + "  –  Not: " + n.getNot()
            );
        }

        /* ---------- ORTALAMALAR ---------- */
        double ogrOrt   = notDao.ogrenciOrtalamasi(ogrenciId);
        Label  ogrLbl   = new Label("Öğrenci Ortalaması: "
                + String.format("%.2f", ogrOrt));

        // Eğer listede tek ders varsa onun ID’sini alabiliriz;
        // çok ders varsa kullanıcı satır seçtiğinde de güncelleyebiliriz.
        double dersOrt = 0.0;
        if (!notlar.isEmpty()) {
            // ilk satırın ders ortalamasını göster (basit çözüm)
            dersOrt = notDao.dersOrtalamasi(notlar.get(0).getDersId());
        }
        Label  dersLbl  = new Label("Ders Ortalaması: "
                + String.format("%.2f", dersOrt));

        // Geçti / Kaldı etiketi
        Label durumLbl  = new Label(ogrOrt >= 60 ? "GEÇTİ ✅" : "KALDI ❌");

        /* ---------- DÜZEN ---------- */
        root.getChildren().addAll(baslik, listView, ogrLbl, dersLbl, durumLbl);

        stage.setScene(new Scene(root, 380, 320));
        stage.setTitle("Notlarım");
        stage.show();
    }
}
