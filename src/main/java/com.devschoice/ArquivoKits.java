package com.devschoice;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoKits {
    private static final String CAMINHO_ARQUIVO = "kits.dat";

    public static void salvarKits(List<Kits.Kit> kits) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CAMINHO_ARQUIVO))) {
            oos.writeObject(kits);
        } catch (IOException e) {
            System.err.println("Erro ao salvar kits: " + e.getMessage());
        }
    }

    public static List<Kits.Kit> carregarKits() {
        File file = new File(CAMINHO_ARQUIVO);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Kits.Kit>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar kits: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
