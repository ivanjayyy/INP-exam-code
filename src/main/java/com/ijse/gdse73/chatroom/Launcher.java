package com.ijse.gdse73.chatroom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

// client launcher
// launch multiple clients at same time
public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ClientFX.fxml")))));
        stage.show();

        Stage stage2 = new Stage();
        stage2.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ClientFX.fxml")))));
        stage2.show();

        Stage stage3 = new Stage();
        stage3.setScene(new Scene(FXMLLoader.load(getClass().getResource("/ClientFX.fxml"))));
        stage3.show();
    }
}
