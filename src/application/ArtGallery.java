package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class ArtGallery extends Application {

    // Список выставок
    private List<Exhibition> exhibitions = new ArrayList<>();
    // Объявляем mainLayout как поле класса
    private VBox mainLayout;
    private boolean isAdmin = false;
    private FavoriteList favoriteList;
    
    // Константы
    public static final String ACCENT_COLOR = "#6f4a25;";
    public static final String DARK_COLOR = "#3e2d1b;";
    public static final String DARK_RED_COLOR = "#780000;";
    public static final String BEIGE_COLOR = "#f8eddb;";
    
    public static final String FONT = "Book Antiqua";
    public static final String BUTTON_STYLE = "-fx-background-color: " 
			+ ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 15px;";
    public static final String SCROLL_PANE_STYLE = "-fx-background-color: "
			+ BEIGE_COLOR + "; -fx-border-color: " + ACCENT_COLOR;
    public static final String MAIN_BG = "-fx-background-color: "+BEIGE_COLOR;
    
    
   @Override
    public void start(Stage primaryStage) {
	   new LoginScreen(this).show(); // Запуск экрана входа
   }
   
   
   public void startMainWindow(boolean isAdmin, int userId) {
       this.isAdmin = isAdmin;
       favoriteList = new FavoriteList(userId);
       Stage primaryStage = new Stage();
       
       mainLayout = new VBox(10);
       mainLayout.setPadding(new Insets(10));
       
       loadDataFromDatabase(); // Загрузка данных из БД
       updateMainLayout(); // Отрисовка интерфейса
       
       // Установка фона и создание сцены
       Image backgroundMain = new Image(getClass().getResourceAsStream("/images/back_main.jpg"));
       ImageView backgroundView = new ImageView(backgroundMain);
       backgroundView.setFitWidth(600);
       backgroundView.setFitHeight(400);
       backgroundView.setPreserveRatio(false);

       StackPane root = new StackPane(backgroundView, mainLayout);
       primaryStage.setScene(new Scene(root, 600, 370));
       primaryStage.setTitle("Галерея искусств");
       primaryStage.show();
   }
   
   
   public void loadDataFromDatabase() {
       exhibitions.clear();
       String sql = "SELECT exhibition_id as id, exhibition_name as name, description FROM exhibition";

       try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
           
           ResultSet rs = stmt.executeQuery();
           while (rs.next()) {
               Exhibition exhibition = new Exhibition(
                   rs.getString("name"),
                   rs.getString("description")
               );
               exhibition.setId(rs.getInt("id"));
               loadArtworks(exhibition);
               exhibitions.add(exhibition);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
   
   
   private void loadArtworks(Exhibition exhibition) {
       String sql = "SELECT a.artwork_id as id, a.title, a.year, a.image_path, ar.artist_name AS artist, s.style_name AS style " +
                    "FROM artwork a " +
                    "JOIN artist ar ON a.artist_id = ar.artist_id " +
                    "JOIN style s ON a.style_id = s.style_id " +
                    "WHERE a.artwork_id IN (SELECT artwork_id FROM exhibition_artwork WHERE exhibition_id = ?)";

       try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
           
           stmt.setInt(1, exhibition.getId());
           ResultSet rs = stmt.executeQuery();

           while (rs.next()) {
               Artwork artwork = new Artwork(
            	   rs.getInt("id"),
                   rs.getString("title"),
                   rs.getString("artist"),
                   rs.getInt("year"),
                   rs.getString("style"),
                   rs.getString("image_path")
               );
               exhibition.addArtwork(artwork);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }


   private void updateMainLayout() {
	    mainLayout.getChildren().clear();

	    // Заголовок
	    Label header = new Label("Текущие выставки:");
	    header.setFont(Font.font(FONT, FontWeight.BOLD, 20));
	    header.setStyle("-fx-text-fill: "+DARK_COLOR);
	    mainLayout.getChildren().add(header);

	    // Контейнер для выставок
	    VBox exhibitionsBox = new VBox(10);
	    exhibitionsBox.setStyle(MAIN_BG);

	    // Список выставок
	    for (Exhibition ex : exhibitions) {
	        HBox row = new HBox(20);
	        row.setPadding(new Insets(15));
	        row.setStyle("-fx-border-color: " + ACCENT_COLOR
	                   + "-fx-border-width: 2;"
	                   + "-fx-background-color: "+BEIGE_COLOR
	                   + "-fx-background-radius: 5;"
	                   + "-fx-border-radius: 5;");

	        Label name = new Label(ex.getName());
	        name.setFont(Font.font(FONT, FontWeight.BOLD, 15));
	        name.setStyle("-fx-text-fill: "+DARK_COLOR);

	        Label desc = new Label(ex.getDescription());
	        desc.setFont(Font.font(FONT,FontWeight.SEMI_BOLD, 12));
	        desc.setStyle("-fx-text-fill: "+ACCENT_COLOR);
	        desc.setWrapText(true);

	        row.getChildren().addAll(name, desc);
	        row.setOnMouseClicked(e -> openExhibitionWindow(ex));
	        exhibitionsBox.getChildren().add(row);
	    }

	    // Скролл-панель
	    ScrollPane sp = new ScrollPane(exhibitionsBox);
	    sp.setStyle(SCROLL_PANE_STYLE);
	    sp.setFitToWidth(true);
	    sp.setPrefViewportHeight(400);
	    mainLayout.getChildren().add(sp);

	    // Кнопки
	    HBox buttons = new HBox(30);
	    buttons.setPadding(new Insets(15));
	    buttons.setAlignment(Pos.CENTER);
	  
	    Button searchBtn = createStyledButton("Поиск картин", BUTTON_STYLE);
	    searchBtn.setOnAction(e -> openSearchWindow());

	    Button favBtn = createStyledButton("Избранное", BUTTON_STYLE);
	    favBtn.setOnAction(e -> openFavoritesWindow());
	    favBtn.setVisible(!isAdmin);

	    Button adminBtn = createStyledButton("Редактировать выставки", BUTTON_STYLE);
	    adminBtn.setVisible(isAdmin);
	    adminBtn.setOnAction(e -> new AdminPanel(exhibitions, this, isAdmin).show());


	    buttons.getChildren().addAll(searchBtn, favBtn, adminBtn);
	    mainLayout.getChildren().add(buttons);
	}

   
   public void refreshMainView() {
	    loadDataFromDatabase();
	    updateMainLayout();
	}
   
   
   private void openExhibitionWindow(Exhibition exhibition) {
	   Stage st = new Stage();
       st.setTitle("Выставка: " + exhibition.getName());
       st.setMinWidth(600);
    
       GridPane grid = new GridPane();
       grid.setPadding(new Insets(25));
       grid.setHgap(30); 
       grid.setVgap(30);
       grid.setAlignment(Pos.TOP_CENTER);
       grid.setStyle(MAIN_BG);

       int row = 0, col = 0;
       for (Artwork art : exhibition.getArtworks()) {
           VBox box = new VBox(10);
           box.setAlignment(Pos.CENTER);
           box.setStyle("-fx-border-color: " + ACCENT_COLOR + "; -fx-padding: 15;");
           // Изображение
           ImageView iv = new ImageView();
           try {
               InputStream is = getClass().getResourceAsStream(art.getImagePath());
               if (is == null) is = getClass().getResourceAsStream("/images/default.jpg");
               Image img = new Image(is);
               iv.setImage(img);
           } catch (Exception e) {
               System.err.println("Ошибка загрузки: " + e.getMessage());
           }
           iv.setFitWidth(280); 
           iv.setFitHeight(220);
           iv.setPreserveRatio(true);

           // Описание
           Label lbl = createDetailLabel(String.format("%s\n%s, %d\n%s",
               art.getName(), art.getArtist(), art.getYear(), art.getStyle()));
           lbl.setMaxWidth(250);
           
           // Кнопки
           HBox btns = new HBox(15);
           btns.setAlignment(Pos.CENTER);
           if (!isAdmin) {
               Button like = createStyledButton(favoriteList.isFavorite(art) ? "★" : "☆", BUTTON_STYLE);
               like.setOnAction(e -> toggleFavorite(art, like));
               btns.getChildren().add(like);
           }
           Button info = createStyledButton("Подробнее", BUTTON_STYLE);
           info.setOnAction(e -> showArtworkDetails(art));
           btns.getChildren().add(info);

           box.getChildren().addAll(iv, lbl, btns);
           grid.add(box, col, row);

           if (++col == 2) { // 2 колонки вместо 3
               col = 0; 
               row++;
           }
       }

       ScrollPane sp = new ScrollPane(grid);
       sp.setStyle(MAIN_BG);
       sp.setFitToWidth(true);
       sp.setPrefViewportHeight(600);

       Scene scene = new Scene(sp, 900, 700);
       st.setScene(scene);
       st.show();
   }

   
   private void showArtworkDetails(Artwork art) {
	   Stage st = new Stage();
       st.setTitle(art.getName() + " - Детали");
       st.setMinWidth(400);

       VBox layout = new VBox(20);
       layout.setPadding(new Insets(25));
       layout.setStyle(MAIN_BG);

       // Изображение
       ImageView iv = new ImageView();
       try (InputStream is = getClass().getResourceAsStream(art.getImagePath())) {
           iv.setImage(new Image(is != null ? is : getClass().getResourceAsStream("/images/default.jpg")));
       } catch (Exception e) {
           System.err.println("Ошибка: " + e.getMessage());
       }
       iv.setFitWidth(350);
       iv.setFitHeight(280);
       iv.setPreserveRatio(true);

       // Заголовок
       Label title = new Label(art.getName());
       title.setFont(Font.font(FONT));
       title.setStyle("-fx-font-size: 20px; -fx-text-fill: " + ACCENT_COLOR + ";");
       
    // Получаем годы жизни художника
       String artistYears = getArtistYears(art.getArtist());
       
       // Детали
       VBox details = new VBox(8);
       details.getChildren().addAll(
           createDetailLabel("Художник: " + art.getArtist() + " " + artistYears),
           createDetailLabel("Год: " + art.getYear()),
           createDetailLabel("Стиль: " + art.getStyle()),
           createDetailLabel("Описание: " + getArtworkDescription(art))
       );

       layout.getChildren().addAll(iv, title, details);
       st.setScene(new Scene(layout, 500, 600));
       st.show();
   }
   
   private String getArtistYears(String artistName) {
	    String years = "";
	    String sql = "SELECT birth_year, death_year FROM artist WHERE artist_name = ?";
	    
	    try (Connection conn = DatabaseConnector.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, artistName);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            int birthYear = rs.getInt("birth_year");
	            Integer deathYear = rs.getObject("death_year", Integer.class);
	            
	            if (deathYear != null) {
	                years = String.format("(%d-%d)", birthYear, deathYear);
	            } else {
	                years = String.format("(род. %d)", birthYear);
	            }
	        } else {
	            years = "(даты неизвестны)";
	        }
	    } catch (SQLException e) {
	        System.err.println("Ошибка получения данных художника: " + e.getMessage());
	        years = "(ошибка загрузки дат)";
	    }
	    
	    return years;
	}

   
   
   private String getArtworkDescription(Artwork art) {
	   String description = null;
	   String sql = "SELECT description FROM artwork WHERE artwork_id = ?";
	    
	    try (Connection conn = DatabaseConnector.getConnection();
	        PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setInt(1, art.getId());
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            description = rs.getString("description");
	            if (description == null) description = "Описание отсутствует";
	        }        
	    } catch (SQLException e) {
	        System.err.println("Ошибка получения описания: " + e.getMessage());
	    }
	    
	    return description;
   }


   private void openSearchWindow() {
	   SearchPanel searchPanel = new SearchPanel(exhibitions);
       searchPanel.show();
   }

   
   private void openFavoritesWindow() {
	   Stage favoritesStage = new Stage();
       favoritesStage.setTitle("Избранные картины");

       VBox favoritesLayout = new VBox(10);
       favoritesLayout.setPadding(new Insets(10));
       favoritesLayout.setStyle("-fx-background-color: "+BEIGE_COLOR);

       ScrollPane scrollPane = new ScrollPane();
       scrollPane.setContent(favoritesLayout);
       scrollPane.setFitToWidth(true);
       scrollPane.setStyle(SCROLL_PANE_STYLE);

       for (Artwork artwork : favoriteList.getFavorites()) {
           VBox artworkBox = new VBox(10);
           artworkBox.setAlignment(Pos.CENTER);
           artworkBox.setStyle("-fx-border-color: "+ACCENT_COLOR+ "-fx-padding: 5; -fx-border-width: 5 5 5 5;");

           // Загрузка изображения с обработкой ошибок
           ImageView imageView = new ImageView();
           try {
               InputStream is = getClass().getResourceAsStream(artwork.getImagePath());
               if (is == null) {
                   throw new IllegalArgumentException("Изображение не найдено: " + artwork.getImagePath());
               }
               Image image = new Image(is);
               imageView.setImage(image);
           } catch (Exception e) {
               System.err.println("Ошибка загрузки: " + e.getMessage());
               InputStream defaultIs = getClass().getResourceAsStream("/images/default.jpg");
               if (defaultIs != null) {
                   imageView.setImage(new Image(defaultIs));
               }
           }

           imageView.setFitWidth(200);
           imageView.setFitHeight(150);
           imageView.setPreserveRatio(true);

           Label description = new Label(String.format("%s\n%s, %d\n%s",
                   artwork.getName(),
                   artwork.getArtist(),
                   artwork.getYear(),
                   artwork.getStyle()));
           description.setFont(Font.font(FONT));
           description.setStyle("-fx-font-size: 12px; -fx-text-alignment: center;");

           artworkBox.getChildren().addAll(imageView, description);
           favoritesLayout.getChildren().add(artworkBox);
       }

       Scene favoritesScene = new Scene(scrollPane, 500, 450);
       favoritesStage.setScene(favoritesScene);
       favoritesStage.show();
   }
   
   
	// Вспомогательный метод для создания кнопок
	private Button createStyledButton(String text, String style) {
	    Button btn = new Button(text);
	    btn.setStyle(style);
	    btn.setFont(Font.font(FONT));
	    btn.setPadding(new Insets(10));
	    btn.setOnMouseEntered(e -> btn.setStyle(style + "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);"));
	    btn.setOnMouseExited(e -> btn.setStyle(style));
	    return btn;
	}

   
   private Label createDetailLabel(String text) {
       Label lbl = new Label(text);
       lbl.setFont(Font.font(FONT));
       lbl.setWrapText(true);
       lbl.setStyle("-fx-text-fill: #5e4b32; -fx-font-size: 14px;");
       return lbl;
   }   
   
   
   private void toggleFavorite(Artwork art, Button btn) {
       if (favoriteList.isFavorite(art)) {
           favoriteList.removeFavorite(art);
           btn.setText("☆");
       } else {
           favoriteList.addFavorite(art);
           btn.setText("★");
       }
   }
   
    
    public static void main(String[] args) {
        launch(args);
    }
}