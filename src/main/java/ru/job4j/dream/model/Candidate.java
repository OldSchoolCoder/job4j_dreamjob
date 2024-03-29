package ru.job4j.dream.model;

import java.util.Objects;

public class Candidate implements Model {
    private int id;
    private String name;
    private String photo;
    private String city_id;

    public Candidate(int id, String name, String photo, String city_id) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.city_id = city_id;
    }

    public String getCity() {
        return city_id;
    }

    public void setCity(String city_id) {
        this.city_id = city_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return id == candidate.id &&
                Objects.equals(name, candidate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
