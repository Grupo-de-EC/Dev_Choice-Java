package com.devschoice;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.util.List;

public class Perfil implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;

    public Perfil() {
    }

    public Perfil(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    private void salvarDados() {
        List<Perfil> perfis = ArquivoPerfil.lerPerfis();
        boolean encontrado = false;

        for (int i = 0; i < perfis.size(); i++) {
            if (perfis.get(i).equals(this)) {
                perfis.set(i, this);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            perfis.add(this); // se não existir, adiciona novo perfil
        }

        ArquivoPerfil.salvarPerfis(perfis);
    }

    public void mostrarJanela(Runnable onPerfilAlterado) {
        Stage stage = new Stage();

        // Título
        Label titulo = new Label("Alterar Dados");
        titulo.setFont(new Font("Arial", 24));
        titulo.setTextFill(Color.WHITE);
        titulo.setPadding(new Insets(0, 0, 10, 0));

        // Nome
        Label nomeLabel = new Label("Nome");
        nomeLabel.setTextFill(Color.LIGHTGRAY);
        TextField nomeField = new TextField(nome);
        nomeField.setStyle(
                "-fx-background-color: #2b2f3a; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );
        nomeField.setMaxWidth(Double.MAX_VALUE);

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.setTextFill(Color.LIGHTGRAY);
        TextField emailField = new TextField(email);
        emailField.setStyle(
                "-fx-background-color: #2b2f3a; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );
        emailField.setMaxWidth(Double.MAX_VALUE);

        // Botões
        Button confirmarButton = new Button("Confirmar");
        confirmarButton.setStyle(
                "-fx-background-radius: 8;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to right, #4a90e2, #357ae8);" +
                        "-fx-text-fill: white;"
        );

        Button excluirButton = new Button("Excluir");
        excluirButton.setStyle(
                "-fx-background-radius: 8;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to right, #e53935, #b71c1c);" +
                        "-fx-text-fill: white;"
        );

        HBox botoes = new HBox(10, confirmarButton, excluirButton);
        botoes.setAlignment(Pos.CENTER);

        confirmarButton.setOnAction(e -> {
            String novoNome = nomeField.getText().trim();
            String novoEmail = emailField.getText().trim();

            if (novoNome.isEmpty() || novoEmail.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nome e email não podem ser vazios.");
                alert.showAndWait();
                return;
            }

            this.nome = novoNome;
            this.email = novoEmail;
            salvarDados();
            if (onPerfilAlterado != null) onPerfilAlterado.run();
            stage.close();
        });

        excluirButton.setOnAction(e -> {
            List<Perfil> perfis = ArquivoPerfil.lerPerfis();
            perfis.removeIf(p -> p.equals(this));
            ArquivoPerfil.salvarPerfis(perfis);
            if (onPerfilAlterado != null) onPerfilAlterado.run();
            stage.close();
        });

        // Painel escuro central
        VBox painelEscuro = new VBox(12, titulo, nomeLabel, nomeField, emailLabel, emailField, botoes);
        painelEscuro.setAlignment(Pos.CENTER);
        painelEscuro.setPadding(new Insets(30));
        painelEscuro.setStyle("-fx-background-color: #141927; -fx-background-radius: 15;");
        painelEscuro.setMaxWidth(400);

        // Wrapper para centralizar
        StackPane centerWrapper = new StackPane(painelEscuro);
        centerWrapper.setPadding(new Insets(40));
        centerWrapper.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f1f4b, #1e3d8f);");

        Scene scene = new Scene(centerWrapper, 800, 600);
        stage.setTitle("Alterar Perfil");
        stage.setScene(scene);

        stage.show();
    }

    public static void mostrarJanelaCriarNovoPerfil(Runnable onSaveCallback) {
        Stage stage = new Stage();

        Label titulo = new Label("Criar Novo Perfil");
        titulo.setFont(new Font("Arial", 24));
        titulo.setTextFill(Color.WHITE);
        titulo.setPadding(new Insets(0, 0, 10, 0));

        Label nomeLabel = new Label("Nome");
        nomeLabel.setTextFill(Color.LIGHTGRAY);
        TextField nomeField = new TextField();
        nomeField.setStyle("-fx-background-color: #2b2f3a; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        nomeField.setMaxWidth(Double.MAX_VALUE);

        Label emailLabel = new Label("Email");
        emailLabel.setTextFill(Color.LIGHTGRAY);
        TextField emailField = new TextField();
        emailField.setStyle("-fx-background-color: #2b2f3a; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        emailField.setMaxWidth(Double.MAX_VALUE);

        Button criarBtn = new Button("Criar");
        criarBtn.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-background-color: linear-gradient(to right, #4a90e2, #357ae8); -fx-text-fill: white;");

        VBox painel = new VBox(12, titulo, nomeLabel, nomeField, emailLabel, emailField, criarBtn);
        painel.setAlignment(Pos.CENTER);
        painel.setPadding(new Insets(30));
        painel.setStyle("-fx-background-color: #141927; -fx-background-radius: 15;");
        painel.setMaxWidth(400);

        StackPane root = new StackPane(painel);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f1f4b, #1e3d8f);");

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Criar Novo Perfil");

        criarBtn.setOnAction(e -> {
            String nome = nomeField.getText().trim();
            String email = emailField.getText().trim();

            if (nome.isEmpty() || email.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha nome e email");
                alert.showAndWait();
                return;
            }

            List<Perfil> perfis = ArquivoPerfil.lerPerfis();
            perfis.add(new Perfil(nome, email));
            ArquivoPerfil.salvarPerfis(perfis);

            if (onSaveCallback != null) onSaveCallback.run();
            stage.close();
        });

        stage.show();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfil perfil)) return false;
        return (email != null && email.equals(perfil.email));
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
