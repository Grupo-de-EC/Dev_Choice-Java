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

    public static class Kit implements java.io.Serializable {
        private String nome;
        private String categoria;

        public Kit(String nome, String categoria) {
            this.nome = nome;
            this.categoria = categoria;
        }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        @Override
        public String toString() {
            return nome + " (" + categoria + ")";
        }
    }

    public static class GerenciadorKits extends VBox {

        private final ListView<String> kitListView = new ListView<>();
        private final ComboBox<String> categoriaComboBox = new ComboBox<>();
        private final TextField nomeField = new TextField();

        private final List<Kit> kits;
        private final List<Kit> kitsFiltrados = new java.util.ArrayList<>();
        private final String perfilId;

        public GerenciadorKits(String perfilId) {
            this.perfilId = perfilId;
            this.kits = ArquivoKits.carregarKits(perfilId);

            Label titulo = new Label("Gerenciar Kits");
            titulo.setFont(Font.font("Arial", 20));
            titulo.setStyle("-fx-text-fill: #1d4ed8; -fx-font-weight: bold;");

            // Add categories + special filter option
            categoriaComboBox.getItems().addAll(
                    "Todos",
                    "Site ou sistema web",
                    "Programa para desktop",
                    "Jogo digital",
                    "Aplicativo mobile",
                    "Análise de dados / IA",
                    "Projeto com hardware / IoT",
                    "Outros"
            );
            categoriaComboBox.setValue("Todos");

            // When categoriaComboBox changes AND no Kit selected, filter list
            categoriaComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                // Only filter if no Kit selected (or we can allow filtering anytime)
                if (kitListView.getSelectionModel().getSelectedIndex() == -1) {
                    atualizarLista();
                }
            });

            nomeField.setPromptText("Nome do Kit");
            categoriaComboBox.setEditable(false);

            // When selecting a Kit in list, fill fields
            kitListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                int index = newIndex.intValue();
                if (index >= 0 && index < kitsFiltrados.size()) {
                    Kit kit = kitsFiltrados.get(index);
                    nomeField.setText(kit.getNome());
                    categoriaComboBox.setValue(kit.getCategoria());
                } else {
                    nomeField.clear();
                    categoriaComboBox.setValue("Todos");
                }
            });

            HBox campos = new HBox(10, nomeField, categoriaComboBox);
            campos.setAlignment(Pos.CENTER);

            HBox botoes = getHBox();

            VBox.setVgrow(kitListView, Priority.ALWAYS);
            this.setSpacing(10);
            this.setPadding(new Insets(20));
            this.getChildren().addAll(titulo, kitListView, campos, botoes);
            this.setStyle("-fx-background-color: #f0f4f8; -fx-background-radius: 10;");

            atualizarLista();
        }

        private HBox getHBox() {
            Button adicionarBtn = new Button("Adicionar");
            adicionarBtn.setStyle("-fx-background-color: #357ae8; -fx-text-fill: white; -fx-font-weight: bold;");
            adicionarBtn.setOnAction(e -> {
                String nome = nomeField.getText().trim();
                String categoria = categoriaComboBox.getValue();
                if (!nome.isEmpty() && categoria != null && !"Todos".equals(categoria)) {
                    kits.add(new Kit(nome, categoria));
                    salvarEAtualizar();
                    nomeField.clear();
                    categoriaComboBox.setValue("Todos");
                    kitListView.getSelectionModel().clearSelection();
                } else {
                    alertAviso();
                }
            });

            Button editarBtn = new Button("Editar");
            editarBtn.setStyle("-fx-background-color: #00796B; -fx-text-fill: white; -fx-font-weight: bold;");
            editarBtn.setOnAction(e -> {
                int index = kitListView.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < kitsFiltrados.size()) {
                    Kit kitSelecionado = kitsFiltrados.get(index);
                    String novoNome = nomeField.getText().trim();
                    String novaCategoria = categoriaComboBox.getValue();
                    if (!novoNome.isEmpty() && novaCategoria != null && !"Todos".equals(novaCategoria)) {
                        kitSelecionado.setNome(novoNome);
                        kitSelecionado.setCategoria(novaCategoria);
                        salvarEAtualizar();
                    } else {
                        alertAviso();
                    }
                }
            });

            Button excluirBtn = new Button("Excluir");
            excluirBtn.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-weight: bold;");
            excluirBtn.setOnAction(e -> {
                int index = kitListView.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < kitsFiltrados.size()) {
                    Kit kitRemover = kitsFiltrados.get(index);
                    kits.remove(kitRemover);
                    salvarEAtualizar();
                    nomeField.clear();
                    categoriaComboBox.setValue("Visualizar Todos");
                    kitListView.getSelectionModel().clearSelection();
                }
            });

            HBox botoes = new HBox(10, adicionarBtn, editarBtn, excluirBtn);
            botoes.setAlignment(Pos.CENTER);
            return botoes;
        }

        private void salvarEAtualizar() {
            ArquivoKits.salvarKits(kits, perfilId);
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
            alert.setContentText("Preencha o nome e selecione uma categoria válida (não 'Todos').");
            alert.showAndWait();
        }

        public static void mostrarJanela(String perfilId) {
            Stage stage = new Stage();
            GerenciadorKits gerenciador = new GerenciadorKits(perfilId);
            Scene scene = new Scene(gerenciador, 600, 400);
            stage.setTitle("Gerenciador de Kits - Perfil: " + perfilId);
            stage.setScene(scene);
            stage.show();
        }
    }
}
