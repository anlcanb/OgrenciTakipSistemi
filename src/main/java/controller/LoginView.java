package controller;

import dao.OgrenciDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Ogrenci;

public class LoginView extends Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Kullanıcı Adı");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Şifre");

        Button loginButton = new Button("Giriş Yap");
        Button registerButton = new Button("Üniversite Yeni Kayıt");

        Label message = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            OgrenciDAO dao = new OgrenciDAO();
            Ogrenci ogr = dao.girisYap(username, password);
            if (ogr != null) {
                message.setText("✅ Giriş başarılı: " + ogr.getAd() +" " +ogr.getSoyad());
                NotListView.show(ogr.getId());
            } else {
                message.setText("❌ Hatalı kullanıcı adı veya şifre!");
            }
        });

        registerButton.setOnAction(e -> {
            RegisterView.show(); // Ayrı pencereyi göster
        });

        root.getChildren().addAll(
                new Label("Giriş Yap"),
                usernameField, passwordField,
                loginButton, registerButton,
                message
        );

        Scene scene = new Scene(root, 300, 250);
        stage.setTitle("Öğrenci Giriş");
        stage.setScene(scene);
        stage.show();
    }
}
