import javax.swing.*;
import java.awt.*;

public class PantallaInicio extends JFrame {

    // PanelConImagen declarado afuera de PantallaInicio para que compile bien
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

    public PantallaInicio() {
        setTitle("Ahorcado de Stitch - Bienvenido");
        setSize(520, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Creamos el panel fondo que ocupa todo el JFrame
        PanelConImagen fondo = new PanelConImagen("imagenes/fondoAhorcado.png");
        fondo.setLayout(new BorderLayout(0, 0));
        setContentPane(fondo);

        // Logo arriba
        ImageIcon logoIcon = new ImageIcon("imagenes/AhorcadoLOGO.png");
        Image logoEscalada = logoIcon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoEscalada));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        fondo.add(logoLabel, BorderLayout.NORTH);

        // Panel para botones abajo
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);  // transparente para que se vea el fondo
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 50, 40, 50));

        Font btnFont = new Font("Arial", Font.BOLD, 20);

        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.setFont(btnFont);
        btnRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistro.setBackground(new Color(30, 60, 90));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setFocusPainted(false);
        btnRegistro.setBorder(BorderFactory.createLineBorder(new Color(255, 230, 150), 2));
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(btnFont);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setBackground(new Color(30, 60, 90));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(255, 230, 150), 2));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efectos hover
        btnRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistro.setBackground(new Color(255, 230, 150));
                btnRegistro.setForeground(new Color(30, 60, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistro.setBackground(new Color(30, 60, 90));
                btnRegistro.setForeground(Color.WHITE);
            }
        });
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(255, 230, 150));
                btnLogin.setForeground(new Color(30, 60, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(30, 60, 90));
                btnLogin.setForeground(Color.WHITE);
            }
        });

        btnRegistro.addActionListener(e -> {
            new RegistroUsuario(this);
            setVisible(false);
        });

        btnLogin.addActionListener(e -> {
            new LoginUsuario(this);
            setVisible(false);
        });

        panelBotones.add(btnRegistro);
        panelBotones.add(Box.createVerticalStrut(30));
        panelBotones.add(btnLogin);

        fondo.add(panelBotones, BorderLayout.SOUTH);

        // Borde elegante alrededor de todo
        ((JComponent) getContentPane()).setBorder(BorderFactory.createLineBorder(new Color(30, 60, 90), 3));

        setVisible(true);
    }
}

