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
import java.util.Map;




public class Main extends Application {

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
        Label perfilTitulo = new Label("Perfil do Administrador");
        perfilTitulo.setFont(new Font("Arial", 18));
        perfilTitulo.setTextFill(Color.web("#1e3d8f"));

        Label nome = new Label("Nome: Adm02");
        Label email = new Label("Email: batatao@devschoice.com");

        Button editarPerfil = new Button("Editar Perfil");
        editarPerfil.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        editarPerfil.setOnAction(e -> {
            Perfil perfil = new Perfil();
            perfil.mostrarJanela();
        });

        VBox sessaoPerfil = new VBox(5, perfilTitulo, nome, email, editarPerfil);
        sessaoPerfil.setPadding(new Insets(0, 0, 10, 0));

        // Sessão 2 - Questionários
        Label questTitulo = new Label("Gerenciar Questionários");
        questTitulo.setFont(new Font("Arial", 18));
        questTitulo.setTextFill(Color.web("#1e3d8f"));

        Button editarQuestoes = new Button("Editar Questionários");
        editarQuestoes.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        editarQuestoes.setOnAction(e -> {
            Formulario formulario = new Formulario();
            formulario.mostrar(new Stage());
        });

        VBox sessaoQuestoes = new VBox(5, questTitulo, editarQuestoes);
        sessaoQuestoes.setPadding(new Insets(10, 0, 10, 0));

        // Sessão 3 - Feedback
        Label kitsTitulo = new Label("Gerenciar Kits");
        kitsTitulo.setFont(new Font("Arial", 18));
        kitsTitulo.setTextFill(Color.web("#1e3d8f"));

        Label kit = new Label("Nome: Java");

        Button editarKits = new Button("Editar Kits:");
        editarKits.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
        editarKits.setOnAction(e -> {
            Kits kits = new Kits();
            kits.mostrarJanela();
        });

        VBox sessaoKits = new VBox(5, kitsTitulo, kit, editarKits);
        sessaoKits.setPadding(new Insets(0, 0, 10, 0));

        // Adiciona tudo ao painel branco
        painelCentral.getChildren().addAll(sessaoPerfil, new Separator(), sessaoQuestoes, new Separator(), sessaoKits, new Separator());

        // Centralização no fundo azul
        StackPane centerWrapper = new StackPane(painelCentral);
        centerWrapper.setPadding(new Insets(40));
        centerWrapper.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3d8f, #2c4f99);");

        Map<String, Object> dados = Persistencia.carregar();
        Usuario usuario = (Usuario) dados.get("usuario");
        Kit kitObj = (Kit) dados.get("kit");

        if (usuario != null) {
            nome.setText("Nome: " + usuario.getNome());
            email.setText("Email: " + usuario.getEmail());
        }

        if (kitObj != null) {
            kit.setText("Nome: " + kitObj.getNome());
        }


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
