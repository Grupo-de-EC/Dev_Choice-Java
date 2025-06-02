package com.devchoice.devchoicejava;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Clique aqui!");

        // Ao clicar, abrir a tela de Perfil
        btn.setOnAction(e -> {
            Perfil perfil = new Perfil();
            perfil.mostrarJanela();
        });

        StackPane root = new StackPane(btn);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Teste JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
