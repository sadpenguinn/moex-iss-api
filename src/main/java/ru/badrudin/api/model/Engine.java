package ru.badrudin.api.model;

public class Engine {
    public Long id;
    public String name;
    public String description;

    public Engine(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return "Engine:{Id: " + id + ", Name: " + name + ", Description: " + description + "}";
    }
}
