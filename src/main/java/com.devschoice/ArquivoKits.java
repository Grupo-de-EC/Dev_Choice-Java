package com.devschoice;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoKits {

    private static String caminhoArquivoPerfil(String perfilId) {
        return "kits_" + perfilId + ".dat";
    }

    public static void salvarKits(List<Kits.Kit> kits, String perfilId) {
        String caminho = caminhoArquivoPerfil(perfilId);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminho))) {
            oos.writeObject(kits);
        } catch (IOException e) {
            System.err.println("Erro ao salvar kits para perfil " + perfilId + ": " + e.getMessage());
        }
    }

    public static List<Kits.Kit> carregarKits(String perfilId) {
        String caminhoArquivo = "kits_" + perfilId + ".dat";
        File file = new File(caminhoArquivo);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Kits.Kit>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar kits do perfil " + perfilId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
