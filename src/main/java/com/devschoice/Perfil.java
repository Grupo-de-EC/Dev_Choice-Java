package com.devchoice.devchoicejava;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Perfil {

    public void mostrarJanela() {
        // Cria um novo Stage (janela)
        Stage stage = new Stage();

        Label titulo = new Label("Alterar Dados");
        titulo.setFont(new Font("Arial", 24));
        titulo.setTextFill(Color.WHITE);

        Label nomeLabel = new Label("Nome");
        nomeLabel.setTextFill(Color.LIGHTGRAY);
        TextField nomeField = new TextField("Adm02");
        nomeField.setStyle("-fx-background-color: #2b2f3a; -fx-text-fill: white; -fx-border-radius: 5;");

        Label emailLabel = new Label("Email");
        emailLabel.setTextFill(Color.LIGHTGRAY);
        TextField emailField = new TextField("batatao@devschoice.com");
        emailField.setStyle("-fx-background-color: #2b2f3a; -fx-text-fill: white; -fx-border-radius: 5;");

        Button confirmarButton = new Button("Confirmar Troca");
        confirmarButton.setMaxWidth(Double.MAX_VALUE);
        confirmarButton.setStyle(
                "-fx-background-radius: 8;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to right, #4a90e2, #357ae8);" +
                        "-fx-text-fill: white;"
        );

        VBox vbox = new VBox(10, titulo, nomeLabel, nomeField, emailLabel, emailField, confirmarButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(25));
        vbox.setStyle("-fx-background-color: #1a1f2b; -fx-background-radius: 10;");
        vbox.setMaxWidth(350);

        StackPane root = new StackPane(vbox);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f1f4b, #1e3d8f);");

        Scene scene = new Scene(root, 800, 450);
        stage.setTitle("Perfil");
        stage.setScene(scene);
        stage.show();
    }
}
