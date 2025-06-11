package com.devschoice;

import java.io.Serializable;

public class Pergunta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titulo;
    private String tipo; // "texto", "checkbox", "combo"
    private String[] opcoes; // pode ser null para "texto"

    public Pergunta(String titulo, String tipo, String[] opcoes) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.opcoes = opcoes;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public String[] getOpcoes() {
        return opcoes;
    }
}

