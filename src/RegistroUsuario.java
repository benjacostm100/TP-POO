import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class RegistroUsuario extends JFrame {

    private PantallaInicio pantallaInicio;

    public RegistroUsuario(PantallaInicio pantallaInicio) {
        this.pantallaInicio = pantallaInicio;

        setTitle("Registro de Usuario");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Panel con fondo azul suave
        JPanel panel = new JPanel();
        panel.setBackground(new Color(200, 230, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Título
        JLabel titulo = new JLabel("Crear Nueva Cuenta");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 24));
        titulo.setForeground(new Color(30, 60, 90));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        panel.add(lblUsuario);
        panel.add(txtUsuario);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(Box.createVerticalStrut(25));

        // Botones en un panel horizontal
        JPanel botonesPanel = new JPanel();
        botonesPanel.setBackground(new Color(200, 230, 255));
        botonesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        // Estilos botones
        Font btnFont = new Font("Arial", Font.BOLD, 16);
        btnRegistrar.setFont(btnFont);
        btnCancelar.setFont(btnFont);

        btnRegistrar.setBackground(new Color(30, 60, 90));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(180, 30, 30));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        botonesPanel.add(btnRegistrar);
        botonesPanel.add(btnCancelar);

        panel.add(botonesPanel);
        add(panel, BorderLayout.CENTER);

        // Acciones
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

        btnCancelar.addActionListener(e -> {
            this.dispose();
            pantallaInicio.setVisible(true);
        });

        setVisible(true);
    }

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

    private void guardarUsuario(String usuario, String contraseña) {
        try (PrintWriter out = new PrintWriter(new FileWriter("usuarios.txt", true))) {
            out.println(usuario + ";" + contraseña + ";0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
