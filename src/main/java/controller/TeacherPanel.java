package controller;

import dao.DersDAO;
import dao.NotDAO;
import dao.OgrenciDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Ders;
import model.Not;
import model.Ogrenci;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherPanel {

    /**
     * Öğretmen panelini gösterir.
     * @param ogretmenId giriş yapan öğretmenin id'si
     */
    public static void show(int ogretmenId) {

        Stage st = new Stage();
        st.setTitle("Öğretmen Paneli");

        // == Ders seçimi ==
        ComboBox<Ders> dersBox = new ComboBox<>();
        dersBox.setPromptText("Ders Seçin");

        // Yalnızca öğretmenin kendi derslerini getir
        List<Ders> tumDersler = new DersDAO().dersListele();
        List<Ders> ogretmeninDersleri = tumDersler.stream()
                .filter(d -> d.getOgretmenid() == ogretmenId)
                .collect(Collectors.toList());
        dersBox.getItems().addAll(ogretmeninDersleri);

        // == Öğrenciler listesi ==
        ListView<Ogrenci> ogrList = new ListView<>();

        // == Not girişi ==
        TextField notField = new TextField();
        notField.setPromptText("0 - 100");

        Button kaydetBtn = new Button("Not Kaydet");
        Label   infoLbl  = new Label();

        // -- Ders seçildiğinde öğrencileri getir --
        dersBox.setOnAction(e -> {
            Ders seciliDers = dersBox.getValue();
            ogrList.getItems().clear();

            if (seciliDers != null) {
                /*  ▼▼▼  BU KISIM İÇİN YENİ METOT  ▼▼▼
                 *  OgrenciDAO'ya aşağıdaki imzayla bir metot eklemeniz
                 *  gerekiyor:  List<Ogrenci> ogrencileriDerseGore(int dersId)
                 *  (öğrenci_ders bağlantı tablosuyla JOIN yaparak.)
                 */
                List<Ogrenci> ogrenciler =
                        new OgrenciDAO().ogrencileriDerseGore(seciliDers.getId());

                ogrList.getItems().addAll(ogrenciler);
            }
        });

        // -- Not kaydet --
        kaydetBtn.setOnAction(e -> {
            Ders    d  = dersBox.getValue();
            Ogrenci o  = ogrList.getSelectionModel().getSelectedItem();

            if (d == null)             { infoLbl.setText("⚠️ Önce ders seçin!");      return; }
            if (o == null)             { infoLbl.setText("⚠️ Öğrenci seçin!");        return; }

            double puan;
            try {
                puan = Double.parseDouble(notField.getText());
                if (puan < 0 || puan > 100) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                infoLbl.setText("⚠️ 0-100 arası bir sayı girin!");
                return;
            }

            Not n = new Not(0, o.getId(), d.getId(), puan);
            new NotDAO().notEkle(n);

            infoLbl.setText("✅ Not kaydedildi.");
            notField.clear();
        });

        // == Layout ==
        VBox root = new VBox(10,
                new Label("Ders Seç:"), dersBox,
                new Label("Öğrenciler:"), ogrList,
                notField, kaydetBtn, infoLbl);
        root.setPadding(new Insets(20));

        st.setScene(new Scene(root, 360, 480));
        st.show();
    }
}
