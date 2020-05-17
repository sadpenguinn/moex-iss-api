package ru.badrudin.api.model;

public class Market {
    public Long id;
    public String name;
    public String description;

    public Market(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return "Market:{Id: " + id + ", Name: " + name + ", Description: " + description + "}";
    }
}
