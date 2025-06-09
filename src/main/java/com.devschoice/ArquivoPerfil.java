package com.devschoice;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoPerfil {
    private static final String ARQUIVO = "perfis.dat";
    private static final String ARQUIVO_PERFIL = "perfil_atual.dat";

    // Salvar lista de perfis
    public static void salvarPerfis(List<Perfil> perfis) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(perfis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ler lista de perfis
    public static List<Perfil> lerPerfis() {
        File f = new File(ARQUIVO);
        if (!f.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Perfil>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Salvar perfil individual
    public static void salvarPerfil(Perfil perfil) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PERFIL))) {
            oos.writeObject(perfil);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ler perfil individual
    public static Perfil lerPerfil() {
        File f = new File(ARQUIVO_PERFIL);
        if (!f.exists()) return new Perfil(); // perfil vazio

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Perfil) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Perfil();
        }
    }
}
