package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT password FROM \"user\" WHERE username = ?";
        return checkCredentials(sql, username, password);
    }

    public boolean authenticateAdmin(String login, String password) {
        String sql = "SELECT password FROM \"admin\" WHERE login = ?";
        return checkCredentials(sql, login, password);
    }

    private boolean checkCredentials(String sql, String username, String password) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                return storedHash.equals(password); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int fetchUserId(String username) {
        String sql = "SELECT user_id FROM \"user\" WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}