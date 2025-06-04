package com.devschoice;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;

public class Kits {

    public void mostrarJanela() {
        Stage stage = new Stage();

        // Título
        Label titulo = new Label("Alterar Dados");
        titulo.setFont(new Font("Arial", 24));
        titulo.setTextFill(Color.WHITE);
        titulo.setPadding(new Insets(0, 0, 10, 0));

        // Nome
        Label kitLabel = new Label("Nome");
        kitLabel.setTextFill(Color.LIGHTGRAY);
        TextField nomeField = new TextField("Java");
        nomeField.setStyle(
                "-fx-background-color: #2b2f3a; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );
        nomeField.setMaxWidth(Double.MAX_VALUE);

        // Botão Confirmar
        Button confirmarButton = new Button("Confirmar Mudança");
        confirmarButton.setStyle(
                "-fx-background-radius: 8;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to right, #4a90e2, #357ae8);" +
                        "-fx-text-fill: white;"
        );
        confirmarButton.setMaxWidth(Double.MAX_VALUE);

        // Painel escuro central
        VBox painelEscuro = new VBox(12, titulo, kitLabel, nomeField, confirmarButton);
        painelEscuro.setAlignment(Pos.CENTER);
        painelEscuro.setPadding(new Insets(30));
        painelEscuro.setStyle("-fx-background-color: #141927; -fx-background-radius: 15;");
        painelEscuro.setMaxWidth(400);

        // Wrapper para centralizar
        StackPane centerWrapper = new StackPane(painelEscuro);
        centerWrapper.setPadding(new Insets(40));
        centerWrapper.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f1f4b, #1e3d8f);");

        // Cena final
        Scene scene = new Scene(centerWrapper, 800, 600);
        stage.setTitle("Alterar Kit");
        stage.setScene(scene);
        stage.show();
    }
}
