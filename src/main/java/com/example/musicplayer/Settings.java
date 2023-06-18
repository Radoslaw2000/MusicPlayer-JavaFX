package com.example.musicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Settings {
    private static Settings instance;

    private ArrayList<File> songs;

    private boolean looped, random, muted;

    private Settings() {
        songs = new ArrayList<>();
        looped = false;
        random = false;
        muted = false;
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

    public boolean isLooped() {
        return looped;
    }

    public void setLooped(boolean looped) {
        this.looped = looped;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
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
