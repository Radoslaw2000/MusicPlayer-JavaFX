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

    public ListItemGridPane(String title) {
        // Ustawienia kolumn
        ColumnConstraints column1 = new ColumnConstraints(50); // prefWidth=50.0
        ColumnConstraints column2 = new ColumnConstraints(); // hgrow=ALWAYS
        ColumnConstraints column3 = new ColumnConstraints(50); // prefWidth=50.0
        column2.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        getColumnConstraints().addAll(column1, column2, column3);

        // Ustawienia wierszy
        RowConstraints row1 = new RowConstraints(50); // prefHeight=50.0
        row1.setValignment(javafx.geometry.VPos.CENTER);
        getRowConstraints().add(row1);

        // Ustawienia wypełnienia i marginesów
        setPadding(new Insets(5, 10, 0, 10)); // padding: 10.0 10.0 5.0 10.0

        // Tworzenie kontenera VBox
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.setPrefHeight(50);
        vBox.getChildren().addAll(
                createImageView("/com/example/musicplayer/images/speaker.png"),
                createImageView("/com/example/musicplayer/images/speaker.png")
        );
        add(vBox, 2, 0); // Dodanie VBox do kolumny 2

        // Tworzenie tekstu
        TextFlow textFlow = createTextFlow(title);
        add(textFlow, 1, 0); // Dodanie TextFlow do kolumny 1

        LetterCircle letterCircle = new LetterCircle(title.charAt(0), 20);
        add(letterCircle, 0,0 );
        // Włączanie widoczności linii siatki (opcjonalne)
        setGridLinesVisible(true);
    }

    private ImageView createImageView(String imageUrl) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageUrl))));

        return imageView;
    }


    private TextFlow createTextFlow(String text) {
        TextFlow textFlow = new TextFlow();
        textFlow.setPadding(new Insets(5, 10, 0, 10));
        Text textNode = new Text();
        textNode.setFont(new Font(20));
        textNode.setBoundsType(TextBoundsType.VISUAL);
        textNode.setTextAlignment(TextAlignment.LEFT);
        textNode.setWrappingWidth(USE_PREF_SIZE);

        // Ograniczenie tekstu do określonej liczby znaków (np. 10)
        int maxCharacters = (int) textFlow.getWidth()/ 10;
        System.out.println("maxCharacters: "+maxCharacters);
        String truncatedText = text.length() > maxCharacters ? text.substring(0, maxCharacters) : text;
        textNode.setText(truncatedText);

        textFlow.getChildren().add(textNode);
        return textFlow;
    }


}

