package com.example.demo;

public class Event {
    private Long Id;
    private String title;

    public Event(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Event{" + "Id=" + Id + ", title='" + title + '\'' + '}';
    }
}
