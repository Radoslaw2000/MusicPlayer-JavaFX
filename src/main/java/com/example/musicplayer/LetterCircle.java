package com.example.musicplayer;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LetterCircle extends StackPane {

    public LetterCircle(char letter) {
        Circle circle = new Circle(12.5, Color.rgb(47, 94, 161));
        Text text = new Text(String.valueOf(letter).toUpperCase());
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        text.setFill(Color.WHITE);
        this.getChildren().addAll(circle, text);
    }

    public LetterCircle(char letter, double radius) {
        Circle circle = new Circle(radius, Color.rgb(47, 94, 161));
        Text text = new Text(String.valueOf(letter).toUpperCase());
        text.setFont(Font.font("Arial", FontWeight.BOLD, radius));
        text.setFill(Color.WHITE);
        this.getChildren().addAll(circle, text);
    }
}