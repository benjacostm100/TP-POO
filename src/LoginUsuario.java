import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class LoginUsuario extends JFrame {

    private PantallaInicio pantallaInicio;

    public LoginUsuario(PantallaInicio pantallaInicio) {
        this.pantallaInicio = pantallaInicio;

        setTitle("Iniciar Sesión");
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

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnCancelar = new JButton("Cancelar");
        panel.add(btnIngresar);
        panel.add(btnCancelar);

        add(panel);

        // Acción al ingresar
        btnIngresar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String contraseña = new String(txtPassword.getPassword()).trim();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.");
                return;
            }

            if (validarUsuario(usuario, contraseña)) {
                JOptionPane.showMessageDialog(this, "¡Bienvenido " + usuario + "!");
                new AhorcadoStitch(); // Más adelante podríamos pasarle el usuario
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        });

        btnCancelar.addActionListener(e -> {
            this.dispose();
            pantallaInicio.setVisible(true);
        });

        setVisible(true);
    }

    // Verifica usuario y contraseña en el archivo
    private boolean validarUsuario(String usuario, String contraseña) {
        File archivo = new File("usuarios.txt");
        if (!archivo.exists()) return false;

        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                String[] datos = linea.split(";");
                if (datos.length >= 2 && datos[0].equals(usuario) && datos[1].equals(contraseña)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
