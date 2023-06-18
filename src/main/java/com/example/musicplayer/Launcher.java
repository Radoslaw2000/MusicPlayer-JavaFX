package com.example.musicplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MP3 Player");
        stage.setScene(scene);
        stage.show();

        stage.setMinWidth(400);
        stage.getIcons().add( new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/musicplayer/images/icon.png"))) );

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent arg0) {
                SongsFileManager.saveSongsToDirectory(Settings.getInstance().getSongs(), "music");
                System.out.println("Bajojajo");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}