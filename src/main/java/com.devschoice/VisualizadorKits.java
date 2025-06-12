package com.devschoice;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class VisualizadorKits extends VBox {

    public VisualizadorKits(String perfilId) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label titulo = new Label("Kits - Visualização");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        getChildren().add(titulo);

        ComboBox<String> filtro = new ComboBox<>();
        filtro.getItems().add("Visualizar Todos");
        filtro.getItems().addAll(
                "Site ou sistema web",
                "Programa para desktop",
                "Jogo digital",
                "Aplicativo mobile",
                "Análise de dados / IA",
                "Projeto com hardware / IoT",
                "Outros"
        );
        filtro.setValue("Visualizar Todos");
        getChildren().add(filtro);

        ListView<String> lista = new ListView<>();
        lista.setPrefHeight(300);
        getChildren().add(lista);

        // Atualizar lista
        Runnable atualizar = () -> {
            List<Kits.Kit> kits = ArquivoKits.carregarKits(perfilId);
            String sel = filtro.getValue();
            ObservableList<String> items = lista.getItems();
            items.clear();
            for (Kits.Kit kit : kits) {
                if ("Visualizar Todos".equals(sel) || kit.getCategoria().equals(sel)) {
                    items.add(kit.getNome() + " (" + kit.getCategoria() + ")");
                }
            }
        };

        filtro.valueProperty().addListener((obs, o, n) -> atualizar.run());
        atualizar.run();
    }

    public static void mostrarJanela(String perfilId) {
        Stage stage = new Stage();
        VisualizadorKits view = new VisualizadorKits(perfilId);
        Scene scene = new Scene(view, 400, 500);
        stage.setTitle("Visualizar Kits - Perfil: " + perfilId);
        stage.setScene(scene);
        stage.show();
    }
}