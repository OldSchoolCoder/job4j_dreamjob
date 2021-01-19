package ru.job4j.dream.model;

import java.util.Objects;

public class Photo implements Model {
    private int id;
    private String name;

    public Photo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return id == photo.id && name.equals(photo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
