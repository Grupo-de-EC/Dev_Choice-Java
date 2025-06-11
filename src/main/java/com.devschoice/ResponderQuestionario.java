package com.devschoice;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResponderQuestionario extends Application {
    private List<TextField> respostas = new ArrayList<>();
    private List<Pergunta> perguntas;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle(
                "-fx-background-color: #f0f2f5;" +
                        "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;"
        );

        // Caixa central com sombra e fundo branco
        VBox container = new VBox(20);
        container.setPadding(new Insets(25));
        container.setMaxWidth(450);
        container.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.5, 0, 2);"
        );

        Label titulo = new Label("Questionário");
        titulo.setStyle(
                "-fx-font-size: 28px; " +
                        "-fx-font-weight: 700; " +
                        "-fx-text-fill: #222;"
        );

        perguntas = carregarPerguntas();

        VBox perguntasBox = new VBox(15);

        for (Pergunta pergunta : perguntas) {
            Label label = new Label(pergunta.getTitulo());
            label.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-text-fill: #444;" +
                            "-fx-font-weight: 600;"
            );

            TextField respostaField = new TextField();
            respostaField.setPrefWidth(400);
            respostaField.setStyle(
                    "-fx-background-color: #fff;" +
                            "-fx-border-color: #ccc;" +
                            "-fx-border-radius: 6;" +
                            "-fx-padding: 8 12 8 12;" +
                            "-fx-font-size: 14px;"
            );

            // Efeito quando o campo estiver focado
            respostaField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    respostaField.setStyle(
                            "-fx-background-color: #fff;" +
                                    "-fx-border-color: #357ae8;" +  // azul substituindo verde
                                    "-fx-border-radius: 6;" +
                                    "-fx-padding: 8 12 8 12;" +
                                    "-fx-font-size: 14px;"
                    );
                } else {
                    respostaField.setStyle(
                            "-fx-background-color: #fff;" +
                                    "-fx-border-color: #ccc;" +
                                    "-fx-border-radius: 6;" +
                                    "-fx-padding: 8 12 8 12;" +
                                    "-fx-font-size: 14px;"
                    );
                }
            });

            respostas.add(respostaField);
            perguntasBox.getChildren().addAll(label, respostaField);
        }

        Button enviarButton = new Button("Enviar");
        enviarButton.setPrefWidth(150);
        enviarButton.setStyle(
                "-fx-background-color: #357ae8;" + // azul substituindo verde
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 12 0 12 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );

        // Efeito hover no botão
        enviarButton.setOnMouseEntered(e -> enviarButton.setStyle(
                "-fx-background-color: #2a62c4;" +  // tom mais escuro do azul para hover
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 12 0 12 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        ));
        enviarButton.setOnMouseExited(e -> enviarButton.setStyle(
                "-fx-background-color: #357ae8;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 12 0 12 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        ));

        enviarButton.setOnAction(e -> {
            salvarRespostas();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Respostas enviadas com sucesso!");
            alert.showAndWait();
        });

        container.getChildren().addAll(titulo, perguntasBox, enviarButton);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 520, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Responder Questionário");
        primaryStage.show();
    }

    private List<Pergunta> carregarPerguntas() {
        List<Pergunta> perguntas = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("questionario.dat"))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                perguntas = (List<Pergunta>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return perguntas;
    }

    private void salvarRespostas() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("respostas.txt", true))) {
            for (int i = 0; i < respostas.size(); i++) {
                String perguntaTexto = (i < perguntas.size()) ? perguntas.get(i).getTitulo() : "Pergunta " + (i + 1);
                writer.write(perguntaTexto + ": " + respostas.get(i).getText());
                writer.newLine();
            }
            writer.write("------");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
