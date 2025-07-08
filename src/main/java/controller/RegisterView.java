package controller;

import dao.OgrenciDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Ogrenci;

public class RegisterView {

    public static void show() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        TextField adField = new TextField();
        adField.setPromptText("Ad");

        TextField soyadField = new TextField();
        soyadField.setPromptText("Soyad");

        TextField kullaniciAdiField = new TextField();
        kullaniciAdiField.setPromptText("Öğrenci No");

        PasswordField sifreField = new PasswordField();
        sifreField.setPromptText("Şifre");

        Button kayitButton = new Button("Kaydı Tamamla");
        Label status = new Label();

        kayitButton.setOnAction(e -> {
            Ogrenci yeni = new Ogrenci(0,
                    adField.getText(),
                    soyadField.getText(),
                    kullaniciAdiField.getText(),
                    sifreField.getText());

            OgrenciDAO dao = new OgrenciDAO();
            dao.ogrenciEkle(yeni);
            status.setText(" Kayıt başarılı!");
        });

        root.getChildren().addAll(
                new Label("Yeni Kayıt"),
                adField, soyadField,
                kullaniciAdiField, sifreField,
                kayitButton,
                status
        );

        Scene scene = new Scene(root, 300, 300);
        stage.setTitle("Yeni Kayıt");
        stage.setScene(scene);
        stage.show();
    }
}
