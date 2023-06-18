package com.example.musicplayer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.scene.text.TextFlow;

import java.util.Objects;

public class ListItemGridPane extends GridPane {

    private Text textNode;
    private String title;

    private ImageView up, down;
    private int index;

    public ListItemGridPane(String title, int index ) {
        this.title = title;
        this.index= index;

        // Ustawienia kolumn
        ColumnConstraints column1 = new ColumnConstraints(50); // prefWidth=50.0
        ColumnConstraints column2 = new ColumnConstraints(); // hgrow=ALWAYS
        ColumnConstraints column3 = new ColumnConstraints(50); // prefWidth=50.0
        column2.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        getColumnConstraints().addAll(column1, column2, column3);

        // Ustawienia wierszy
        RowConstraints row1 = new RowConstraints(60); // prefHeight=60.0
        row1.setValignment(javafx.geometry.VPos.CENTER);
        getRowConstraints().add(row1);

        // Ustawienia wypełnienia i marginesów
        setPadding(new Insets(5, 10, 0, 10)); // padding: 10.0 10.0 5.0 10.0

        // Tworzenie kontenera VBox
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.setPrefHeight(50);
        up = createImageView("/com/example/musicplayer/images/up.png");
        down = createImageView("/com/example/musicplayer/images/down.png");
        vBox.getChildren().addAll(up, down);
        add(vBox, 2, 0);

        // Tworzenie tekstu
        TextFlow textFlow = createTextFlow();
        add(textFlow, 1, 0);

        LetterCircle letterCircle = new LetterCircle(this.title.charAt(0), 20);
        add(letterCircle, 0,0 );
        // Włączanie widoczności linii siatki (opcjonalne)
        setGridLinesVisible(true);

        widthProperty().addListener((observable, oldValue, newValue) -> {
            int characters = (int) (widthProperty().get() / 10) + 6;
            String truncatedText = title.length() > characters ? title.substring(0, characters)  + "..." : title;
            textNode.setText(truncatedText);
        });

    }

    private ImageView createImageView(String imageUrl) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageUrl))));
        imageView.getStyleClass().add("tile");

        return imageView;
    }


    private TextFlow createTextFlow() {
        TextFlow textFlow = new TextFlow();
        textFlow.setPadding(new Insets(5, 10, 0, 10));
        textNode = new Text();
        textNode.setFont(new Font(20));
        textNode.setBoundsType(TextBoundsType.VISUAL);
        textNode.setTextAlignment(TextAlignment.LEFT);
        textNode.setWrappingWidth(USE_PREF_SIZE);

        int maxCharacters = 20;
        String truncatedText = title.length() > maxCharacters ? title.substring(0, maxCharacters) : title;
        textNode.setText(truncatedText);

        textFlow.getChildren().add(textNode);
        return textFlow;
    }

    public ImageView getUp() {
        return up;
    }

    public ImageView getDown() {
        return down;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle(){
        return  title;
    }


}

