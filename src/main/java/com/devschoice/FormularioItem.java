package com.devschoice;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FormularioItem {

    private String tipo; // "Texto", "CheckBox", "ComboBox"
    private String valor; // Dado textual para representação

    public FormularioItem(String tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getTipo() { return tipo; }
    public String getValor() { return valor; }

    public String toTxt() {
        return "Formulario:" + tipo + ";" + valor;
    }

    public static FormularioItem fromTxt(String line) {
        String[] parts = line.split(":")[1].split(";");
        return new FormularioItem(parts[0], parts.length > 1 ? parts[1] : "");
    }

    // Método para coletar os dados do formulário a partir de um VBox que contém os campos
    public static List<FormularioItem> coletarFormulario(VBox formArea) {
        List<FormularioItem> itens = new ArrayList<>();
        for (Node node : formArea.getChildren()) {
            if (node instanceof TextField tf) {
                itens.add(new FormularioItem("Texto", tf.getText()));
            } else if (node instanceof CheckBox cb) {
                itens.add(new FormularioItem("CheckBox", cb.getText() + ":" + cb.isSelected()));
            } else if (node instanceof ComboBox<?> cb) {
                Object selected = cb.getValue();
                itens.add(new FormularioItem("ComboBox", selected != null ? selected.toString() : ""));
            }
        }
        return itens;
    }
}
