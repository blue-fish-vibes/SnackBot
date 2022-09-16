package com.example.snackbot.data;

public class AppStatus {
    private String username;
    private static final AppStatus instance = new AppStatus();
    public static AppStatus getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        username = name;
    }
}
