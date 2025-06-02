package com.devschoice;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class formulario {

    private VBox formArea;

    public void mostrar(Stage stage) {
        stage.setTitle("DevChoice - Editor de Formulário");

        BorderPane root = new BorderPane();

        // Painel lateral com botões
        VBox controles = new VBox(10);
        controles.setPadding(new Insets(10));

        Button adicionarCampoTexto = new Button("Adicionar Campo de Texto");
        Button adicionarCaixaSelecao = new Button("Adicionar Caixa de Seleção");
        Button adicionarListaSuspensa = new Button("Adicionar Lista Suspensa");
        Button limparFormulario = new Button("Limpar Formulário");

        controles.getChildren().addAll(adicionarCampoTexto, adicionarCaixaSelecao, adicionarListaSuspensa, limparFormulario);

        // Área do formulário
        formArea = new VBox(10);
        formArea.setPadding(new Insets(10));
        formArea.setStyle("-fx-border-color: gray; -fx-border-width: 2px; -fx-background-color: #f9f9f9;");

        ScrollPane scrollPane = new ScrollPane(formArea);
        scrollPane.setFitToWidth(true);

        // Eventos dos botões
        adicionarCampoTexto.setOnAction(e -> formArea.getChildren().add(new TextField("Campo de texto")));
        adicionarCaixaSelecao.setOnAction(e -> formArea.getChildren().add(new CheckBox("Opção")));
        adicionarListaSuspensa.setOnAction(e -> {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll("Opção 1", "Opção 2", "Opção 3");
            comboBox.setPromptText("Selecione uma opção");
            formArea.getChildren().add(comboBox);
        });
        limparFormulario.setOnAction(e -> formArea.getChildren().clear());

        root.setLeft(controles);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}

