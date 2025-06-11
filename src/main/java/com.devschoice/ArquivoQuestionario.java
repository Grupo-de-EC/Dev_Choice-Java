package com.devschoice;

import java.io.Serializable;

public class ArquivoQuestionario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pergunta;
    private String resposta;

    // Construtor padrão (opcional, caso queira usar frameworks que exigem)
    public ArquivoQuestionario() {
    }

    public ArquivoQuestionario(String pergunta, String resposta) {
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    @Override
    public String toString() {
        return "Pergunta: " + pergunta + "\nResposta: " + resposta;
    }
}
