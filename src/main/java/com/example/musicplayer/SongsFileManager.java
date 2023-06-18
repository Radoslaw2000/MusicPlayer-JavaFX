package com.example.musicplayer;
import java.io.*;
import java.util.ArrayList;

public class SongsFileManager {

    private static final String SONG_ORDER_FILE = "song_order.txt";

    public static void saveSongsToDirectory(ArrayList<File> songs, String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Zapisz kolejność utworów do pliku song_order.txt
        saveSongOrder(songs, directoryPath + "/" + SONG_ORDER_FILE);

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
                // Sprawdź czy plik song_order.txt istnieje
                File songOrderFile = new File(directoryPath + "/" + SONG_ORDER_FILE);
                if (songOrderFile.exists()) {
                    // Wczytaj kolejność utworów z pliku song_order.txt
                    ArrayList<String> songOrder = loadSongOrder(directoryPath + "/" + SONG_ORDER_FILE);

                    // Dodawaj pliki do listy utworów w kolejności z pliku song_order.txt
                    for (String fileName : songOrder) {
                        File file = new File(directoryPath + "/" + fileName);
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) {
                            songs.add(file);
                        }
                    }
                }
            }
        }
        return songs;
    }

    private static void saveSongOrder(ArrayList<File> songs, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (File song : songs) {
                writer.println(song.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> loadSongOrder(String filePath) {
        ArrayList<String> songOrder = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                songOrder.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songOrder;
    }
    public static void createEmptySongOrderFile(String directoryPath) {
        String filePath = directoryPath + "/" + SONG_ORDER_FILE;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
