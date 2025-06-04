// Pacote onde está localizada a classe
package com.devschoice;

// Importações necessárias do JavaFX e bibliotecas de arquivos
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Classe que representa o editor de formulário
public class Formulario {
    // Lista para armazenar os campos adicionados no formulário
    private List<String> campos;

    // Área visual onde os campos do formulário serão exibidos
    private VBox formArea;

    // Construtor da classe: inicializa a lista e carrega dados salvos
    public Formulario() {
        campos = new ArrayList<>();
        carregarDados();
    }

    // Método para carregar os dados do formulário salvos em arquivo
    private void carregarDados() {
        try {
            campos = Files.readAllLines(Paths.get("formulario.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para salvar os dados atuais do formulário no arquivo
    private void salvarDados() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("formulario.txt"))) {
            for (String campo : campos) {
                writer.write(campo);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método principal que monta a interface do editor de formulário
    public void mostrar(Stage stage) {
        stage.setTitle("DevChoice - Editor de Formulário");

        // Layout principal da janela
        BorderPane root = new BorderPane();

        // Caixa lateral com os botões de controle
        VBox controles = new VBox(10);
        controles.setPadding(new Insets(20));
        controles.setStyle("-fx-background-color: rgba(15,23,42,0.9); "
                + "-fx-border-radius: 16px; "
                + "-fx-background-radius: 16px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        // Botões de adicionar campos ao formulário
        Button adicionarCampoTexto = criarBotao("Adicionar Campo de Texto");
        Button adicionarCaixaSelecao = criarBotao("Adicionar Caixa de Seleção");
        Button adicionarListaSuspensa = criarBotao("Adicionar Lista Suspensa");
        Button limparFormulario = criarBotao("Limpar Formulário");

        // Adiciona os botões à caixa lateral
        controles.getChildren().addAll(adicionarCampoTexto, adicionarCaixaSelecao, adicionarListaSuspensa, limparFormulario);

        // Área principal onde os campos serão inseridos
        formArea = new VBox(10);
        formArea.setPadding(new Insets(20));
        formArea.setStyle("-fx-border-color: #334155; -fx-border-width: 2px; "
                + "-fx-background-color: rgba(15,23,42,0.9); "
                + "-fx-border-radius: 16px; -fx-background-radius: 16px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        // Área de rolagem para o formulário (caso tenha muitos campos)
        ScrollPane scrollPane = new ScrollPane(formArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 10;");

        // Evento para adicionar campo de texto
        adicionarCampoTexto.setOnAction(e -> {
            TextField campoTexto = new TextField();
            campoTexto.setPromptText("Digite aqui...");
            campoTexto.setStyle(estiloCampoInput());
            formArea.getChildren().add(campoTexto);
            campos.add(campoTexto.getText()); // Adiciona o conteúdo à lista
        });

        // Evento para adicionar caixa de seleção (CheckBox)
        adicionarCaixaSelecao.setOnAction(e -> {
            CheckBox checkBox = new CheckBox("Opção");
            checkBox.setStyle(estiloCheckBox());
            formArea.getChildren().add(checkBox);
            campos.add(checkBox.getText()); // Adiciona o conteúdo à lista
        });

        // Evento para adicionar lista suspensa (ComboBox)
        adicionarListaSuspensa.setOnAction(e -> {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll("Opção 1", "Opção 2", "Opção 3");
            comboBox.setPromptText("Selecione uma opção");

            // Estiliza o ComboBox principal
            comboBox.setStyle("-fx-background-color: #1e293b; "
                    + "-fx-text-fill: #e2e8f0; "
                    + "-fx-prompt-text-fill: #94a3b8; "
                    + "-fx-padding: 8px; "
                    + "-fx-font-size: 14px; "
                    + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
                    + "-fx-border-color: transparent; "
                    + "-fx-focus-color: #3b82f6; "
                    + "-fx-faint-focus-color: transparent;");

            // Estilo dos itens da lista suspensa
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
                }
            });

            // Estilo da célula selecionada
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
            campos.add(comboBox.getPromptText()); // Adiciona à lista
        });

        // Evento para limpar o formulário
        limparFormulario.setOnAction(e -> {
            formArea.getChildren().clear();
            campos.clear(); // Limpa a lista de campos
        });

        // Define o layout principal da tela
        root.setLeft(controles);
        root.setCenter(scrollPane);

        // Cria a cena e define no palco
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);

        // Salva os dados quando a janela for fechada
        stage.setOnCloseRequest(e -> salvarDados());

        // Exibe a janela
        stage.show();
    }

    // Método auxiliar para criar botões com estilo personalizado
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

        // Efeito ao passar o mouse
        botao.setOnMouseEntered(e -> botao.setStyle("-fx-background-color: linear-gradient(to right, #2563eb, #1e40af); "
                + "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 3);"));

        // Efeito ao remover o mouse
        botao.setOnMouseExited(e -> botao.setStyle("-fx-background-color: linear-gradient(to right, #3b82f6, #2563eb); "
                + "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"));

        return botao;
    }

    // Método auxiliar que retorna o estilo CSS para campos de texto
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

    // Método auxiliar que retorna o estilo CSS para caixas de seleção (CheckBox)
    private String estiloCheckBox() {
        return "-fx-text-fill: #e2e8f0; "
                + "-fx-font-size: 14px;";
    }
}
