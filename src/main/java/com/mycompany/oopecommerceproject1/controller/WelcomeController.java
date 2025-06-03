// src/main/java/com/mycompany/oopecommerceproject1/controller/WelcomeController.java
package com.mycompany.oopecommerceproject1.controller;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomeController {

    /**
     * “View Products” butonuna tıklanınca çağrılır.
     */
    @FXML
    private void handleViewProductsAction(ActionEvent event) {
        try {
            // Aşağıdaki path, src/main/resources içerisinde ProductList.fxml’in tam yolunu göstermeli:
            URL fxmlUrl = getClass().getResource(
                "/com/mycompany/oopecommerceproject1/view/ProductList.fxml"
            );
            if (fxmlUrl == null) {
                throw new IOException("ProductList.fxml not found on classpath");
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = new Stage();
            stage.setTitle("Product List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not open Product List screen.");
        }
    }

    // Eğer ileride “View Cart” gibi başka butonlarınız olacaksa, onlar için de benzer @FXML handler’lar ekleyebilirsiniz.
}
