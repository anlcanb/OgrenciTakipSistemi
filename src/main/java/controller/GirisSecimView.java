package controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GirisSecimView extends Application {

    @Override
    public void start(Stage stage) {
        Button ogrenciBtn  = new Button("👨‍🎓 Öğrenci Girişi");
        Button ogretmenBtn = new Button("👨‍🏫 Öğretmen Girişi");

        ogrenciBtn.setOnAction(e -> {
            LoginView login = new LoginView();
            try {
                login.start(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        ogretmenBtn.setOnAction(e -> {
            TeacherLoginView login = new TeacherLoginView();
            try {
                login.start(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox root = new VBox(15, ogrenciBtn, ogretmenBtn);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 300, 150));
        stage.setTitle("Giriş Türü Seç");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
