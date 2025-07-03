package controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import dao.OgretmenDAO;
import model.Ogretmen;
import controller.TeacherPanel;

public class TeacherLoginView extends Application {

    @Override
    public void start(Stage st) {
        TextField user = new TextField();
        user.setPromptText("Kullanıcı Adı");

        PasswordField pass = new PasswordField();
        pass.setPromptText("Şifre");

        Button btn = new Button("Öğretmen Giriş");
        Label  msg = new Label();

        btn.setOnAction(e -> {
            OgretmenDAO dao = new OgretmenDAO();
            Ogretmen g     = dao.girisYap(user.getText().trim(), pass.getText());

            if (g != null) {
                TeacherPanel.show(g.getId());   // ikinci pencereyi aç
                st.close();
            } else {
                msg.setText("❌ Hatalı giriş!");
            }
        });

        VBox root = new VBox(10, user, pass, btn, msg);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        st.setScene(new Scene(root, 300, 200));
        st.setTitle("Öğretmen Giriş");
        st.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
