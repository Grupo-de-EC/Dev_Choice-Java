package com.devschoice;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Kits {

    private static final String FILE_PATH = "Kits.txt";

    public interface SaveListener {
        void onSave(String newContent);
    }

    private SaveListener saveListener;

    public void setOnSaveListener(SaveListener listener) {
        this.saveListener = listener;
    }

    public void mostrarJanela() {
        Stage stage = new Stage();
        stage.setTitle("Editor de Kits");

        TextArea textArea = new TextArea();
        textArea.setPromptText("Digite os dados dos kits aqui...");

        // Load content from file
        try {
            if (Files.exists(Paths.get(FILE_PATH))) {
                List<String> lines = Files.readAllLines(Paths.get("Kits.txt"));
                String lastLine = lines.isEmpty() ? "" : lines.get(lines.size() - 1);
                textArea.setText(lastLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button salvarBtn = new Button("Salvar");
        salvarBtn.setOnAction(e -> {
            String newContent = textArea.getText().trim();

            if (!newContent.isEmpty()) {
                try {
                    Files.write(Paths.get(FILE_PATH),
                            (newContent + System.lineSeparator()).getBytes(),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                    System.out.println("Arquivo salvo com sucesso.");

                    if (saveListener != null) {
                        saveListener.onSave(newContent);
                    }

                    textArea.clear();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox layout = new VBox(10, textArea, salvarBtn);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}