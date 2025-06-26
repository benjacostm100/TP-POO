import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

public class AhorcadoStitch extends JFrame {

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

    private final String[] palabras = {
        "ohana", "lilo", "stitch", "nani", "aloha", "hawai", "surf", "tabla",
        "experimento", "familia", "alien", "extraterrestre", "adopcion", "hermanas",
        "perro", "nave", "mision", "amor", "playa", "oceano", "casa", "hula","elvis","ukelele","jumba","pleakley"
    };

    private final String[] pistas = {
        "Significa familia", "Nombre de la nena hawaiana", "Experimento alienígena azul", "Hermana mayor de Lilo",
        "Hola y Chau en hawaiano", "Hogar de Lilo", "Deporte que practica Nani", "Lo que utilizan para surfear",
        "Stitch fue un", "Nunca te abandona", "Seres de otro planeta", "Muchos personajes lo son",
        "Stitch encuentra su lugar en la Tierra gracias a este acto de amor.", "Lilo y Nani",
        "Lo que Lilo cree haber adoptado", "Stitch viaja a la Tierra en una de estas,", "Cada alien en la historia tiene una",
        "Lo que transforma a Stitch de destructor a ser parte de una familia.", "Donde todos surfean",
        "Gran azul que rodea hawaii", "El lugar que intentan salvar Lilo y Nani", "Baile típico que Lilo ama practicar",
        "El ídolo musical favorito de Lilo", "Instrumento que Lilo toca", "El científico loco que creó a Stitch",
        "Alien con un solo ojo"
    };

    private String palabraSecreta;
    private String pistaActual;
    private final Set<Character> letrasAdivinadas = new HashSet<>();
    private int errores = 0;
    private final int MAX_ERRORES = 6;

    private int puntaje = 0;
    private String nombreUsuario = "Jugador";

    private JLabel tituloLabel;
    private JLabel usuarioLabel;
    private JLabel palabraLabel;
    private JLabel imagenLabel;
    private JLabel contadorLabel;
    private JLabel pistaLabel;
    private JPanel letrasPanel;

    public AhorcadoStitch(String usuario) {
        this.nombreUsuario = usuario;
        setTitle("Ahorcado de Stitch");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        // Fondo con imagen
        PanelConImagen fondo = new PanelConImagen("imagenes/fondoJUEGO.png");
        fondo.setLayout(new BorderLayout(20, 20));
        setContentPane(fondo);

        // Panel central translúcido
        JPanel panelCentral = new JPanel(new BorderLayout(20, 15)) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            }
        };
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(25, 40, 25, 40));
        fondo.add(panelCentral, BorderLayout.CENTER);

        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        panelCentral.add(topPanel, BorderLayout.NORTH);

        tituloLabel = new JLabel("¡No dejes que Stitch se enoje!");
        tituloLabel.setFont(new Font("Papyrus", Font.BOLD, 30));
        tituloLabel.setForeground(new Color(25, 25, 112));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(tituloLabel, BorderLayout.CENTER);

        usuarioLabel = new JLabel();
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        usuarioLabel.setForeground(new Color(50, 50, 50));
        usuarioLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usuarioLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        topPanel.add(usuarioLabel, BorderLayout.EAST);

        // Panel del centro
        JPanel centerPanel = new JPanel(new BorderLayout(20, 15));
        centerPanel.setOpaque(false);
        panelCentral.add(centerPanel, BorderLayout.CENTER);

        // Izquierda
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        palabraLabel = new JLabel();
        palabraLabel.setFont(new Font("Monospaced", Font.BOLD, 54));
        palabraLabel.setForeground(new Color(25, 25, 112));
        palabraLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        palabraLabel.setBorder(new EmptyBorder(0, 0, 40, 0));
        leftPanel.add(palabraLabel);

        pistaLabel = new JLabel("Pista: ");
        pistaLabel.setFont(new Font("Arial", Font.ITALIC, 26));
        pistaLabel.setForeground(new Color(0, 100, 0));
        pistaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(pistaLabel);
        leftPanel.add(Box.createVerticalStrut(20));

        contadorLabel = new JLabel("Errores: 0 / " + MAX_ERRORES);
        contadorLabel.setFont(new Font("Arial", Font.BOLD, 28));
        contadorLabel.setForeground(new Color(178, 34, 34));
        contadorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(contadorLabel);

        centerPanel.add(leftPanel, BorderLayout.WEST);

        // Derecha (imagen)
        imagenLabel = new JLabel();
        imagenLabel.setPreferredSize(new Dimension(320, 320));
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagenLabel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(30, 144, 255), 6, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        imagenLabel.setOpaque(true);
        imagenLabel.setBackground(Color.WHITE);
        centerPanel.add(imagenLabel, BorderLayout.EAST);

        // Panel inferior con letras
        letrasPanel = new JPanel(new GridLayout(3, 9, 14, 14));
        letrasPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        letrasPanel.setBackground(new Color(240, 248, 255));
        fondo.add(letrasPanel, BorderLayout.SOUTH);

        for (char letra = 'A'; letra <= 'Z'; letra++) {
            JButton boton = new JButton(String.valueOf(letra));
            boton.setFont(new Font("Arial", Font.BOLD, 28));
            boton.setFocusPainted(false);
            boton.setBackground(new Color(30, 144, 255));
            boton.setForeground(Color.WHITE);
            boton.setBorder(BorderFactory.createLineBorder(new Color(0, 104, 179), 3, true));
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            boton.addActionListener(e -> {
                JButton btn = (JButton) e.getSource();
                btn.setEnabled(false);
                procesarLetra(btn.getText().charAt(0));
            });
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (boton.isEnabled()) boton.setBackground(new Color(0, 104, 179));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (boton.isEnabled()) boton.setBackground(new Color(30, 144, 255));
                }
            });
            letrasPanel.add(boton);
        }

        nuevaPalabra();
        actualizarUsuarioYPuntaje();

        setVisible(true);
    }

    private void nuevaPalabra() {
        letrasAdivinadas.clear();
        errores = 0;
        int idx = new Random().nextInt(palabras.length);
        palabraSecreta = palabras[idx].toUpperCase();
        pistaActual = pistas[idx];
        actualizarPalabra();
        actualizarImagen();
        contadorLabel.setText("Errores: 0 / " + MAX_ERRORES);
        pistaLabel.setText("Pista: " + pistaActual);
        habilitarBotones();
    }

    private void actualizarPalabra() {
        StringBuilder sb = new StringBuilder();
        for (char c : palabraSecreta.toCharArray()) {
            sb.append(letrasAdivinadas.contains(c) ? c : "_").append(" ");
        }
        palabraLabel.setText(sb.toString());
    }

    private void procesarLetra(char letra) {
        if (palabraSecreta.contains(String.valueOf(letra))) {
            letrasAdivinadas.add(letra);
        } else {
            errores++;
        }
        actualizarImagen();
        contadorLabel.setText("Errores: " + errores + " / " + MAX_ERRORES);
        actualizarPalabra();
        chequearFinJuego();
    }

    private void actualizarImagen() {
        String ruta;
        if (errores >= MAX_ERRORES) {
            ruta = "imagenes/stitchroj.png";
        } else if (palabraCompleta()) {
            ruta = "imagenes/stitchverde.png";
        } else {
            int imgNum = errores == 0 ? 1 : errores;
            ruta = "imagenes/stitch" + imgNum + ".jpg";
        }
        ImageIcon icon = new ImageIcon(ruta);
        if (icon.getIconWidth() <= 0) {
            imagenLabel.setText("(Imagen no encontrada)");
            imagenLabel.setIcon(null);
            return;
        }
        Image imagen = icon.getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH);
        imagenLabel.setIcon(new ImageIcon(imagen));
        imagenLabel.setText(null);
    }

    private void chequearFinJuego() {
            if (palabraCompleta()) {
            puntaje += 10;
            actualizarUsuarioYPuntaje();

            // Imagen de Stitch contento
            ImageIcon icono = new ImageIcon("imagenes/stitchcontento.png");
            Image imagenEscalada = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

            JOptionPane.showMessageDialog(
                this,
                "¡Ganaste! La palabra era: " + palabraSecreta,
                "¡Bien hecho!",
                JOptionPane.INFORMATION_MESSAGE,
                iconoEscalado
            );

            nuevaPalabra();
        } else if (errores >= MAX_ERRORES) {
            puntaje = Math.max(0, puntaje - 5);
            actualizarUsuarioYPuntaje();
            ImageIcon originalIcon = new ImageIcon("imagenes/stitchenojao.png");
            Image imagenEscalada = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon iconoReducido = new ImageIcon(imagenEscalada);

            JOptionPane.showMessageDialog(
                this,
                "¡Perdiste! La palabra era: " + palabraSecreta,
                "Stitch está enojado :(",
                JOptionPane.ERROR_MESSAGE,
                iconoReducido
            );


            nuevaPalabra();
        }
    }

    private boolean palabraCompleta() {
        for (char c : palabraSecreta.toCharArray()) {
            if (!letrasAdivinadas.contains(c)) return false;
        }
        return true;
    }

    private void habilitarBotones() {
        for (Component c : letrasPanel.getComponents()) {
            if (c instanceof JButton) {
                c.setEnabled(true);
                c.setBackground(new Color(30, 144, 255));
            }
        }
    }

    private void actualizarUsuarioYPuntaje() {
        usuarioLabel.setText("<html><b>Usuario:</b> " + nombreUsuario + " &nbsp;&nbsp;&nbsp; <b>Puntaje:</b> " + puntaje + "</html>");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AhorcadoStitch("Benja"));
    }
}
