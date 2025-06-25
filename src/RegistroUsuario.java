import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class RegistroUsuario extends JFrame {

    private PantallaInicio pantallaInicio;

    public RegistroUsuario(PantallaInicio pantallaInicio) {
        this.pantallaInicio = pantallaInicio;

        setTitle("Registro de Usuario");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Usuario:"));
        JTextField txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        JPasswordField txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");
        panel.add(btnRegistrar);
        panel.add(btnCancelar);

        add(panel);

        // Acción al registrar
        btnRegistrar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String contraseña = new String(txtPassword.getPassword()).trim();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.");
                return;
            }

            if (existeUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "Ese usuario ya existe.");
            } else {
                guardarUsuario(usuario, contraseña);
                JOptionPane.showMessageDialog(this, "¡Registro exitoso!");
                this.dispose();
                pantallaInicio.setVisible(true);
            }
        });

        // Acción al cancelar
        btnCancelar.addActionListener(e -> {
            this.dispose();
            pantallaInicio.setVisible(true);
        });

        setVisible(true);
    }

    // Verifica si ya existe el usuario en el archivo
    private boolean existeUsuario(String usuario) {
        File archivo = new File("usuarios.txt");
        if (!archivo.exists()) return false;

        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                String[] datos = linea.split(";");
                if (datos.length >= 1 && datos[0].equals(usuario)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Guarda el nuevo usuario
    private void guardarUsuario(String usuario, String contraseña) {
        try (PrintWriter out = new PrintWriter(new FileWriter("usuarios.txt", true))) {
            out.println(usuario + ";" + contraseña + ";0"); // ;0 para puntaje
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
