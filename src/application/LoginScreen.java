package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginScreen {
    private final ArtGallery mainApp;
    private AuthService authService;

    public LoginScreen(ArtGallery mainApp) {
        this.mainApp = mainApp;
    }

    public void show() {
        Stage loginStage = new Stage();
        loginStage.setTitle("Вход в систему");

        VBox layout = new VBox(10);

        layout.setStyle(ArtGallery.MAIN_BG);
        layout.setPadding(new Insets(20));

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.setStyle("-fx-font-family: '" + ArtGallery.FONT + "'; " +
        	    "-fx-font-size: 14px;");
        roleComboBox.setStyle(ArtGallery.SCROLL_PANE_STYLE);
        roleComboBox.getItems().addAll("Пользователь", "Администратор");
        roleComboBox.setValue("Пользователь");

        TextField usernameField = new TextField();
        usernameField.setFont(Font.font(ArtGallery.FONT));
        usernameField.setPromptText("Логин");

        PasswordField passwordField = new PasswordField();
        passwordField.setFont(Font.font(ArtGallery.FONT));
        passwordField.setPromptText("Пароль");

        Button loginButton = new Button("Войти");
        loginButton.setFont(Font.font(ArtGallery.FONT));
        loginButton.setStyle(ArtGallery.BUTTON_STYLE);
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String role = roleComboBox.getValue();
            String username = usernameField.getText();
            String password = passwordField.getText();

            boolean isAuthenticated = false;
            authService = new AuthService();

            if (role.equals("Администратор")) {
                isAuthenticated = authService.authenticateAdmin(username, password);
                if (isAuthenticated) {
                    loginStage.close();
                    mainApp.startMainWindow(role.equals("Администратор"), -1);
                }
                
                else {
                    messageLabel.setText("Неправильный логин или пароль");
                }
            } 
            else {
                isAuthenticated = authService.authenticateUser(username, password);
                if (isAuthenticated) {
                	int userId = authService.fetchUserId(username);
                    loginStage.close();
                    mainApp.startMainWindow(role.equals("Администратор"), userId);
                }  
                else
                	messageLabel.setText("Неправильный логин или пароль");
            
            }
        });

        layout.getChildren().addAll(
            new Label("Роль:"), roleComboBox,
            new Label("Логин:"), usernameField,
            new Label("Пароль:"), passwordField,
            loginButton, messageLabel
        );

        Scene scene = new Scene(layout, 300, 300);
        loginStage.setScene(scene);
        loginStage.show();
    }

}