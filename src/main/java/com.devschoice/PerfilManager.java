package com.devschoice;

public class PerfilManager {
    private static String perfilAtivo;

    public static void setPerfilAtivo(String perfil) {
        perfilAtivo = perfil;
    }

    public static String getPerfilAtivo() {
        return perfilAtivo;
    }
}
