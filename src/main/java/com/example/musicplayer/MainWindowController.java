package com.example.musicplayer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {
    @FXML
    private Slider volumeSlider, songProgressBar;
    @FXML
    private Text songLabel, currentTime, endTime;
    @FXML
    private ImageView playAndPauseButton, previousButton, nextButton;

    @FXML
    private VBox songList;


    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;
    private ArrayList<File> songs;

    private int songNumber;
    private Timer timer;
    private TimerTask task;
    private boolean running;

    private static final String DIRECTORY_PATH = "music";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        running = false;
        songs = new ArrayList<File>();
        //directory = new File("music");
        //files = directory.listFiles();
/*
        if(files != null){
            for(File file : files){
                System.out.println(file);
                songs.add(file);
            }

        }*/
        songs = SongsFileManager.loadSongsFromDirectory(DIRECTORY_PATH);

        loadSong();
        setBarListeners();

        for( File file : songs)
            songList.getChildren().add(new ListItemGridPane(file.getName()));

    }

    private void setBarListeners(){
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
        volumeSlider.setValue(20);

        songProgressBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (songProgressBar.isValueChanging()) {
                    double progress = songProgressBar.getValue();
                    Duration seekTime = Duration.seconds(progress);
                    mediaPlayer.seek(seekTime);
                }
            }
        });
        songProgressBar.setOnMouseClicked(event -> {
            double clickPosition = event.getX();
            double progressBarWidth = songProgressBar.getWidth();
            double progress = clickPosition / progressBarWidth;
            Duration seekTime = Duration.seconds(progress * media.getDuration().toSeconds());
            mediaPlayer.seek(seekTime);
        });
    }


    @FXML
    public void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    @FXML
    public void handleDragDropped(DragEvent event) {
        FileHandler fh = new FileHandler();
        Dragboard dragboard = event.getDragboard();
        if (!dragboard.hasFiles())
            return;

        List<File> files = dragboard.getFiles();
        for (File file : files) {
            if(fh.canBeAdded(file)){
                songs.add(file);
                songList.getChildren().add(new ListItemGridPane(file.getName()));
            }

        }

        event.setDropCompleted(true);
        event.consume();
    }







    private void playMedia() {
        beginTimer();
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    private void pauseMedia(){
        cancelTimer();
        mediaPlayer.pause();
    }

    public void playAndPauseButtonAction(){
            if(songs.isEmpty())
                return;

            if(running) {
                playAndPauseButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/musicplayer/images/play.png"))));
                running = false;
                pauseMedia();
                System.out.println("Paused");
            }
            else{
                playAndPauseButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/musicplayer/images/pause.png"))));
                running = true;
                playMedia();
                System.out.println("Playing");
            }

    }

    private void loadSong(){
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
    }
    public void previousButtonAction(){
        mediaPlayer.stop();
        cancelTimer();
        System.out.println("Stopped");

        if(songNumber > 0)
            songNumber--;
        else
            songNumber = songs.size() - 1;

        loadSong();
        if(!running)
            playAndPauseButtonAction();
    }


    public void nextButtonAction(){
        mediaPlayer.stop();
        cancelTimer();
        System.out.println("Stopped");

        if(songNumber < songs.size() - 1)
            songNumber++;
        else
            songNumber = 0;

        loadSong();
        if(!running)
            playAndPauseButtonAction();
    }

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                Duration currentDuration = mediaPlayer.getCurrentTime();
                Duration endDuration = media.getDuration();
                double currentSeconds = currentDuration.toSeconds();
                double endSeconds = endDuration.toSeconds();
                songProgressBar.setMax(endSeconds);
                songProgressBar.setValue(currentSeconds);

                if (currentSeconds / endSeconds == 1)
                    cancelTimer();

                String currentTimeString = formatTime(currentDuration);
                String endTimeString = formatTime(endDuration);
                Platform.runLater(() -> {
                    currentTime.setText(currentTimeString);
                    endTime.setText(endTimeString);
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }


    public void cancelTimer() {
        if(!Objects.isNull(timer)){
            running = false;
            timer.cancel();
        }
    }


}