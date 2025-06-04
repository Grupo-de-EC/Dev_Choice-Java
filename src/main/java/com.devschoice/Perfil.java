package com.devschoice;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Perfil {
    private String nome;
    private String email;

    public Perfil() {
        carregarDados();
    }

    private void carregarDados() {
        try {
            List<String> linhas = Files.readAllLines(Paths.get("perfil.txt"));
            if (linhas.size() >= 2) {
                nome = linhas.get(0);
                email = linhas.get(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarDados() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("perfil.txt"))) {
            writer.write(nome);
            writer.newLine();
            writer.write(email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrarJanela() {
        Stage stage = new Stage();

        // Título
        Label titulo = new Label("Alterar Dados");
        titulo.setFont(new Font("Arial", 24));
        titulo.setTextFill(Color.WHITE);
        titulo.setPadding(new Insets(0, 0, 10, 0));

        // Nome
        Label nomeLabel = new Label("Nome");
        nomeLabel.setTextFill(Color.LIGHTGRAY);
        TextField nomeField = new TextField(nome);
        nomeField.setStyle(
                "-fx-background-color: #2b2f3a; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );
        nomeField.setMaxWidth(Double.MAX_VALUE);

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.setTextFill(Color.LIGHTGRAY);
        TextField emailField = new TextField(email);
        emailField.setStyle(
                "-fx-background-color: #2b2f3a; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );
        emailField.setMaxWidth(Double.MAX_VALUE);

        // Botão Confirmar
        Button confirmarButton = new Button("Confirmar Troca");
        confirmarButton.setStyle(
                "-fx-background-radius: 8;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to right, #4a90e2, #357ae8);" +
                        "-fx-text-fill: white;"
        );

        // Painel escuro central
        VBox painelEscuro = new VBox(12, titulo, nomeLabel, nomeField, emailLabel, emailField, confirmarButton);
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
        stage.setTitle("Alterar Perfil");
        stage.setScene(scene);

        // Ação do botão Confirmar
        confirmarButton.setOnAction(e -> {
            nome = nomeField.getText();
            email = emailField.getText();
            salvarDados();
            stage.close();
        });

        stage.show();
    }
}
