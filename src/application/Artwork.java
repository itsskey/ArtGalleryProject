package application;


public class Artwork {
    private final String name;
    private final String artist;
    private final int year;
    private final String style;
    private final String imagePath;
    private final int id;

    public Artwork( int id, String name, String artist, int year, String style, String imagePath) {
        this.id=id;
    	this.name = name;
        this.artist = artist;
        this.year = year;
        this.style = style;
        this.imagePath = imagePath;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artwork)) return false;
        Artwork other = (Artwork) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public int getYear() {
        return year;
    }

    public String getStyle() {
        return style;
    }

    public String getImagePath() {
        return imagePath;
    }
    
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s by %s (%d) [%s]", name, artist, year, style);
    }
}
