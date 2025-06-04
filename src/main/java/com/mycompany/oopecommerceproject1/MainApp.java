package com.mycompany.oopecommerceproject1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX main application class.
 * - In start(), loads Login.fxml and displays the scene
 * - The window title is “OOP E-Commerce - Login”
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/oopecommerceproject1/view/Login.fxml")
            );
            primaryStage.setTitle("OOP E-Commerce - Login");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
