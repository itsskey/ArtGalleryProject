package application;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
	private static final Properties props = new Properties();
	
	
	static {
        // Попытка загрузить config.properties из classpath
        try (InputStream in = DatabaseConnector.class
                                         .getClassLoader()
                                         .getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("Файл config.properties не найден в classpath");
            }
            props.load(in);

            // Теперь свойства точно есть, загружаем драйвер
            String driver = props.getProperty("db.driver");
            if (driver == null) {
                throw new IllegalStateException("Свойство db.driver не задано в config.properties");
            }
            Class.forName(driver);

        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Не удалось инициализировать DatabaseConnector: " + e);
        }
    }
	
	public static Connection getConnection() throws SQLException {
        String url  = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");
        return DriverManager.getConnection(url, user, pass);
    }
}