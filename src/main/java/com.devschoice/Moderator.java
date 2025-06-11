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

        HBox botoesPerfil = new HBox(10, criarNovoPerfilBtn);
        botoesPerfil.setAlignment(Pos.CENTER_LEFT);

        // ListView para navbar lateral
        ListView<Perfil> listaPerfis = new ListView<>();
        listaPerfis.setPrefWidth(250);

        // Painel direito para edição do perfil selecionado
        VBox painelEdicao = new VBox(10);
        painelEdicao.setPadding(new Insets(10));
        painelEdicao.setStyle("-fx-background-color: #f0f0f0;");

        // Adiciona um botão "Editar Perfil" no painel de edição para confirmar alterações
        Button confirmarBtn = new Button("Confirmar Alteração");
        confirmarBtn.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        Button excluirBtn = new Button("Excluir Perfil");
        excluirBtn.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox botoesEdicao = new HBox(10, confirmarBtn, excluirBtn);

        // Layout: painel com lista e painel edição sempre visíveis lado a lado
        BorderPane painelEditarPerfis = new BorderPane();
        painelEditarPerfis.setLeft(listaPerfis);
        painelEditarPerfis.setCenter(painelEdicao);
        painelEditarPerfis.setPadding(new Insets(10));
        painelEditarPerfis.setPrefHeight(300);
        painelEditarPerfis.setStyle("-fx-border-color: rgba(100, 100, 150, 0.3); -fx-border-width: 1; -fx-background-color: white; -fx-background-radius: 10;");

        // Atualiza lista de perfis com mensagem caso vazio
        Runnable atualizarListaPerfis = () -> {
            List<Perfil> perfis = ArquivoPerfil.lerPerfis();
            listaPerfis.getItems().clear();

            if (perfis.isEmpty()) {
                // Limpa a lista e desabilita o ListView
                listaPerfis.setDisable(true);
                painelEdicao.getChildren().setAll(new Label("Nenhum perfil salvo"));
            } else {
                listaPerfis.setDisable(false);
                listaPerfis.getItems().setAll(perfis);
                painelEdicao.getChildren().clear();
                painelEdicao.getChildren().add(new Label("Nenhum perfil selecionado"));
                listaPerfis.getSelectionModel().selectFirst();
            }
        };

        atualizarListaPerfis.run();

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

        // Evento criar novo perfil
        criarNovoPerfilBtn.setOnAction(e -> Perfil.mostrarJanelaCriarNovoPerfil(atualizarListaPerfis));

        // Evento seleção perfil na lista
        listaPerfis.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            painelEdicao.getChildren().clear();

            if (newSel == null) {
                painelEdicao.getChildren().add(new Label("Nenhum perfil selecionado"));
                return;
            }

            String nomeOriginal = newSel.getNome();

            TextField nomeField = new TextField(newSel.getNome());
            TextField emailField = new TextField(newSel.getEmail());

            confirmarBtn.setOnAction(ev -> {
                String novoNome = nomeField.getText().trim();
                String novoEmail = emailField.getText().trim();

                List<Perfil> perfis = ArquivoPerfil.lerPerfis();

                for (Perfil perfi : perfis) {
                    if (perfi.getNome().equals(nomeOriginal)) {  // compara pelo nome original
                        perfi.setNome(novoNome);
                        perfi.setEmail(novoEmail);
                        break;
                    }
                }

                ArquivoPerfil.salvarPerfis(perfis);
                atualizarListaPerfis.run();
            });

            excluirBtn.setOnAction(ev -> {
                List<Perfil> perfis = ArquivoPerfil.lerPerfis();
                perfis.removeIf(p -> p.equals(newSel));
                ArquivoPerfil.salvarPerfis(perfis);
                atualizarListaPerfis.run();
            });

            painelEdicao.getChildren().addAll(
                    new Label("Nome:"), nomeField,
                    new Label("Email:"), emailField,
                    new HBox(10, confirmarBtn, excluirBtn)
            );
        });

        atualizarListaPerfis.run();

        // Layout final: sessão perfil com título, botões e painel de perfis e edição
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

        // Sessão 3 - Responder Formulário
        Label responderTitulo = new Label("Questionário");
        responderTitulo.setFont(new Font("Arial", 18));
        responderTitulo.setTextFill(Color.web("#1e3d8f"));
        responderTitulo.setStyle("-fx-font-weight: bold;");

        Button responderQuestionarioBtn = new Button("Responder Questionário");
        responderQuestionarioBtn.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        responderQuestionarioBtn.setOnAction(e -> {
            ResponderQuestionario responderApp = new ResponderQuestionario();
            try {
                responderApp.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox sessaoQuestoes = new VBox(10, questTitulo, editarQuestoes);
        sessaoQuestoes.setPadding(new Insets(10, 0, 10, 0));

        VBox sessaoResponder = new VBox(10, responderTitulo, responderQuestionarioBtn);
        sessaoResponder.setPadding(new Insets(10, 0, 10, 0));

        // Sessão 4 - Kits
        Label kitsTitulo = new Label("Gerenciar Kits");
        kitsTitulo.setFont(new Font("Arial", 18));
        kitsTitulo.setTextFill(Color.web("#1e3d8f"));
        kitsTitulo.setStyle("-fx-font-weight: bold;");

        Button editarKits = new Button("Editar Kits");
        editarKits.setStyle("-fx-background-color: linear-gradient(to right, #4a90e2, #357ae8); -fx-text-fill: white; -fx-font-weight: bold;");
        editarKits.setOnAction(e -> {
            Perfil perfilSelecionado = listaPerfis.getSelectionModel().getSelectedItem();
            if (perfilSelecionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um perfil antes de editar os kits.");
                alert.showAndWait();
                return;
            }
            String perfilId = perfilSelecionado.getNome(); // ou outro identificador único

            Kits.GerenciadorKits.mostrarJanela(perfilId);
        });

        VBox sessaoKits = new VBox(10, kitsTitulo, editarKits);
        sessaoKits.setAlignment(Pos.CENTER_LEFT);
        sessaoKits.setPadding(new Insets(20, 0, 0, 0));

        // Adicionar todas as seções no painel branco
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

        // Layout central com padding e background
        StackPane centerWrapper = new StackPane(painelCentral);
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
