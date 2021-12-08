package com.example.stay_awake_android.models;

import java.time.LocalDateTime;

public class Task {
    private String id;
    private String title;
    private LocalDateTime date;
    private LocalDateTime duration;
    private String description;
    private int priority;

    public Task() {
    }

    public Task(String id, String title, LocalDateTime date, LocalDateTime duration, String description, int priority) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.duration = duration;
        this.description = description;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDuration() {
        return duration;
    }

    public void setDuration(LocalDateTime duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
