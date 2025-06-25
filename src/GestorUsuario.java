import java.io.*;
import java.util.*;

public class GestorUsuario {
    private static final String ARCHIVO = "usuarios.txt";

    public static boolean registrarUsuario(String nombre, String contraseña) {
        if (usuarioExiste(nombre)) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            writer.write(nombre + "," + contraseña + ",0"); // nombre,clave,puntaje
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario login(String nombre, String contraseña) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3 && partes[0].equals(nombre) && partes[1].equals(contraseña)) {
                    Usuario u = new Usuario(partes[0], partes[1]);
                    u.sumarPuntos(Integer.parseInt(partes[2]));
                    return u;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean usuarioExiste(String nombre) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 1 && partes[0].equals(nombre)) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

