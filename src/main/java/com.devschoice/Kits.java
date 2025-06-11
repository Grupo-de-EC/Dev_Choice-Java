package com.devschoice;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class Kits {

    // Modelo do Kit com categoria
    public static class Kit implements java.io.Serializable {
        private String nome;
        private String categoria;  // NOVO campo

        public Kit(String nome, String categoria) {
            this.nome = nome;
            this.categoria = categoria;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        @Override
        public String toString() {
            return nome + " (" + categoria + ")";
        }
    }

    // Interface CRUD
    public static class GerenciadorKits extends VBox {
        private final ListView<String> kitListView;
        private final List<Kit> kits;
        private final ComboBox<String> categoriaComboBox;  // filtro
        private final TextField nomeField;
        private final ComboBox<String> categoriaField;     // campo para adicionar/editar categoria
        private final List<Kit> kitsFiltrados = new java.util.ArrayList<>(); // lista para salvar os kits com o filtro


        public GerenciadorKits() {
            kits = ArquivoKits.carregarKits();

            Label titulo = new Label("Gerenciar Kits");
            titulo.setFont(Font.font("Arial", 20));
            titulo.setStyle("-fx-text-fill: #1d4ed8; -fx-font-weight: bold;");

            // ComboBox filtro para categoria (project type)
            categoriaComboBox = new ComboBox<>();
            categoriaComboBox.getItems().addAll("Todos", "Site ou sistema web", "Programa para desktop", "Jogo digital", "Aplicativo mobile", "Análise de dados / IA", "Projeto com hardware / IoT", "Outros");
            categoriaComboBox.setValue("Todos");  // valor padrão
            categoriaComboBox.valueProperty().addListener((obs, oldVal, newVal) -> atualizarLista());

            kitListView = new ListView<>();
            kitListView.setPrefHeight(200);
            atualizarLista();

            // Campos para nome e categoria do kit (para adicionar/editar)
            nomeField = new TextField();
            nomeField.setPromptText("Nome do kit");

            categoriaField = new ComboBox<>();
            categoriaField.getItems().addAll("Site ou sistema web", "Programa para desktop", "Jogo digital", "Aplicativo mobile", "Análise de dados / IA", "Projeto com hardware / IoT", "Outros");
            categoriaField.setPromptText("Categoria");

            // Quando selecionar um item na lista, carregar os dados nos campos
            kitListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                int index = newIndex.intValue();
                if (index >= 0 && index < kitsFiltrados.size()) {
                    Kit kit = kitsFiltrados.get(index); //Busca a lista filtrada especifica
                    nomeField.setText(kit.getNome());
                    categoriaField.setValue(kit.getCategoria());
                }
            });

            Button adicionarBtn = new Button("Adicionar");
            adicionarBtn.setOnAction(e -> {
                String nome = nomeField.getText().trim();
                String categoria = categoriaField.getValue();
                if (!nome.isEmpty() && categoria != null) {
                    kits.add(new Kit(nome, categoria));
                    salvarEAtualizar();
                    nomeField.clear();
                    categoriaField.setValue(null);
                } else {
                    alertAviso();
                }
            });

            Button editarBtn = new Button("Editar");
            editarBtn.setOnAction(e -> {
                int index = kitListView.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < kitsFiltrados.size()) {
                    Kit kitSelecionado = kitsFiltrados.get(index);
                    String novoNome = nomeField.getText().trim();
                    String novaCategoria = categoriaField.getValue();
                    if (!novoNome.isEmpty() && novaCategoria != null) {
                        kitSelecionado.setNome(novoNome);
                        kitSelecionado.setCategoria(novaCategoria);
                        salvarEAtualizar();
                    } else {
                        alertAviso();
                    }
                }
            });

            Button excluirBtn = new Button("Excluir");
            excluirBtn.setOnAction(e -> {
                int index = kitListView.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < kitsFiltrados.size()) {
                    Kit kitRemover = kitsFiltrados.get(index);
                    kits.remove(kitRemover);
                    salvarEAtualizar();
                    nomeField.clear();
                    categoriaField.setValue(null);
                }
            });

            HBox botoes = new HBox(10, adicionarBtn, editarBtn, excluirBtn);
            botoes.setAlignment(Pos.CENTER);

            HBox campos = new HBox(10, nomeField, categoriaField);
            campos.setAlignment(Pos.CENTER);

            VBox.setVgrow(kitListView, Priority.ALWAYS);
            this.setSpacing(10);
            this.setPadding(new Insets(20));
            this.getChildren().addAll(titulo, categoriaComboBox, kitListView, campos, botoes);
            this.setStyle("-fx-background-color: #f0f4f8; -fx-background-radius: 10;");
        }

        private void salvarEAtualizar() {
            ArquivoKits.salvarKits(kits);
            atualizarLista();
        }

        private void atualizarLista() {
            kitListView.getItems().clear();
            kitsFiltrados.clear();
            String filtro = categoriaComboBox.getValue();
            for (Kit kit : kits) {
                if ("Todos".equals(filtro) || kit.getCategoria().equals(filtro)) {
                    kitListView.getItems().add(kit.getNome() + " (" + kit.getCategoria() + ")");
                    kitsFiltrados.add(kit);
                }
            }
        }

        private void alertAviso() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Preencha o nome e selecione uma categoria.");
            alert.showAndWait();
        }

        // Método estático para abrir a janela de gerenciamento — pode continuar, se quiser
        public static void mostrarJanela() {
            Stage stage = new Stage();
            GerenciadorKits gerenciador = new GerenciadorKits();
            Scene scene = new Scene(gerenciador, 600, 400);
            stage.setTitle("Gerenciador de Kits");
            stage.setScene(scene);
            stage.show();
        }
    }

}
