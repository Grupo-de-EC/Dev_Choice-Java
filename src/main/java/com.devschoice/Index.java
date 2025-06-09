package com.devschoice;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class Index extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Título
        Label titulo = new Label("Dev's Choice");
        titulo.setFont(new Font("Arial", 20));
        titulo.setTextFill(Color.WHITE);

        Button sairBtn = new Button("Sair");
        sairBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: lightgray;");
        sairBtn.setOnAction(e -> primaryStage.close());

        HBox topBar = new HBox(20, titulo, new Region(), sairBtn);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: linear-gradient(to right, #0f1f4b, #1e3d8f);");
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Título da seção de kits
        Label sectionTitle = new Label("Kits mais utilizados");
        sectionTitle.setFont(new Font("Segoe UI", 20));
        sectionTitle.setStyle("-fx-font-weight: bold;");
        sectionTitle.setTextFill(Color.web("#1d4ed8"));

        // Lista de kits
        VBox kitList = new VBox(10);
        kitList.setPadding(new Insets(10));

        List<Kits.Kit> kits = ArquivoKits.carregarKits();

        if (kits.isEmpty()) {
            Label vazio = new Label("Nenhum kit cadastrado.");
            vazio.setFont(new Font("Segoe UI", 14));
            vazio.setTextFill(Color.GRAY);
            kitList.getChildren().add(vazio);
        } else {
            for (Kits.Kit kit : kits) {
                Label kitLabel = new Label(kit.getNome());
                kitLabel.setStyle(
                        "-fx-background-color: #f1f5f9;" +
                                "-fx-padding: 10;" +
                                "-fx-border-color: #1d4ed8;" +
                                "-fx-border-width: 0 0 0 5px;" +
                                "-fx-background-radius: 5;"
                );
                kitLabel.setMaxWidth(Double.MAX_VALUE);
                kitLabel.setFont(new Font("Segoe UI", 14));
                kitList.getChildren().add(kitLabel);
            }
        }

        VBox centralPanel = new VBox(15, sectionTitle, kitList);
        centralPanel.setAlignment(Pos.TOP_CENTER);
        centralPanel.setPadding(new Insets(30));
        centralPanel.setMaxWidth(600);
        centralPanel.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 4);"
        );

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centralPanel);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3a8a, #1d4ed8);");

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Dev's Choice - Kits");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
