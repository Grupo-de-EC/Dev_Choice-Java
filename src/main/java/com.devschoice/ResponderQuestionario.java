package com.devschoice;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResponderQuestionario extends Application {

    private List<Pergunta> perguntas;
    private List<javafx.scene.Node> camposResposta = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #0f172a;");

        perguntas = carregarPerguntas();

        if (perguntas == null || perguntas.isEmpty()) {
            Label aviso = new Label("Nenhum questionário encontrado.");
            aviso.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            root.getChildren().add(aviso);
        } else {
            for (Pergunta p : perguntas) {
                Label label = new Label(p.getTitulo());
                label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

                javafx.scene.Node campoResposta;

                switch (p.getTipo()) {
                    case "texto":
                        TextField tf = new TextField();
                        tf.setPromptText("Digite sua resposta aqui...");
                        campoResposta = tf;
                        break;

                    case "checkbox":
                        VBox caixaOpcoes = new VBox(5);
                        for (String opcao : p.getOpcoes()) {
                            CheckBox cb = new CheckBox(opcao);
                            cb.setStyle("-fx-text-fill: white;");
                            caixaOpcoes.getChildren().add(cb);
                        }
                        campoResposta = caixaOpcoes;
                        break;

                    case "combo":
                        ComboBox<String> combo = new ComboBox<>();
                        combo.getItems().addAll(p.getOpcoes());
                        combo.setPromptText("Selecione uma opção");
                        campoResposta = combo;
                        break;

                    default:
                        campoResposta = new TextField();
                        break;
                }

                camposResposta.add(campoResposta);
                root.getChildren().addAll(label, campoResposta);
            }

            Button btnEnviar = new Button("Enviar Respostas");
            btnEnviar.setStyle(
                    "-fx-background-color: #2563eb; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 8 12; " +
                            "-fx-border-radius: 8; " +
                            "-fx-background-radius: 8;"
            );

            btnEnviar.setOnAction(e -> processarRespostas());

            root.getChildren().add(btnEnviar);
        }

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Responder Questionário");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<Pergunta> carregarPerguntas() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("questionario.dat"))) {
            return (List<Pergunta>) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    private void processarRespostas() {
        List<ArquivoQuestionario> respostasParaSalvar = new ArrayList<>();

        for (int i = 0; i < perguntas.size(); i++) {
            Pergunta p = perguntas.get(i);
            javafx.scene.Node campo = camposResposta.get(i);
            String respostaTexto = "";

            switch (p.getTipo()) {
                case "texto":
                    if (campo instanceof TextField tf) {
                        respostaTexto = tf.getText().trim();
                    }
                    break;

                case "checkbox":
                    if (campo instanceof VBox vbox) {
                        List<String> selecionados = new ArrayList<>();
                        for (javafx.scene.Node node : vbox.getChildren()) {
                            if (node instanceof CheckBox cb && cb.isSelected()) {
                                selecionados.add(cb.getText());
                            }
                        }
                        respostaTexto = selecionados.isEmpty() ? "" : String.join(", ", selecionados);
                    }
                    break;

                case "combo":
                    if (campo instanceof ComboBox<?> combo) {
                        Object valor = combo.getValue();
                        respostaTexto = (valor == null) ? "" : valor.toString();
                    }
                    break;

                default:
                    respostaTexto = "";
            }

            respostasParaSalvar.add(new ArquivoQuestionario(p.getTitulo(), respostaTexto));
        }

        salvarRespostas(respostasParaSalvar);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Respostas");
        alert.setHeaderText("Respostas salvas com sucesso!");
        alert.showAndWait();
    }

    private void salvarRespostas(List<ArquivoQuestionario> respostas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("respostas.dat"))) {
            oos.writeObject(respostas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
