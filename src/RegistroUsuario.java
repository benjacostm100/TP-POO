import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class RegistroUsuario extends JFrame {

    private PantallaInicio pantallaInicio;

    // Panel con imagen de fondo
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

    public RegistroUsuario(PantallaInicio pantallaInicio) {
        this.pantallaInicio = pantallaInicio;

        setTitle("Registro de Usuario");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Fondo con imagen
        PanelConImagen fondo = new PanelConImagen("imagenes/fondoLOGIN.png");
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Panel principal sobre el fondo
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Hacemos el panel transparente
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Logo en lugar del texto de título
        ImageIcon logoIcon = new ImageIcon("imagenes/LOGOREGISTRO.png");
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

        // Panel de botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setOpaque(false);
        botonesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

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
        fondo.add(panel, BorderLayout.CENTER);

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
