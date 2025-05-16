package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AdminPanel {

	private final List<Exhibition> exhibitions;
    private final ArtGallery mainApp;
    private final boolean skipLogin;


    public AdminPanel(List<Exhibition> exhibitions, ArtGallery mainApp, boolean skipLogin) {
        this.exhibitions = exhibitions;
        this.mainApp = mainApp;
        this.skipLogin = skipLogin;
    }
    
    // Метод отображения окна администратора
    public void show() {
    	if (skipLogin) {
            // без запроса логина
            Stage stage = new Stage();
            showAdminControls(stage);
        } 
    }
    
    // Утилита для отображения уведомлений
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Метод для сохранения изменений
    private void saveChanges(Exhibition exhibition, TextField nameField, TextArea descriptionField) {
        String sql = "UPDATE exhibition SET exhibition_name = ?, description = ? WHERE exhibition_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nameField.getText());
            stmt.setString(2, descriptionField.getText());
            stmt.setInt(3, exhibition.getId());
            stmt.executeUpdate();

            exhibition.setName(nameField.getText());
            exhibition.setDescription(descriptionField.getText());
            showAlert("Изменения сохранены!", Alert.AlertType.INFORMATION);
            
            mainApp.refreshMainView(); // Перезагрузить данные
        } catch (SQLException e) {
            showAlert("Ошибка базы данных: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Метод редактирования выставок
    private void showAdminControls(Stage adminStage) {
    	adminStage.setTitle("Редактирование выставок");
        VBox adminLayout = new VBox(10);
        adminLayout.setStyle("-fx-background-color: "+ArtGallery.BEIGE_COLOR);
        adminLayout.setPadding(new Insets(10));
        adminLayout.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(ArtGallery.SCROLL_PANE_STYLE);

        VBox exhibitionList = new VBox(10);
        exhibitionList.setPadding(new Insets(10));

        for (Exhibition exhibition : exhibitions) {
            VBox exhibitionBox = new VBox(5);
            exhibitionBox.setPadding(new Insets(10));
            exhibitionBox.setStyle("-fx-border-color: "+ArtGallery.ACCENT_COLOR+ "-fx-padding: 10; -fx-border-width: 1;");
            exhibitionBox.setAlignment(Pos.CENTER_LEFT);

            TextField nameField = new TextField(exhibition.getName());
            nameField.setPromptText("Название выставки");

            TextArea descriptionField = new TextArea(exhibition.getDescription());
            descriptionField.setPromptText("Описание выставки");
            descriptionField.setWrapText(true);

            Button saveButton = new Button("Сохранить");
            saveButton.setFont(Font.font(ArtGallery.FONT));
            saveButton.setOnAction(e -> saveChanges(exhibition, nameField, descriptionField));

            exhibitionBox.getChildren().addAll(
                    new Label("Название:"),
                    nameField,
                    new Label("Описание:"),
                    descriptionField,
                    saveButton
            );

            exhibitionList.getChildren().add(exhibitionBox);
        }

        scrollPane.setContent(exhibitionList);
        adminLayout.getChildren().addAll(new Label("Редактирование выставок"), scrollPane);
        adminLayout.setStyle("-fx-color: "+ArtGallery.ACCENT_COLOR);
        adminStage.setScene(new Scene(adminLayout, 400, 500));
        adminStage.show();
    }
}
