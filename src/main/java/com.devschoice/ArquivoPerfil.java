package com.devschoice;

import java.io.*;

public class ArquivoPerfil {
    private static final String CAMINHO_ARQUIVO = "perfil.dat";

    public static void salvarPerfil(Perfil perfil) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CAMINHO_ARQUIVO))) {
            oos.writeObject(perfil);
            System.out.println("Perfil salvo com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar perfil: " + e.getMessage());
        }
    }

    public static Perfil lerPerfil() {
        Perfil perfil = new Perfil();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CAMINHO_ARQUIVO))) {
            perfil = (Perfil) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de perfil não encontrado. Usando perfil padrão.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler perfil: " + e.getMessage());
        }
        return perfil;
    }
}

