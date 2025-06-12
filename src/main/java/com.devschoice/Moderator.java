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
import java.util.List;

public class Moderator extends Application {

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
        Label perfilTitulo = new Label("Perfis");
        perfilTitulo.setFont(new Font("Arial", 18));
        perfilTitulo.setTextFill(Color.web("#1e3d8f"));
        perfilTitulo.setStyle("-fx-font-weight: bold;");

        Button criarNovoPerfilBtn = new Button("Criar Novo Perfil");
        criarNovoPerfilBtn.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");

        Button modificarPerfilBtn = new Button("Modificar Perfil");
        modificarPerfilBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        ListView<Perfil> listaPerfis = new ListView<>();
        listaPerfis.setPrefWidth(300);

        Runnable atualizarListaPerfis = () -> {
            List<Perfil> perfis = ArquivoPerfil.lerPerfis();
            listaPerfis.getItems().setAll(perfis);
            if (!perfis.isEmpty()) {
                listaPerfis.getSelectionModel().selectFirst();
            }
        };

        modificarPerfilBtn.setOnAction(e -> {
            Perfil selecionado = listaPerfis.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um perfil para modificar.");
                alert.showAndWait();
                return;
            }
            // Abre a janela da classe Perfil para editar o perfil selecionado
            selecionado.mostrarJanela(atualizarListaPerfis);
        });

        criarNovoPerfilBtn.setOnAction(e -> Perfil.mostrarJanelaCriarNovoPerfil(atualizarListaPerfis));

        HBox botoesPerfil = new HBox(10, criarNovoPerfilBtn, modificarPerfilBtn);
        botoesPerfil.setAlignment(Pos.CENTER_LEFT);

        Button visualizarBtn = new Button("Visualizar Perfis");
        visualizarBtn.setStyle("-fx-background-color: #00796B; -fx-text-fill: white; -fx-font-weight: bold;");
        visualizarBtn.setOnAction(ev -> {
            List<Perfil> perfis = ArquivoPerfil.lerPerfis();
            perfis.sort((p1, p2) -> p1.getNome().compareToIgnoreCase(p2.getNome()));

            VBox listaBox = new VBox(10);
            listaBox.setPadding(new Insets(20));
            listaBox.setStyle("-fx-background-color: white;");

            for (Perfil perfil : perfis) {
                Label label = new Label("Nome: " + perfil.getNome() + " | Email: " + perfil.getEmail());
                label.setStyle("-fx-text-fill: black; -fx-font-size: 14;");
                listaBox.getChildren().add(label);
            }

            ScrollPane scroll = new ScrollPane(listaBox);
            scroll.setFitToWidth(true);

            Scene scene = new Scene(scroll, 400, 300);
            Stage stage = new Stage();
            stage.setTitle("Perfis Cadastrados");
            stage.setScene(scene);
            stage.show();
        });

        VBox painelEditarPerfis = new VBox(10, listaPerfis, visualizarBtn);
        painelEditarPerfis.setPadding(new Insets(10));
        painelEditarPerfis.setPrefHeight(300);
        painelEditarPerfis.setStyle("-fx-border-color: rgba(100, 100, 150, 0.3); -fx-border-width: 1; -fx-background-color: white; -fx-background-radius: 10;");

        listaPerfis.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Perfil item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    setDisable(false);
                } else {
                    setText(item.getNome());
                    setDisable("Nenhum perfil salvo".equals(item.getNome()));
                }
            }
        });

        atualizarListaPerfis.run();

        VBox sessaoPerfil = new VBox(10, perfilTitulo, botoesPerfil, painelEditarPerfis);
        sessaoPerfil.setPadding(new Insets(0, 0, 10, 0));

        // Sessão 2 - Questionários
        Label questTitulo = new Label("Gerenciar Questionários");
        questTitulo.setFont(new Font("Arial", 18));
        questTitulo.setTextFill(Color.web("#1e3d8f"));
        questTitulo.setStyle("-fx-font-weight: bold;");

        Button editarQuestoes = new Button("Editar Questionários");
        editarQuestoes.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        editarQuestoes.setOnAction(e -> {
            Questionario questionario = new Questionario();
            questionario.mostrar(new Stage());
        });

        VBox sessaoQuestoes = new VBox(10, questTitulo, editarQuestoes);
        sessaoQuestoes.setPadding(new Insets(10, 0, 10, 0));

        // Sessão 3 - Responder Formulário
        Label responderTitulo = new Label("Questionário");
        responderTitulo.setFont(new Font("Arial", 18));
        responderTitulo.setTextFill(Color.web("#1e3d8f"));
        responderTitulo.setStyle("-fx-font-weight: bold;");

        Button responderBtn = new Button("Responder Questionário");
        responderBtn.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white; -fx-font-weight: bold;");
        responderBtn.setOnAction(e -> {
            try {
                new ResponderQuestionario().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button verRespostasBtn = new Button("Ver Respostas");
        verRespostasBtn.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white; -fx-font-weight: bold;");
        verRespostasBtn.setOnAction(e -> {
            try {
                new Respostas().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox questionarioBotoes = new HBox(15, responderBtn, verRespostasBtn);
        questionarioBotoes.setAlignment(Pos.CENTER_LEFT);

        VBox sessaoResponder = new VBox(10, responderTitulo, questionarioBotoes);
        sessaoResponder.setPadding(new Insets(10, 0, 10, 0));

        // Sessão 4 - Kits
        Label kitsTitulo = new Label("Kits");
        kitsTitulo.setFont(new Font("Arial", 18));
        kitsTitulo.setTextFill(Color.web("#1e3d8f"));
        kitsTitulo.setStyle("-fx-font-weight: bold;");

        Button editarKits = new Button("Gerenciar Kits");
        editarKits.setStyle("-fx-background-color: linear-gradient(to right, #4a90e2, #357ae8); -fx-text-fill: white; -fx-font-weight: bold;");
        editarKits.setOnAction(e -> {
            Perfil perfilSelecionado = listaPerfis.getSelectionModel().getSelectedItem();
            if (perfilSelecionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um perfil antes de editar os kits.");
                alert.showAndWait();
                return;
            }
            String perfilId = perfilSelecionado.getNome();
            Kits.GerenciadorKits.mostrarJanela(perfilId);
        });

        Button visualizarKits = new Button("Visualizar Kits");
        visualizarKits.setStyle("-fx-background-color: #00796B; -fx-text-fill: white; -fx-font-weight: bold;");
        visualizarKits.setOnAction(e -> {
            Perfil perfilSelecionado = listaPerfis.getSelectionModel().getSelectedItem();
            if (perfilSelecionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um perfil antes de visualizar os kits.");
                alert.showAndWait();
                return;
            }
            String perfilId = perfilSelecionado.getNome();
            VisualizadorKits.mostrarJanela(perfilId); // ← You'll need to implement this
        });

        HBox botoesKits = new HBox(10, editarKits, visualizarKits);
        botoesKits.setAlignment(Pos.CENTER_LEFT);

        VBox sessaoKits = new VBox(10, kitsTitulo, botoesKits);
        sessaoKits.setAlignment(Pos.CENTER_LEFT);
        sessaoKits.setPadding(new Insets(20, 0, 0, 0));
        sessaoKits.setAlignment(Pos.CENTER_LEFT);
        sessaoKits.setPadding(new Insets(20, 0, 0, 0));

        painelCentral.getChildren().addAll(
                sessaoPerfil,
                new Separator(),
                sessaoQuestoes,
                new Separator(),
                sessaoResponder,
                new Separator(),
                sessaoKits,
                new Separator()
        );

        VBox scrollContent = new VBox(painelCentral);
        scrollContent.setAlignment(Pos.TOP_CENTER);
        scrollContent.setPadding(new Insets(40)); // espaço interno opcional

        ScrollPane scrollPane = new ScrollPane(scrollContent);

        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        scrollPane.setPadding(new Insets(0)); // remove margens extra

        StackPane centerWrapper = new StackPane(scrollPane);

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