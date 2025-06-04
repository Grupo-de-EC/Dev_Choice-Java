package com.devschoice;
//Usado para salvar dados do Perfil
public class Usuario {
    private String nome;
    private String email;

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }

    public String toTxt() {
        return "Usuario:" + nome + ";" + email;
    }

    public static Usuario fromTxt(String line) {
        String[] parts = line.split(":")[1].split(";");
        return new Usuario(parts[0], parts[1]);
    }
}

