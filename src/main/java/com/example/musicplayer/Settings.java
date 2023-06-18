package com.example.musicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Settings {
    private static Settings instance;

    private ArrayList<File> songs;

    private Settings() {
        songs = new ArrayList<>();
    }

    public static Settings getInstance() {
        if (instance == null) {
            synchronized (Settings.class) {
                if (instance == null) {
                    instance = new Settings();
                }
            }
        }
        return instance;
    }

    public ArrayList<File> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<File> songs) {
        this.songs = songs;
    }

    public void addSong(File file){
        songs.add(file);
    }

    public File getSong(int songNumber){
        return songs.get(songNumber);
    }

    public void swapSongs(int index1, int index2) {
        Collections.swap(Settings.getInstance().getSongs(), index1, index2);
    }
}
