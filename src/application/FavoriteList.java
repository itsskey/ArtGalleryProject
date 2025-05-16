package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteList {
    private final List<Artwork> favoriteArtworks = new ArrayList<>();
    private final int userId;

    
    public FavoriteList(int userId) {
        this.userId = userId;
        loadFavoritesFromDB();
    }

    private void loadFavoritesFromDB() {
        String sql = "SELECT artwork_id FROM favorite WHERE user_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int artId = rs.getInt("artwork_id");

                Artwork art = findArtworkById(artId);
                if (art != null) favoriteArtworks.add(art);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addFavorite(Artwork artwork) {
        String sql = "INSERT INTO favorite(user_id, artwork_id) VALUES(?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, artwork.getId());
            stmt.executeUpdate();
            favoriteArtworks.add(artwork);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public void removeFavorite(Artwork artwork) {
        String sql = "DELETE FROM favorite WHERE user_id = ? AND artwork_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, artwork.getId());
            stmt.executeUpdate();
            favoriteArtworks.removeIf(a -> a.getId() == artwork.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Artwork findArtworkById(int id) {
    	 String sql = "SELECT a.artwork_id AS id, a.title AS title, a.year AS year, " +
                 "artist.artist_name AS artist, a.image_path AS image, style.style_name AS style " +
                 "FROM artwork a " +
                 "JOIN artist ON a.artist_id = artist.artist_id " +
                 "JOIN style  ON a.style_id = style.style_id " +
                 "WHERE a.artwork_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                	return new Artwork(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("artist"),
                            rs.getInt("year"),
                            rs.getString("style"),
                            rs.getString("image")
                        );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Метод получения списка избранных работ
    public List<Artwork> getFavorites() {
        return new ArrayList<>(favoriteArtworks);
    }

    // Проверка, является ли работа избранной
    public boolean isFavorite(Artwork artwork) {
    	return favoriteArtworks.stream()
                .anyMatch(a -> a.getId() == artwork.getId());    }
}
