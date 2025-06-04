package com.devschoice;

import java.io.*;
import java.util.*;

public class Persistencia {
    private static final String FILE = "dados.txt";

    public static void salvar(Usuario usuario, Kit kit, List<FormularioItem> formulario) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            writer.write(usuario.toTxt());
            writer.newLine();
            writer.write(kit.toTxt());
            writer.newLine();
            for (FormularioItem item : formulario) {
                writer.write(item.toTxt());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> carregar() {
        Usuario usuario = null;
        Kit kit = null;
        List<FormularioItem> formulario = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Usuario:")) {
                    usuario = Usuario.fromTxt(line);
                } else if (line.startsWith("Kit:")) {
                    kit = Kit.fromTxt(line);
                } else if (line.startsWith("Formulario:")) {
                    formulario.add(FormularioItem.fromTxt(line));
                }
            }
        } catch (IOException e) {
            // Primeira execução, arquivo pode não existir
        }

        Map<String, Object> dados = new HashMap<>();
        dados.put("usuario", usuario);
        dados.put("kit", kit);
        dados.put("formulario", formulario);
        return dados;
    }
}
