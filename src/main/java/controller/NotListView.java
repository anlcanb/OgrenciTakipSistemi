package controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import dao.NotDAO;
import model.Not;
import java.util.List;

public class NotListView {

    /** Öğrenci ID'sine ait notları listeleyen pencere */
    public static void show(int ogrenciId) {

        // 1) JavaFX sahne & temel VBox
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        // 2) Başlık
        Label title = new Label("Notlarınız:");

        // 3) Ortalama label – önce oluştur, sonra değeri set et
        Label ortalamaLabel = new Label();      // boş yarat
        NotDAO dao = new NotDAO();              // DAO
        double ortalama = dao.ortalamaHesapla(ogrenciId);
        ortalamaLabel.setText("Ortalama: " + String.format("%.2f", ortalama));

        // 4) Not listesi
        ListView<String> listView = new ListView<>();
        List<Not> notlar = dao.getOgrenciNotlari(ogrenciId);
        for (Not n : notlar) {
            listView.getItems().add(
                    "Ders ID: " + n.getDersId() + "  -  Not: " + n.getNot()
            );
        }

        // 5) VBox’a SIRAYLA ekle
        vbox.getChildren().addAll(title, listView, ortalamaLabel);

        // 6) Sahneyi göster
        Scene scene = new Scene(vbox, 350, 300);
        stage.setScene(scene);
        stage.setTitle("Notlarım");
        stage.show();


        Label durum = new Label(ortalama >= 60 ? "GEÇTİ ✅" : "KALDI ❌");
        vbox.getChildren().add(durum);

    }
}
