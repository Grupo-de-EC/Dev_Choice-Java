package com.devschoice;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Formulario {
    private List<String> campos;
    private VBox formArea;

    public Formulario() {
        campos = new ArrayList<>();
    }

    public void mostrar(Stage stage) {
        stage.setTitle("DevChoice - Editor de Formulário");

        BorderPane root = new BorderPane();

        VBox controles = new VBox(10);
        controles.setPadding(new Insets(20));
        controles.setStyle("-fx-background-color: rgba(15,23,42,0.9); "
                + "-fx-border-radius: 16px; -fx-background-radius: 16px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        Button adicionarCampoTexto = criarBotao("Adicionar Campo de Texto");
        Button adicionarCaixaSelecao = criarBotao("Adicionar Caixa de Seleção");
        Button adicionarListaSuspensa = criarBotao("Adicionar Lista Suspensa");
        Button limparFormulario = criarBotao("Limpar Formulário");
        Button salvarFormulario = criarBotao("Salvar");

        controles.getChildren().addAll(adicionarCampoTexto, adicionarCaixaSelecao, adicionarListaSuspensa, limparFormulario, salvarFormulario);

        formArea = new VBox(10);
        formArea.setPadding(new Insets(20));
        formArea.setStyle("-fx-border-color: #334155; -fx-border-width: 2px; "
                + "-fx-background-color: rgba(15,23,42,0.9); "
                + "-fx-border-radius: 16px; -fx-background-radius: 16px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        ScrollPane scrollPane = new ScrollPane(formArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 10;");

        // Adiciona questões fixas
        adicionarQuestaoFixa("Nome do seu projeto:");
        adicionarQuestaoFixa("Nivel de Experiencia:");
        adicionarQuestaoFixa("Tipo do Projeto:");

        adicionarCampoTexto.setOnAction(e -> {
            String titulo = solicitarTitulo("Campo de Texto");
            if (titulo != null && !titulo.trim().isEmpty()) {
                Label label = new Label(titulo);
                label.setStyle(estiloTituloPergunta());
                TextField campoTexto = new TextField();
                campoTexto.setPromptText("Digite aqui...");
                campoTexto.setStyle(estiloCampoInput());
                formArea.getChildren().addAll(label, campoTexto);
            }
        });

        adicionarCaixaSelecao.setOnAction(e -> {
            String titulo = solicitarTitulo("Caixa de Seleção");
            if (titulo != null && !titulo.trim().isEmpty()) {
                Label label = new Label(titulo);
                label.setStyle(estiloTituloPergunta());
                CheckBox checkBox = new CheckBox("Opção");
                checkBox.setStyle(estiloCheckBox());
                formArea.getChildren().addAll(label, checkBox);
            }
        });

        adicionarListaSuspensa.setOnAction(e -> {
            String titulo = solicitarTitulo("Lista Suspensa");
            if (titulo != null && !titulo.trim().isEmpty()) {
                Label label = new Label(titulo);
                label.setStyle(estiloTituloPergunta());
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.getItems().addAll("Opção 1", "Opção 2", "Opção 3");
                comboBox.setPromptText("Selecione uma opção");
                comboBox.setStyle(estiloComboBox());
                comboBox.setCellFactory(listView -> new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item);
                        setStyle("-fx-background-color: #1e293b; -fx-text-fill: #e2e8f0;");
                    }
                });
                comboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? comboBox.getPromptText() : item);
                        setStyle("-fx-text-fill: #e2e8f0; -fx-background-color: #1e293b;");
                    }
                });
                formArea.getChildren().addAll(label, comboBox);
            }
        });

        salvarFormulario.setOnAction(e -> salvarDados());

        root.setLeft(controles);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private String solicitarTitulo(String tipoCampo) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Campo");
        dialog.setHeaderText("Digite o título da nova pergunta:");
        dialog.setContentText("Título para " + tipoCampo + ":");
        return dialog.showAndWait().orElse(null);
    }

    private void adicionarQuestaoFixa(String labelTexto) {
        Label label = new Label(labelTexto);
        label.setStyle(estiloTituloPergunta());
        TextField campo = new TextField();
        campo.setPromptText(labelTexto);
        campo.setStyle(estiloCampoInput());
        formArea.getChildren().addAll(label, campo);
    }

    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("formulario.dat"))) {
            List<String> dados = new ArrayList<>();

            for (javafx.scene.Node node : formArea.getChildren()) {
                if (node instanceof TextField textField) {
                    dados.add("Texto: " + textField.getText());
                } else if (node instanceof CheckBox checkBox) {
                    dados.add("CheckBox: " + checkBox.getText() + " (" + (checkBox.isSelected() ? "Selecionado" : "Não selecionado") + ")");
                } else if (node instanceof ComboBox comboBox) {
                    Object value = comboBox.getValue();
                    dados.add("ComboBox: " + (value != null ? value.toString() : "Nenhuma opção selecionada"));
                }
            }

            oos.writeObject(dados);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Formulário salvo com sucesso!", ButtonType.OK);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar o formulário.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private Button criarBotao(String texto) {
        Button botao = new Button(texto);
        botao.setPrefWidth(200);
        botao.setStyle("-fx-background-color: linear-gradient(to right, #3b82f6, #2563eb); "
                + "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; "
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
        return "-fx-text-fill: #e2e8f0; -fx-font-size: 14px;";
    }

    private String estiloComboBox() {
        return "-fx-background-color: #1e293b; "
                + "-fx-text-fill: #e2e8f0; "
                + "-fx-prompt-text-fill: #94a3b8; "
                + "-fx-padding: 8px; "
                + "-fx-font-size: 14px; "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
                + "-fx-border-color: transparent; "
                + "-fx-focus-color: #3b82f6; "
                + "-fx-faint-focus-color: transparent;";
    }

    private String estiloTituloPergunta() {
        return "-fx-text-fill: #e2e8f0; "
                + "-fx-font-size: 16px; "
                + "-fx-font-weight: bold; "
                + "-fx-padding: 4 0 2 0;";
    }
}
