package lk.ijse.chatting_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ClientInitializer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/chatting_app/view/Client.fxml"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
