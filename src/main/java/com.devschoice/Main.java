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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {

    private Label kit;  // Class-level label to update kit name text

    @Override
    public void start(Stage primaryStage) {
        // NavBar
        Label titulo = new Label("Painel do Admin");
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

        // Painel central branco
        VBox painelCentral = new VBox(25);
        painelCentral.setPadding(new Insets(30));
        painelCentral.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
        painelCentral.setMaxWidth(600);

        // Sessão 1 - Perfil
        Label perfilTitulo = new Label("Perfil do Administrador");
        perfilTitulo.setFont(new Font("Arial", 18));
        perfilTitulo.setTextFill(Color.web("#1e3d8f"));

        Label nome = new Label("Nome: Adm02");
        Label email = new Label("Email: batatao@devschoice.com");

        Button editarPerfil = new Button("Editar Perfil");
        editarPerfil.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        editarPerfil.setOnAction(e -> {
            Perfil perfil = new Perfil();
            perfil.mostrarJanela();
        });

        VBox sessaoPerfil = new VBox(5, perfilTitulo, nome, email, editarPerfil);
        sessaoPerfil.setPadding(new Insets(0, 0, 10, 0));

        // Sessão 2 - Questionários
        Label questTitulo = new Label("Gerenciar Questionários");
        questTitulo.setFont(new Font("Arial", 18));
        questTitulo.setTextFill(Color.web("#1e3d8f"));

        Button editarQuestoes = new Button("Editar Questionários");
        editarQuestoes.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        editarQuestoes.setOnAction(e -> {
            Formulario formulario = new Formulario();
            formulario.mostrar(new Stage());
        });

        VBox sessaoQuestoes = new VBox(5, questTitulo, editarQuestoes);
        sessaoQuestoes.setPadding(new Insets(10, 0, 10, 0));

        // Sessão 3 - Kits
        Label kitsTitulo = new Label("Gerenciar Kits");
        kitsTitulo.setFont(new Font("Arial", 18));
        kitsTitulo.setTextFill(Color.web("#1e3d8f"));

        // Read the last line of Kits.txt to display on label
        String kitContent = "Nenhum kit cadastrado";  // default if no file or empty
        try {
            if (Files.exists(Paths.get("Kits.txt"))) {
                List<String> lines = Files.readAllLines(Paths.get("Kits.txt"));
                if (!lines.isEmpty()) {
                    kitContent = lines.get(lines.size() - 1).trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        kit = new Label("Nome: " + kitContent);

        Button editarKits = new Button("Editar Kits");
        editarKits.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        editarKits.setOnAction(e -> {
            Kits kits = new Kits();

            // Setup listener to update label when Kits saves new content
            kits.setOnSaveListener(new Kits.SaveListener() {
                @Override
                public void onSave(String newContent) {
                    // Update label with the new saved line
                    kit.setText("Nome: " + newContent.trim());
                }
            });

            kits.mostrarJanela();
        });

        VBox sessaoKits = new VBox(5, kitsTitulo, kit, editarKits);
        sessaoKits.setPadding(new Insets(0, 0, 10, 0));

        // Add all sections to central white panel
        painelCentral.getChildren().addAll(
                sessaoPerfil,
                new Separator(),
                sessaoQuestoes,
                new Separator(),
                sessaoKits,
                new Separator()
        );

        // Center layout with padding and background gradient
        StackPane centerWrapper = new StackPane(painelCentral);
        centerWrapper.setPadding(new Insets(40));
        centerWrapper.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3d8f, #2c4f99);");

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(centerWrapper);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setTitle("Painel do Admin");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
