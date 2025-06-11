package com.devschoice;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class Respostas extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f4f6f9;");

        Label titulo = new Label("Respostas do Questionário");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox respostasBox = new VBox(10);
        respostasBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        List<ArquivoQuestionario> respostas = carregarRespostas();
        if (respostas.isEmpty()) {
            Label nenhuma = new Label("Nenhuma resposta encontrada.");
            nenhuma.setStyle("-fx-text-fill: #555555;");
            respostasBox.getChildren().add(nenhuma);
        } else {
            for (ArquivoQuestionario resposta : respostas) {
                Label perguntaLabel = new Label("• " + resposta.getPergunta());
                perguntaLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e3d8f;");
                Label respostaLabel = new Label(resposta.getResposta());
                respostaLabel.setWrapText(true);
                respostasBox.getChildren().addAll(perguntaLabel, respostaLabel);
            }
        }

        root.getChildren().addAll(titulo, respostasBox);

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Respostas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<ArquivoQuestionario> carregarRespostas() {
        List<ArquivoQuestionario> respostas = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("respostas.dat"))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                for (Object o : list) {
                    if (o instanceof ArquivoQuestionario aq) {
                        respostas.add(aq);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar respostas: " + e.getMessage());
        }
        return respostas;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
