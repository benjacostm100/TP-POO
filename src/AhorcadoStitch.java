import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

public class AhorcadoStitch extends JFrame {

    private final String[] palabras = {
            "ohana", "lilo", "stitch", "nani", "aloha", "hawaii", "surf", "tabla",
            "experimento", "familia", "alien", "extraterrestre", "adopcion", "hermanas",
            "perro", "nave", "mision", "amor", "playa", "oceano"
    };

    private final String[] pistas = {
            "Significa familia en hawaiano",
            "Nombre de la niña protagonista",
            "Experimento alienígena azul",
            "Hermana mayor de Lilo",
            "Saludo y despedida hawaiano",
            "Estado famoso por playas y surf",
            "Deporte acuático con tabla",
            "Superficie plana para surfear",
            "Línea de experimentos secretos",
            "Grupo con lazos muy cercanos",
            "Ser vivo fuera de la Tierra",
            "Ser no terrestre y extraño",
            "Tomado como parte de la familia",
            "Relación entre hermanas",
            "Mascota canina en la historia",
            "Vehículo espacial",
            "Objetivo o tarea a cumplir",
            "Sentimiento profundo",
            "Lugar con arena y mar",
            "Gran masa de agua salada"
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

        // Fondo degradado azul turquesa
        setContentPane(new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                Color start = new Color(0, 191, 255);
                Color end = new Color(64, 224, 208);
                GradientPaint gp = new GradientPaint(0, 0, start, 0, getHeight(), end);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        });
        setLayout(new BorderLayout(20, 20));

        // Panel central blanco translúcido y bordes redondeados
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
        add(panelCentral, BorderLayout.CENTER);

        // ---------- ARRIBA: TÍTULO + USUARIO Y PUNTAJE ----------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        panelCentral.add(topPanel, BorderLayout.NORTH);

        tituloLabel = new JLabel("AHORCADO");
        tituloLabel.setFont(new Font("Papyrus", Font.BOLD, 48));
        tituloLabel.setForeground(new Color(25, 25, 112));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(tituloLabel, BorderLayout.CENTER);

        usuarioLabel = new JLabel();
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        usuarioLabel.setForeground(new Color(50, 50, 50));
        usuarioLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usuarioLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        topPanel.add(usuarioLabel, BorderLayout.EAST);

        // PANEL CENTRO: dividido en dos columnas: izquierda (pista + errores + palabra) y derecha (imagen)
        JPanel centerPanel = new JPanel(new BorderLayout(20, 15));
        centerPanel.setOpaque(false);
        panelCentral.add(centerPanel, BorderLayout.CENTER);

        // Panel izquierdo con BoxLayout vertical (columna) para pista + errores + palabra
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Palabra oculta (arriba)
        palabraLabel = new JLabel();
        palabraLabel.setFont(new Font("Monospaced", Font.BOLD, 54));
        palabraLabel.setForeground(new Color(25, 25, 112));
        palabraLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        palabraLabel.setBorder(new EmptyBorder(0, 0, 40, 0));
        leftPanel.add(palabraLabel);

        // Pista
        pistaLabel = new JLabel("Pista: ");
        pistaLabel.setFont(new Font("Arial", Font.ITALIC, 26));
        pistaLabel.setForeground(new Color(0, 100, 0));
        pistaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(pistaLabel);
        leftPanel.add(Box.createVerticalStrut(20));

        // Contador de errores
        contadorLabel = new JLabel("Errores: 0 / " + MAX_ERRORES);
        contadorLabel.setFont(new Font("Arial", Font.BOLD, 28));
        contadorLabel.setForeground(new Color(178, 34, 34));
        contadorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(contadorLabel);

        centerPanel.add(leftPanel, BorderLayout.WEST);

        // Imagen a la derecha con tamaño fijo cuadrado
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

        // PANEL LETRAS ABAJO
        letrasPanel = new JPanel(new GridLayout(3, 9, 14, 14));
        letrasPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        letrasPanel.setBackground(new Color(240, 248, 255));

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

        add(letrasPanel, BorderLayout.SOUTH);

        // Empezar juego
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
            JOptionPane.showMessageDialog(this, "¡Ganaste! La palabra era: " + palabraSecreta);
            nuevaPalabra();
        } else if (errores >= MAX_ERRORES) {
            puntaje = Math.max(0, puntaje - 5);
            actualizarUsuarioYPuntaje();
            JOptionPane.showMessageDialog(this, "¡Perdiste! La palabra era: " + palabraSecreta);
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
