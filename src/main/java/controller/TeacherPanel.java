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
import model.Ogrenci;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherPanel {

    /** Öğretmen panelini gösterir */
    public static void show(int ogretmenId) {

        Stage stage = new Stage();
        stage.setTitle("Öğretmen Paneli");

        /* ---------- 1. DERS SEÇİMİ ---------- */
        ComboBox<Ders> dersBox = new ComboBox<>();
        dersBox.setPromptText("Ders Seçin");

        List<Ders> dersler = new DersDAO().dersListele().stream()
                .filter(d -> d.getOgretmenid() == ogretmenId)
                .collect(Collectors.toList());
        dersBox.getItems().addAll(dersler);

        /* ---------- 2. ÖĞRENCİ LİSTESİ ---------- */
        ListView<Ogrenci> ogrList = new ListView<>();

        /* ---------- 3. NOT ALANI + BUTONLAR ---------- */
        TextField notField = new TextField();
        notField.setPromptText("0-100");

        Button kaydetBtn   = new Button("Not Kaydet");
        Button guncelleBtn = new Button("Not Güncelle");

        Label  ortLabel   = new Label(" Ders Ortalaması: -");
        Label  bilgiLabel = new Label();

        /* Ders seçildiğinde öğrencileri ve ortalamayı yükle */
        dersBox.setOnAction(e -> {
            ogrList.getItems().clear();
            notField.clear();

            Ders d = dersBox.getValue();
            if (d == null) { ortLabel.setText(" Ders Ortalaması: -"); return; }

            ogrList.getItems().addAll(
                    new OgrenciDAO().ogrencileriDerseGore(d.getId()));

            double ort = new NotDAO().dersOrtalamasi(d.getId());
            ortLabel.setText(" Ders Ortalaması: " + String.format("%.2f", ort));
        });

        /* Öğrenci seçilince not alanını doldur */
        ogrList.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            Ders d = dersBox.getValue();
            if (d == null || n == null) { notField.clear(); return; }

            Double puan = new NotDAO().getNotByOgrenciVeDers(n.getId(), d.getId());
            notField.setText(puan != null ? String.valueOf(puan) : "");
        });

        /* Yardımcı doğrulama */
        Runnable check = () -> {
            if (dersBox.getValue() == null || ogrList.getSelectionModel().getSelectedItem() == null)
                throw new IllegalStateException("Ders ve öğrenci seçilmelidir!");
        };

        /* Ortalamayı yenileyen yardımcı */
        Runnable ortYenile = () -> {
            double ort = new NotDAO().dersOrtalamasi(dersBox.getValue().getId());
            ortLabel.setText(" Ders Ortalaması: " + String.format("%.2f", ort));
        };

        /* NOT KAYDET (UPSERT) */
        kaydetBtn.setOnAction(e -> {
            try {
                check.run();
                double puan = Double.parseDouble(notField.getText());
                new NotDAO().notEkleOrGuncelle(
                        ogrList.getSelectionModel().getSelectedItem().getId(),
                        dersBox.getValue().getId(),
                        puan);
                bilgiLabel.setText(" Not kaydedildi.");
                ortYenile.run();
            } catch (NumberFormatException ex) {
                bilgiLabel.setText(" 0-100 arasi sayi girmediniz.");
            } catch (Exception ex) {
                bilgiLabel.setText("⚠ " + ex.getMessage());
            }
        });

        /* NOT GÜNCELLE */
        guncelleBtn.setOnAction(e -> {
            try {
                check.run();
                double puan = Double.parseDouble(notField.getText());
                new NotDAO().notGuncelle(
                        new model.Not(0,
                                ogrList.getSelectionModel().getSelectedItem().getId(),
                                dersBox.getValue().getId(),
                                puan));
                bilgiLabel.setText(" Not güncellendi.");
                ortYenile.run();
            } catch (NumberFormatException ex) {
                bilgiLabel.setText(" 0-100 arası sayı girin!");
            } catch (Exception ex) {
                bilgiLabel.setText("⚠ " + ex.getMessage());
            }
        });

        /* ---------- 4. LAYOUT ---------- */
        VBox root = new VBox(10,
                new Label("Ders Seç:"), dersBox,
                new Label("Öğrenciler:"), ogrList,
                notField, kaydetBtn, guncelleBtn,
                ortLabel,
                bilgiLabel);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 380, 540));
        stage.show();
    }
}
