package com.example.musicplayer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
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
    private ImageView playAndPauseButton;

    @FXML
    private VBox songList;


    private Media media;
    private MediaPlayer mediaPlayer;
    private int songNumber;
    private Timer timer;
    private TimerTask task;
    private boolean running;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        running = false;
        Settings.getInstance().setSongs( SongsFileManager.loadSongsFromDirectory("music") );

        if(Settings.getInstance().getSongs().isEmpty()){
            System.out.println("pusto");
            return;
        }

        loadSongToMediaPlayer();
        setBarListeners();

        fillSongList();



    }

    void fillSongList(){
        for (File file : Settings.getInstance().getSongs()) {
            int index = songList.getChildren().size(); // Pobierz indeks przed dodaniem elementu do listy
            ListItemGridPane li = new ListItemGridPane(file.getName(), index);
            li.getUp().setOnMouseClicked(event2 -> {
                System.out.println("aaa");
            });
            addDragEventsToListItem(li, index); // Przekaż indeks jako argument
            songList.getChildren().add(li);

        }
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
        boolean wasEmpty = Settings.getInstance().getSongs().isEmpty();

        FileHandler fh = new FileHandler();
        Dragboard dragboard = event.getDragboard();
        if (!dragboard.hasFiles())
            return;

        List<File> files = dragboard.getFiles();
        for (File file : files) {
            if (fh.canBeAdded(file)) {
                Settings.getInstance().addSong(file);

                int index = songList.getChildren().size();
                ListItemGridPane li = new ListItemGridPane(file.getName(), index);
                li.getUp().setOnMouseClicked(event2 -> {
                    System.out.println("aaa");
                });
                addDragEventsToListItem(li, index); // Przypisz zdarzenia przeciągania i upuszczania
                songList.getChildren().add(li);
            }
        }

        event.setDropCompleted(true);
        event.consume();

        if (wasEmpty) {
            loadSongToMediaPlayer();
            setBarListeners();
        }

        // Przypisz ponownie zdarzenia przeciągania i upuszczania dla wszystkich elementów w songList
        for (int i = 0; i < songList.getChildren().size(); i++) {
            ListItemGridPane li = (ListItemGridPane) songList.getChildren().get(i);
            addDragEventsToListItem(li, i);
        }
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
            if(Settings.getInstance().getSongs().isEmpty())
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

    private void loadSongToMediaPlayer(){
        media = new Media(Settings.getInstance().getSong(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(Settings.getInstance().getSong(songNumber).getName());
        mediaPlayer.setOnReady(() -> {
            Duration duration = media.getDuration();
            String endTimeString = formatTime(duration);
            Platform.runLater(() -> endTime.setText(endTimeString));
        });
    }
    public void previousButtonAction(){
        if(Settings.getInstance().getSongs().isEmpty())
            return;

        mediaPlayer.stop();
        cancelTimer();
        System.out.println("Stopped");

        if(songNumber > 0)
            songNumber--;
        else
            songNumber = Settings.getInstance().getSongs().size() - 1;

        loadSongToMediaPlayer();
        if(!running)
            playAndPauseButtonAction();
    }


    public void nextButtonAction(){
        if(Settings.getInstance().getSongs().isEmpty())
            return;

        mediaPlayer.stop();
        cancelTimer();
        System.out.println("Stopped");

        if(songNumber < Settings.getInstance().getSongs().size() - 1)
            songNumber++;
        else
            songNumber = 0;

        loadSongToMediaPlayer();
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

                if (currentSeconds / endSeconds == 1){
                    cancelTimer();
                    nextButtonAction();
                }


                String currentTimeString = formatTime(currentDuration);
                Platform.runLater(() -> {
                    currentTime.setText(currentTimeString);
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



    void addDragEventsToListItem(ListItemGridPane li, int index) {
        // Dodawanie obsługi zdarzeń przeciągania i upuszczania
        li.setOnDragDetected(dragEvent -> {
            /* Utwórz ClipboardContent i ustaw odpowiednie dane, na przykład indeks elementu lub same dane */
            Dragboard dragboard = li.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(index));
            System.out.println("index: " + index);
            dragboard.setContent(content);
            dragEvent.consume();
        });
        li.setOnDragOver(dragEvent -> {
            /* Sprawdź, czy przeciągane dane są obsługiwane */
            if (dragEvent.getDragboard().hasString()) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
            dragEvent.consume();
        });
        li.setOnDragEntered(dragEvent -> {
            /* Zastosuj efekt wizualny, na przykład zmiana koloru tła */
            li.setStyle("-fx-background-color: lightblue;");
            dragEvent.consume();
        });
        li.setOnDragExited(dragEvent -> {
            /* Przywróć domyślny efekt wizualny */
            li.setStyle("-fx-background-color: transparent;");
            dragEvent.consume();
        });
        li.setOnDragDropped(dragEvent -> {
            /* Pobierz dane przeciąganej pozycji */
            Dragboard dragboard = dragEvent.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                int draggedIndex = Integer.parseInt(dragboard.getString());
                int targetIndex = index; // Użyj indeksu przekazanego jako argument metody
                System.out.println("draggedIndex: " + draggedIndex);
                System.out.println("targetIndex: " + targetIndex);

                if (draggedIndex != targetIndex) {
                    Settings.getInstance().swapSongs(draggedIndex, targetIndex);
                    success = true;
                    if(songNumber == draggedIndex)
                        songNumber = targetIndex;
                    else if(songNumber == targetIndex)
                        songNumber = draggedIndex;
                }
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
        li.setOnDragDone(dragEvent -> {
            /* Przywróć domyślny efekt wizualny */
            li.setStyle("-fx-background-color: transparent;");
            dragEvent.consume();
            songList.getChildren().clear();
            fillSongList();
            System.out.println("zmieniono");
        });


    }




}