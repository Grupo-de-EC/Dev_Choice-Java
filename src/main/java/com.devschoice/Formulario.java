package com.devschoice;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Formulario {
    private VBox formArea;
    private boolean modoEdicaoAtivo = false;

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
        Button editarQuestao = criarBotao("Editar Questão");
        Button limparFormulario = criarBotao("Limpar Formulário");
        Button salvarFormulario = criarBotao("Salvar");


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

        adicionarCampoTexto.setOnAction(e -> {
            String titulo = solicitarTitulo("Campo de Texto");
            if (titulo != null && !titulo.trim().isEmpty()) {
                adicionarPergunta(titulo, "texto", null);
            }
        });

        adicionarCaixaSelecao.setOnAction(e -> {
            String titulo = solicitarTitulo("Caixa de Seleção");
            if (titulo != null && !titulo.trim().isEmpty()) {
                TextInputDialog opcoesDialog = new TextInputDialog("Opção 1, Opção 2, Opção 3");
                opcoesDialog.setTitle("Opções da Caixa de Seleção");
                opcoesDialog.setHeaderText("Digite as opções separadas por vírgula:");
                opcoesDialog.setContentText("Opções:");
                Optional<String> resultadoOpcoes = opcoesDialog.showAndWait();
                if (resultadoOpcoes.isPresent()) {
                    String[] opcoes = resultadoOpcoes.get().split("\\s*,\\s*");
                    adicionarPergunta(titulo, "checkbox", opcoes);
                }
            }
        });

        adicionarListaSuspensa.setOnAction(e -> {
            String titulo = solicitarTitulo("Lista Suspensa");
            if (titulo != null && !titulo.trim().isEmpty()) {
                TextInputDialog opcoesDialog = new TextInputDialog("Opção 1, Opção 2, Opção 3");
                opcoesDialog.setTitle("Opções da Lista Suspensa");
                opcoesDialog.setHeaderText("Digite as opções separadas por vírgula:");
                opcoesDialog.setContentText("Opções:");
                Optional<String> resultadoOpcoes = opcoesDialog.showAndWait();
                if (resultadoOpcoes.isPresent()) {
                    String[] opcoes = resultadoOpcoes.get().split("\\s*,\\s*");
                    adicionarPergunta(titulo, "combo", opcoes);
                }
            }
        });

        limparFormulario.setOnAction(e -> {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmação");
            alerta.setHeaderText("Tem certeza que deseja limpar o formulário?");
            alerta.setContentText("Essa ação não pode ser desfeita.");

            Optional<ButtonType> resultado = alerta.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                formArea.getChildren().clear();
            }
        });



        salvarFormulario.setOnAction(e -> salvarDados());

        editarQuestao.setOnAction(e -> {
            modoEdicaoAtivo = !modoEdicaoAtivo;
            editarQuestao.setText(modoEdicaoAtivo ? "Sair do Modo Edição" : "Editar Questão");
        });

        root.setLeft(controles);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        inicializarFormulario();  // <-- chamada correta agora que está fora do método
    }

    public void inicializarFormulario() {
        boolean carregado = false;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("formulario.dat"))) {
            List<Pergunta> perguntasSalvas = (List<Pergunta>) ois.readObject();
            for (Pergunta p : perguntasSalvas) {
                adicionarPergunta(p.getTitulo(), p.getTipo(), p.getOpcoes());
            }
            carregado = true;
        } catch (Exception e) {
            // Ignora erro
        }

        if (!carregado) {
            adicionarQuestaoFixaTexto("Nome do seu projeto:");
            adicionarQuestaoFixaCombo("Nivel de Experiencia:", new String[]{"Iniciante", "Intermediário", "Avançado"});
            adicionarQuestaoFixaCombo("Tipo do Projeto:", new String[]{"web", "mobile", "desktop", "iot", "jogo", "analise", "outros"});
        }
    }

    private void adicionarPergunta(String titulo, String tipo, String[] opcoes) {
        Label label = new Label(titulo);
        label.setStyle(estiloTituloPergunta());

        javafx.scene.Node campo = switch (tipo) {
            case "texto" -> {
                TextField t = new TextField();
                t.setPromptText("Digite aqui...");
                t.setStyle(estiloCampoInput());
                yield t;
            }
            case "checkbox" -> {
                // Para checkbox, criar VBox com várias CheckBoxes (opções fixas ou recebidas)
                VBox caixaOpcoes = new VBox(5);
                String[] opcs = opcoes != null ? opcoes : new String[]{"Opção 1", "Opção 2", "Opção 3"};
                for (String opcao : opcs) {
                    CheckBox cb = new CheckBox(opcao);
                    cb.setStyle(estiloCheckBox());
                    caixaOpcoes.getChildren().add(cb);
                }
                yield caixaOpcoes;
            }
            case "combo" -> {
                ComboBox<String> combo = new ComboBox<>();
                combo.getItems().addAll(opcoes != null ? opcoes : new String[]{"Opção 1", "Opção 2", "Opção 3"});
                combo.setPromptText("Selecione uma opção");
                combo.setStyle(estiloComboBox());
                yield combo;
            }
            default -> new TextField();
        };

        VBox grupo = criarLinhaPergunta(label, campo, tipo);
        formArea.getChildren().add(grupo);
    }


    private VBox criarLinhaPergunta(Label label, javafx.scene.Node campo, String tipoAtual) {
        label.setOnMouseClicked(event -> {
            if (modoEdicaoAtivo) {
                TextInputDialog dialog = new TextInputDialog(label.getText());
                dialog.setTitle("Editar Pergunta");
                dialog.setHeaderText("Editar o título da pergunta:");
                dialog.setContentText("Novo título:");
                Optional<String> resultado = dialog.showAndWait();
                resultado.ifPresent(novoTitulo -> {
                    if (!novoTitulo.trim().isEmpty()) {
                        label.setText(novoTitulo);
                    }
                });

                ChoiceDialog<String> tipoDialog = new ChoiceDialog<>(tipoAtual, "texto", "checkbox", "combo");
                tipoDialog.setTitle("Editar Tipo");
                tipoDialog.setHeaderText("Escolha o novo tipo de campo:");
                tipoDialog.setContentText("Tipo:");

                Optional<String> novoTipo = tipoDialog.showAndWait();
                novoTipo.ifPresent(tipo -> {
                    int index = formArea.getChildren().indexOf(label.getParent().getParent());
                    if (index >= 0) {
                        formArea.getChildren().remove(index);

                        String[] opcoes = null;
                        if (tipo.equals("combo")) {
                            TextInputDialog opcoesDialog = new TextInputDialog("Opção 1, Opção 2, Opção 3");
                            opcoesDialog.setTitle("Editar Opções");
                            opcoesDialog.setHeaderText("Digite as novas opções separadas por vírgula:");
                            opcoesDialog.setContentText("Opções:");

                            Optional<String> resultadoOpcoes = opcoesDialog.showAndWait();
                            if (resultadoOpcoes.isPresent()) {
                                String texto = resultadoOpcoes.get();
                                opcoes = texto.split("\\s*,\\s*");
                            }
                        }

                        adicionarPergunta(label.getText(), tipo, opcoes);
                    }
                });
            }
        });

        ImageView icone = new ImageView(new Image("file:src/main/Imagens/lixeira.png"));
        icone.setFitWidth(16);
        icone.setFitHeight(16);
        Button deletar = new Button("", icone);
        deletar.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;");
        deletar.setOnAction(e -> formArea.getChildren().remove(((Button) e.getSource()).getParent().getParent()));

        HBox topo = new HBox(10, label, deletar);
        topo.setPadding(new Insets(0, 0, 4, 0));
        topo.setStyle("-fx-alignment: center-left;");

        return new VBox(4, topo, campo);
    }

    private void adicionarQuestaoFixaTexto(String titulo) {
        Label label = new Label(titulo);
        label.setStyle(estiloTituloPergunta());
        TextField campo = new TextField();
        campo.setPromptText(titulo);
        campo.setStyle(estiloCampoInput());
        formArea.getChildren().add(criarLinhaPergunta(label, campo, "texto"));
    }

    private void adicionarQuestaoFixaCombo(String titulo, String[] opcoes) {
        Label label = new Label(titulo);
        label.setStyle(estiloTituloPergunta());
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(opcoes);
        combo.setPromptText("Selecione uma opção");
        combo.setStyle(estiloComboBox());
        formArea.getChildren().add(criarLinhaPergunta(label, combo, "combo"));
    }

    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("formulario.dat"))) {
            List<Pergunta> pergunta = new ArrayList<>();
            for (javafx.scene.Node grupo : formArea.getChildren()) {
                if (grupo instanceof VBox vbox && vbox.getChildren().size() >= 2) {
                    Label label = (Label)((HBox)vbox.getChildren().get(0)).getChildren().get(0);
                    String titulo = label.getText();
                    javafx.scene.Node campo = vbox.getChildren().get(1);

                    String tipo = "texto";
                    String[] opcoes = null;

                    if (campo instanceof TextField) {
                        tipo = "texto";
                    } else if (campo instanceof VBox) { // checkbox (VBox com CheckBoxes)
                        tipo = "checkbox";
                        VBox vboxCheckbox = (VBox) campo;
                        List<String> opcs = new ArrayList<>();
                        for (javafx.scene.Node cbNode : vboxCheckbox.getChildren()) {
                            if (cbNode instanceof CheckBox cb) {
                                opcs.add(cb.getText());
                            }
                        }
                        opcoes = opcs.toArray(new String[0]);
                    } else if (campo instanceof ComboBox<?>) {
                        tipo = "combo";
                        ComboBox<?> combo = (ComboBox<?>) campo;
                        opcoes = combo.getItems().toArray(new String[0]);
                    }

                    pergunta.add(new Pergunta(titulo, tipo, opcoes));
                }
            }
            oos.writeObject(pergunta);
            new Alert(Alert.AlertType.INFORMATION, "Formulário salvo com sucesso!").showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao salvar: " + e.getMessage()).showAndWait();
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
        return "-fx-background-color: linear-gradient(to bottom, #3b82f6, #2563eb);" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: #f0f9ff;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-padding: 6 15 6 15;" +
                "-fx-border-color: #1e40af;" +
                "-fx-border-width: 2;" +
                "-fx-effect: dropshadow(gaussian, rgba(37, 99, 235, 0.6), 8, 0, 0, 2);";
    }

    private String solicitarTitulo(String tipoCampo) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo Campo");
        dialog.setHeaderText("Digite o título da nova pergunta:");
        dialog.setContentText("Título para " + tipoCampo + ":");
        return dialog.showAndWait().orElse(null);
    }
}
