package com.devschoice;

import java.io.Serializable;

public class ArquivoQuestionario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pergunta;
    private String resposta;

    public ArquivoQuestionario(String pergunta, String resposta) {
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public String getResposta() {
        return resposta;
    }
}