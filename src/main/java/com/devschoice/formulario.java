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
        controles.setPadding(new Insets(20));
        controles.setStyle("-fx-background-color: rgba(15,23,42,0.9); "
                + "-fx-border-radius: 16px; "
                + "-fx-background-radius: 16px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        Button adicionarCampoTexto = criarBotao("Adicionar Campo de Texto");
        Button adicionarCaixaSelecao = criarBotao("Adicionar Caixa de Seleção");
        Button adicionarListaSuspensa = criarBotao("Adicionar Lista Suspensa");
        Button limparFormulario = criarBotao("Limpar Formulário");

        controles.getChildren().addAll(adicionarCampoTexto, adicionarCaixaSelecao, adicionarListaSuspensa, limparFormulario);

        // Área do formulário
        formArea = new VBox(10);
        formArea.setPadding(new Insets(20));
        formArea.setStyle("-fx-border-color: #334155; -fx-border-width: 2px; "
                + "-fx-background-color: rgba(15,23,42,0.9); "
                + "-fx-border-radius: 16px; -fx-background-radius: 16px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        ScrollPane scrollPane = new ScrollPane(formArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 10;");

        // Eventos dos botões
        adicionarCampoTexto.setOnAction(e -> {
            TextField campoTexto = new TextField();
            campoTexto.setPromptText("Digite aqui...");
            campoTexto.setStyle(estiloCampoInput());
            formArea.getChildren().add(campoTexto);
        });

        adicionarCaixaSelecao.setOnAction(e -> {
            CheckBox checkBox = new CheckBox("Opção");
            checkBox.setStyle(estiloCheckBox());
            formArea.getChildren().add(checkBox);
        });

        adicionarListaSuspensa.setOnAction(e -> {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll("Opção 1", "Opção 2", "Opção 3");
            comboBox.setPromptText("Selecione uma opção");

            // Estilo geral do ComboBox (caixa principal)
            comboBox.setStyle("-fx-background-color: #1e293b; "
                    + "-fx-text-fill: #e2e8f0; "
                    + "-fx-prompt-text-fill: #94a3b8; "
                    + "-fx-padding: 8px; "
                    + "-fx-font-size: 14px; "
                    + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
                    + "-fx-border-color: transparent; "
                    + "-fx-focus-color: #3b82f6; "
                    + "-fx-faint-focus-color: transparent;");

            // Personalizar a lista suspensa (itens)
            comboBox.setCellFactory(listView -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: #1e293b; -fx-text-fill: #e2e8f0;");
                    }

                    // Efeito ao passar o mouse (hover)
                    listView.setOnMouseMoved(ev -> {
                        if (listView.getSelectionModel().getSelectedItem() != null) {
                            setStyle("-fx-background-color: #334155; -fx-text-fill: #ffffff;");
                        }
                    });
                }
            });

            // Estilo da célula que mostra a seleção
            comboBox.setButtonCell(new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(comboBox.getPromptText());
                        setStyle("-fx-text-fill: #94a3b8; -fx-background-color: #1e293b;");
                    } else {
                        setText(item);
                        setStyle("-fx-text-fill: #e2e8f0; -fx-background-color: #1e293b;");
                    }
                }
            });

            formArea.getChildren().add(comboBox);
        });

        limparFormulario.setOnAction(e -> formArea.getChildren().clear());

        root.setLeft(controles);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private Button criarBotao(String texto) {
        Button botao = new Button(texto);
        botao.setPrefWidth(200);
        botao.setStyle("-fx-background-color: linear-gradient(to right, #3b82f6, #2563eb); "
                + "-fx-text-fill: white; "
                + "-fx-font-weight: bold; "
                + "-fx-padding: 10px; "
                + "-fx-border-radius: 8px; "
                + "-fx-background-radius: 8px; "
                + "-fx-cursor: hand; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);");
        botao.setOnMouseEntered(e -> botao.setStyle("-fx-background-color: linear-gradient(to right, #2563eb, #1e40af); "
                + "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 3);"));
        botao.setOnMouseExited(e -> botao.setStyle("-fx-background-color: linear-gradient(to right, #3b82f6, #2563eb); "
                + "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"));
        return botao;
    }

    private String estiloCampoInput() {
        return "-fx-background-color: #1e293b; "
                + "-fx-text-fill: #e2e8f0; "
                + "-fx-prompt-text-fill: #94a3b8; "
                + "-fx-padding: 12px; "
                + "-fx-font-size: 14px; "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
                + "-fx-border-color: transparent; "
                + "-fx-focus-color: #3b82f6; "
                + "-fx-faint-focus-color: transparent;";
    }

    private String estiloCheckBox() {
        return "-fx-text-fill: #e2e8f0; "
                + "-fx-font-size: 14px;";
    }
}
