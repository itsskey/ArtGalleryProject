package application;

import java.io.InputStream;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.List;


public class SearchPanel {

    private final List<Exhibition> exhibitions;

    public SearchPanel(List<Exhibition> exhibitions) {
        this.exhibitions = exhibitions;
    }

    public void show() {
        Stage searchStage = new Stage();
        searchStage.setTitle("Поиск картин");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField artistField = new TextField();
        artistField.setPromptText("Художник");
        artistField.setStyle("-fx-background-color: white;");

        TextField nameField = new TextField();
        nameField.setPromptText("Название картины");
        nameField.setStyle("-fx-background-color: white;");

        TextField yearField = new TextField();
        yearField.setPromptText("Год создания (целое число)");
        yearField.setStyle("-fx-background-color: white;");

        Button searchButton = new Button("Искать");
        searchButton.setFont(Font.font(ArtGallery.FONT));
        searchButton.setStyle("-fx-background-color: "+ArtGallery.ACCENT_COLOR+ "-fx-text-fill: white;");

        GridPane resultGrid = new GridPane();
        resultGrid.setPadding(new Insets(10));
        resultGrid.setHgap(15);
        resultGrid.setVgap(15);
        resultGrid.setStyle("-fx-background-color: " + ArtGallery.BEIGE_COLOR);
        resultGrid.setAlignment(Pos.CENTER); 


        ScrollPane scrollPane = new ScrollPane(resultGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: "+ArtGallery.BEIGE_COLOR);

        searchButton.setOnAction(e -> {
            String artist = artistField.getText().trim().toLowerCase();
            String name = nameField.getText().trim().toLowerCase();
            int year = -1;

            resultGrid.getChildren().clear();

            try {
                if (!yearField.getText().isEmpty()) {
                    year = Integer.parseInt(yearField.getText().trim());
                    if (year < 0) {
                        throw new NumberFormatException();
                    }
                }
            } catch (NumberFormatException ex) {
                resultGrid.add(new Label("Ошибка: Некорректный формат года"), 0, 0);
                return;
            }

            List<Artwork> results = new ArrayList<>();

            for (Exhibition exhibition : exhibitions) {
                for (Artwork artwork : exhibition.getArtworks()) {
                    boolean matchesArtist = artist.isEmpty() 
                        || artwork.getArtist().toLowerCase().contains(artist);
                    boolean matchesName = name.isEmpty() 
                        || artwork.getName().toLowerCase().contains(name);
                    boolean matchesYear = year == -1 
                        || artwork.getYear() == year;

                    if (matchesArtist && matchesName && matchesYear) {
                        results.add(artwork);
                    }
                }
            }

            if (results.isEmpty()) {
                resultGrid.add(new Label("Ничего не найдено"), 0, 0);
            } else {
                int row = 0;
                for (Artwork artwork : results) {
                    VBox artworkBox = new VBox(10);
                    artworkBox.setStyle("-fx-border-color: "+ArtGallery.ACCENT_COLOR+ "-fx-padding: 5;");
                    artworkBox.setAlignment(Pos.CENTER);
                    // Загрузка изображения
                    ImageView imageView = new ImageView();
                    try {
                        InputStream is = getClass().getResourceAsStream(artwork.getImagePath());
                        if (is == null) {
                            throw new IllegalArgumentException("Изображение не найдено");
                        }
                        Image image = new Image(is);
                        imageView.setImage(image);
                    } catch (Exception ex) {
                        System.err.println("Ошибка загрузки: " + ex.getMessage());
                        InputStream defaultIs = getClass().getResourceAsStream("/images/default.jpg");
                        if (defaultIs != null) {
                            imageView.setImage(new Image(defaultIs));
                        }
                    }

                    imageView.setFitWidth(200);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(true);

                    Label infoLabel = new Label(
                        artwork.getName() + "\n" +
                        artwork.getArtist() + ", " + 
                        artwork.getYear() + "\n" +
                        artwork.getStyle()
                    );
                    infoLabel.setFont(Font.font(ArtGallery.FONT));
                    infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: "+ArtGallery.DARK_COLOR);
                    infoLabel.setWrapText(true);

                    artworkBox.getChildren().addAll(imageView, infoLabel);
                    resultGrid.add(artworkBox, 0, row++);
                }
            }
        });

        layout.getChildren().addAll(
            new Label("Поиск по критериям:"),
            artistField,
            nameField,
            yearField,
            searchButton,
            scrollPane
        );

        layout.setStyle("-fx-background-color: "+ArtGallery.BEIGE_COLOR);
        Scene scene = new Scene(layout, 400, 500);
        searchStage.setScene(scene);
        searchStage.show();
    }
}

