import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class LoginUsuario extends JFrame {

    private PantallaInicio pantallaInicio;

    public static class PanelConImagen extends JPanel {
        private Image imagenFondo;

        public PanelConImagen(String rutaImagen) {
            ImageIcon icon = new ImageIcon(rutaImagen);
            imagenFondo = icon.getImage();
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public LoginUsuario(PantallaInicio pantallaInicio) {
        this.pantallaInicio = pantallaInicio;

        setTitle("Iniciar Sesión");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Fondo con imagen
        PanelConImagen fondo = new PanelConImagen("imagenes/fondoLOGIN.png");
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Logo en lugar de título
        ImageIcon logoIcon = new ImageIcon("imagenes/LOGOLOGIN.png"); // Asegurate de tener bien la ruta
        Image logoEscalado = logoIcon.getImage().getScaledInstance(190, 70, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoEscalado));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(10));

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUsuario.setForeground(Color.BLACK);
        JTextField txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPassword.setForeground(Color.BLACK);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        panel.add(lblUsuario);
        panel.add(txtUsuario);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(Box.createVerticalStrut(25));

        // Botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setOpaque(false);
        botonesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnCancelar = new JButton("Cancelar");

        Font btnFont = new Font("Arial", Font.BOLD, 16);
        btnIngresar.setFont(btnFont);
        btnCancelar.setFont(btnFont);

        btnIngresar.setBackground(new Color(30, 60, 90));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(180, 30, 30));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        botonesPanel.add(btnIngresar);
        botonesPanel.add(btnCancelar);

        panel.add(botonesPanel);
        fondo.add(panel, BorderLayout.CENTER);

        // Acciones
        btnIngresar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String contraseña = new String(txtPassword.getPassword()).trim();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.");
                return;
            }

            if (validarUsuario(usuario, contraseña)) {
                JOptionPane.showMessageDialog(this, "¡Bienvenido " + usuario + "!");
                new AhorcadoStitch(usuario);
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
