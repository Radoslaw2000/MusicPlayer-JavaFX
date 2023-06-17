package com.example.musicplayer;

import java.io.File;

public class FileHandler {

    public FileHandler(){}
    public boolean canBeAdded(File file) {
        if (isMP3File(file)) {
            System.out.println("Dodano plik: " + file.getName());
            return true;
        }
        System.out.println("Nie można dodać pliku: " + file.getName() + " (nieprawidłowe rozszerzenie)");
        return false;
    }

    private boolean isMP3File(File file) {
        String extension = getFileExtension(file);
        return extension.equalsIgnoreCase("mp3");
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

}
