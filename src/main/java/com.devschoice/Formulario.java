package com.devschoice;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Formulario {
    private VBox formArea;
    private boolean modoEdicaoAtivo = false;

    public Formulario() {}

    public void mostrar(Stage stage) {
        stage.setTitle("DevChoice - Editor de Formulário");

        BorderPane root = new BorderPane();

        VBox controles = new VBox(10);
        controles.setPadding(new Insets(20));
        controles.setStyle(
                "-fx-background-color: rgba(15, 23, 42, 0.9); " +
                        "-fx-border-radius: 16px; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);"
        );

        Button adicionarCampoTexto = criarBotao("Adicionar Campo de Texto");
        Button adicionarCaixaSelecao = criarBotao("Adicionar Caixa de Seleção");
        Button adicionarListaSuspensa = criarBotao("Adicionar Lista Suspensa");
        Button limparFormulario = criarBotao("Limpar Formulário");
        Button salvarFormulario = criarBotao("Salvar");
        Button editarQuestao = criarBotao("Editar Questão");

        controles.getChildren().addAll(
                adicionarCampoTexto,
                adicionarCaixaSelecao,
                adicionarListaSuspensa,
                limparFormulario,
                salvarFormulario,
                editarQuestao
        );

        formArea = new VBox(10);
        formArea.setPadding(new Insets(20));
        formArea.setStyle(
                "-fx-border-color: #334155; " +
                        "-fx-border-width: 2px; " +
                        "-fx-background-color: rgba(15, 23, 42, 0.9); " +
                        "-fx-border-radius: 16px; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);"
        );

        ScrollPane scrollPane = new ScrollPane(formArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 10;");

        // Adiciona questões fixas com estilo antigo
        adicionarQuestaoFixaTexto("Nome do seu projeto:");
        adicionarQuestaoFixaCombo("Nivel de Experiencia:", new String[]{"Iniciante", "Intermediário", "Avançado"});
        adicionarQuestaoFixaCombo("Tipo do Projeto:", new String[]{"web", "mobile", "desktop", "iot", "jogo", "analise", "outros"});

        adicionarCampoTexto.setOnAction(e -> {
            String titulo = solicitarTitulo("Campo de Texto");
            if (titulo != null && !titulo.trim().isEmpty()) {
                adicionarPergunta(titulo, "texto", null);
            }
        });

        adicionarCaixaSelecao.setOnAction(e -> {
            String titulo = solicitarTitulo("Caixa de Seleção");
            if (titulo != null && !titulo.trim().isEmpty()) {
                adicionarPergunta(titulo, "checkbox", null);
            }
        });

        adicionarListaSuspensa.setOnAction(e -> {
            String titulo = solicitarTitulo("Lista Suspensa");
            if (titulo != null && !titulo.trim().isEmpty()) {
                adicionarPergunta(titulo, "combo", new String[]{"Opção 1", "Opção 2", "Opção 3"});
            }
        });

        limparFormulario.setOnAction(e -> formArea.getChildren().clear());

        salvarFormulario.setOnAction(e -> salvarDados());

        editarQuestao.setOnAction(e -> {
            modoEdicaoAtivo = !modoEdicaoAtivo;
            editarQuestao.setText(modoEdicaoAtivo ? "Sair do Modo Edição" : "Editar Questão");

            if (modoEdicaoAtivo) {
                ativarModoEdicao();
            } else {
                desativarModoEdicao();
            }
        });

        root.setLeft(controles);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void ativarModoEdicao() {
        for (javafx.scene.Node node : formArea.getChildren()) {
            if (node instanceof Label label) {
                label.setStyle(estiloTituloPergunta() + "-fx-border-color: yellow; -fx-border-width: 1px; -fx-cursor: hand;");
                label.setOnMouseClicked(event -> {
                    if (modoEdicaoAtivo) {
                        editarPergunta(label);
                    }
                });
            }
        }
    }

    private void desativarModoEdicao() {
        for (javafx.scene.Node node : formArea.getChildren()) {
            if (node instanceof Label label) {
                label.setStyle(estiloTituloPergunta());
                label.setOnMouseClicked(null);
            }
        }
    }

    private void editarPergunta(Label label) {
        int index = formArea.getChildren().indexOf(label);
        if (index == -1 || index + 1 >= formArea.getChildren().size()) return;

        javafx.scene.Node campo = formArea.getChildren().get(index + 1);

        // Diálogo para editar título
        TextInputDialog dialogTitulo = new TextInputDialog(label.getText());
        dialogTitulo.setTitle("Editar Título da Pergunta");
        dialogTitulo.setHeaderText("Edite o título da pergunta:");
        dialogTitulo.setContentText("Título:");
        Optional<String> novoTituloOpt = dialogTitulo.showAndWait();

        if (novoTituloOpt.isEmpty()) return;
        String novoTitulo = novoTituloOpt.get().trim();
        if (novoTitulo.isEmpty()) return;

        // Diálogo para escolher tipo do campo
        ChoiceDialog<String> dialogTipo = new ChoiceDialog<>(obterTipoCampo(campo),
                "texto", "checkbox", "combo");
        dialogTipo.setTitle("Editar Tipo do Campo");
        dialogTipo.setHeaderText("Selecione o novo tipo do campo:");
        dialogTipo.setContentText("Tipo:");

        Optional<String> novoTipoOpt = dialogTipo.showAndWait();
        if (novoTipoOpt.isEmpty()) return;
        String novoTipo = novoTipoOpt.get();

        label.setText(novoTitulo);

        formArea.getChildren().remove(campo);

        switch (novoTipo) {
            case "texto" -> {
                TextField novoCampo = new TextField();
                novoCampo.setPromptText("Digite aqui...");
                novoCampo.setStyle(estiloCampoInput());
                formArea.getChildren().add(index + 1, novoCampo);
            }
            case "checkbox" -> {
                CheckBox novoCampo = new CheckBox("Opção");
                novoCampo.setStyle(estiloCheckBox());
                formArea.getChildren().add(index + 1, novoCampo);
            }
            case "combo" -> {
                ComboBox<String> novoCampo = new ComboBox<>();
                novoCampo.getItems().addAll("Opção 1", "Opção 2", "Opção 3");
                novoCampo.setPromptText("Selecione uma opção");
                novoCampo.setStyle(estiloComboBox());
                formArea.getChildren().add(index + 1, novoCampo);
            }
            default -> {
                formArea.getChildren().add(index + 1, campo);
            }
        }
    }

    private String obterTipoCampo(javafx.scene.Node campo) {
        if (campo instanceof TextField) return "texto";
        if (campo instanceof CheckBox) return "checkbox";
        if (campo instanceof ComboBox) return "combo";
        return "texto";
    }

    private void adicionarPergunta(String titulo, String tipo, String[] opcoes) {
        Label label = new Label(titulo);
        label.setStyle(estiloTituloPergunta());

        switch (tipo) {
            case "texto" -> {
                TextField campoTexto = new TextField();
                campoTexto.setPromptText("Digite aqui...");
                campoTexto.setStyle(estiloCampoInput());
                formArea.getChildren().addAll(label, campoTexto);
            }
            case "checkbox" -> {
                CheckBox checkBox = new CheckBox("Opção");
                checkBox.setStyle(estiloCheckBox());
                formArea.getChildren().addAll(label, checkBox);
            }
            case "combo" -> {
                ComboBox<String> comboBox = new ComboBox<>();
                if (opcoes != null) {
                    comboBox.getItems().addAll(opcoes);
                } else {
                    comboBox.getItems().addAll("Opção 1", "Opção 2", "Opção 3");
                }
                comboBox.setPromptText("Selecione uma opção");
                comboBox.setStyle(estiloComboBox());
                formArea.getChildren().addAll(label, comboBox);
            }
        }
    }

    private String solicitarTitulo(String tipoCampo) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Campo");
        dialog.setHeaderText("Digite o título da nova pergunta:");
        dialog.setContentText("Título para " + tipoCampo + ":");
        return dialog.showAndWait().orElse(null);
    }

    private void adicionarQuestaoFixaTexto(String labelTexto) {
        Label label = new Label(labelTexto);
        label.setStyle(estiloTituloPergunta());
        TextField campo = new TextField();
        campo.setPromptText(labelTexto);
        campo.setStyle(estiloCampoInput());
        formArea.getChildren().addAll(label, campo);
    }

    private void adicionarQuestaoFixaCombo(String labelTexto, String[] opcoes) {
        Label label = new Label(labelTexto);
        label.setStyle(estiloTituloPergunta());

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(opcoes);
        comboBox.setPromptText("Selecione uma opção");
        comboBox.setStyle(estiloComboBox());
        formArea.getChildren().addAll(label, comboBox);
    }

    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("formulario.dat"))) {
            List<String> dados = new ArrayList<>();

            for (javafx.scene.Node node : formArea.getChildren()) {
                if (node instanceof TextField textField) {
                    dados.add("Texto: " + textField.getText());
                } else if (node instanceof CheckBox checkBox) {
                    dados.add("Checkbox: " + checkBox.isSelected());
                } else if (node instanceof ComboBox<?> comboBox) {
                    Object selecionado = comboBox.getSelectionModel().getSelectedItem();
                    dados.add("Combo: " + (selecionado == null ? "" : selecionado.toString()));
                }
            }
            oos.writeObject(dados);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Formulário salvo com sucesso!", ButtonType.OK);
            alert.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar formulário: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private Button criarBotao(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: #2563eb; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 12; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8;"
        );
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private String estiloTituloPergunta() {
        return "-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #e0e0e0; -fx-padding: 6 4 6 4;";
    }

    private String estiloCampoInput() {
        return "-fx-background-color: #0f172a; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #ff0000; -fx-padding: 6;";
    }

    private String estiloCheckBox() {
        return "-fx-text-fill: #e0e0e0;";
    }

    private String estiloComboBox() {
        return "-fx-background-color: linear-gradient(to bottom, #3b82f6, #2563eb);" + // azul vibrante degradê
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: #f0f9ff;" +        // texto quase branco, suave
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-padding: 6 15 6 15;" +
                "-fx-border-color: #1e40af;" +
                "-fx-border-width: 2;" +
                "-fx-effect: dropshadow(gaussian, rgba(37, 99, 235, 0.6), 8, 0, 0, 2);" + // sombra azul
                "-fx-cursor: hand;" +
                "-fx-transition: all 0.3s ease;" +
                "-fx-focus-color: #60a5fa;" +
                "-fx-faint-focus-color: transparent;";
    }
}
