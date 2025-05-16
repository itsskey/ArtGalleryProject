package application;

import java.util.ArrayList;
import java.util.List;

public class Exhibition {
    private String name;
    private String description;
    private final List<Artwork> artworks = new ArrayList<>();
    private int id;

    public Exhibition(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public Exhibition() {
        this.name = "Unknown";
        this.description = "Some description";
    }

    // Геттеры и сеттеры для названия
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Геттеры и сеттеры для описания
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    public List<Artwork> getArtworks() {
        return artworks;
    }

    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
    }
    @Override
    public String toString() {
        return "Exhibition{name='" + name + "', description='" + description + "'}";
    }
}
