package com.example.stay_awake_android.models;

import java.time.LocalDate;

public class Task {
    private String id;
    private String title;
    private LocalDate day;
    private String hour;
    private int duration;
    private String description;
    private int priority;
    private boolean checked;

    public Task() {
    }

    public Task(String id, String title, LocalDate day, String hour, int duration, String description, int priority, boolean checked) {
        this.id = id;
        this.title = title;
        this.day = day;
        this.hour = hour;
        this.duration = duration;
        this.description = description;
        this.priority = priority;
        this.checked = checked;
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

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
