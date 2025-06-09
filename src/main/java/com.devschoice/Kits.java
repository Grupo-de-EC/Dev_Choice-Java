package com.devschoice;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class Kits extends Application {

    // Modelo do Kit
    public static class Kit implements java.io.Serializable {
        private String nome;

        public Kit(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    // Interface CRUD
    public static class GerenciadorKits extends VBox {
        private final ListView<String> kitListView;
        private List<Kit> kits;

        public GerenciadorKits() {
            kits = ArquivoKits.carregarKits();

            Label titulo = new Label("Gerenciar Kits");
            titulo.setFont(Font.font("Arial", 20));
            titulo.setStyle("-fx-text-fill: #1d4ed8; -fx-font-weight: bold;");

            kitListView = new ListView<>();
            atualizarLista();

            TextField nomeField = new TextField();
            nomeField.setPromptText("Nome do kit");

            Button adicionarBtn = new Button("Adicionar");
            adicionarBtn.setOnAction(e -> {
                String nome = nomeField.getText().trim();
                if (!nome.isEmpty()) {
                    kits.add(new Kit(nome));
                    salvarEAtualizar();
                    nomeField.clear();
                }
            });

            Button editarBtn = new Button("Editar");
            editarBtn.setOnAction(e -> {
                int index = kitListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    String novoNome = nomeField.getText().trim();
                    if (!novoNome.isEmpty()) {
                        kits.get(index).setNome(novoNome);
                        salvarEAtualizar();
                    }
                }
            });

            Button excluirBtn = new Button("Excluir");
            excluirBtn.setOnAction(e -> {
                int index = kitListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    kits.remove(index);
                    salvarEAtualizar();
                }
            });

            HBox botoes = new HBox(10, adicionarBtn, editarBtn, excluirBtn);
            botoes.setAlignment(Pos.CENTER);

            VBox.setVgrow(kitListView, Priority.ALWAYS);
            this.setSpacing(10);
            this.setPadding(new Insets(20));
            this.getChildren().addAll(titulo, kitListView, nomeField, botoes);
            this.setStyle("-fx-background-color: #f0f4f8; -fx-background-radius: 10;");
        }

        private void salvarEAtualizar() {
            ArquivoKits.salvarKits(kits);
            atualizarLista();
        }

        private void atualizarLista() {
            kitListView.getItems().clear();
            for (Kit kit : kits) {
                kitListView.getItems().add(kit.getNome());
            }
        }

        // Método estático para abrir a janela de gerenciamento
        public static void mostrarJanela() {
            Stage stage = new Stage();
            GerenciadorKits gerenciador = new GerenciadorKits();
            Scene scene = new Scene(gerenciador, 600, 400);
            stage.setTitle("CRUD de Kits");
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void start(Stage stage) {
        GerenciadorKits.mostrarJanela();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
