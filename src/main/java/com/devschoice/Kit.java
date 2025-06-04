package com.devschoice;
// Usado para salvar dados do Kits
public class Kit {
    private String nome;

    public Kit(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }

    public String toTxt() {
        return "Kit:" + nome;
    }

    public static Kit fromTxt(String line) {
        return new Kit(line.split(":")[1]);
    }
}

