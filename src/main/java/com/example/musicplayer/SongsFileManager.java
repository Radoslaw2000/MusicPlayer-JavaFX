package com.example.musicplayer;
import java.io.*;
import java.util.ArrayList;

public class SongsFileManager {

    public static void saveSongsToDirectory(ArrayList<File> songs, String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (File song : songs) {
            try {
                String destinationPath = directoryPath + "/" + song.getName();
                File destinationFile = new File(destinationPath);
                if (!destinationFile.exists()) {
                    copyFile(song, destinationFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static ArrayList<File> loadSongsFromDirectory(String directoryPath) {
        ArrayList<File> songs = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) {
                        songs.add(file);
                    }
                }
            }
        }
        return songs;
    }

    private static void copyFile(File sourceFile, File destinationFile) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}
