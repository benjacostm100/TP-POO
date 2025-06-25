import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AhorcadoStitch extends JFrame {

    private final String[] palabras = {
            "ohana", "lilo", "stitch", "nani", "aloha", "hawaii", "surf", "tabla",
            "experimento", "familia", "alien", "extraterrestre", "adopcion", "hermanas",
            "perro", "nave", "mision", "amor", "playa", "oceano"
    };

    private String palabraSecreta;
    private Set<Character> letrasAdivinadas = new HashSet<>();
    private int errores = 0;
    private final int MAX_ERRORES = 8;

    private JLabel palabraLabel;
    private JLabel imagenLabel;
    private JPanel letrasPanel;

    public AhorcadoStitch() {
        setTitle("Ahorcado de Stitch");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Elegir palabra aleatoria
        Random random = new Random();
        palabraSecreta = palabras[random.nextInt(palabras.length)].toUpperCase();

        // Etiqueta para mostrar la palabra oculta
        palabraLabel = new JLabel();
        palabraLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        palabraLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(palabraLabel, BorderLayout.NORTH);

        // Imagen de Stitch
        imagenLabel = new JLabel();
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        actualizarImagen();
        add(imagenLabel, BorderLayout.CENTER);

        // Panel de botones con letras
        letrasPanel = new JPanel(new GridLayout(3, 9, 5, 5));
        for (char letra = 'A'; letra <= 'Z'; letra++) {
            JButton boton = new JButton(String.valueOf(letra));
            boton.setFont(new Font("Arial", Font.BOLD, 18));
            boton.addActionListener(e -> {
                JButton btn = (JButton) e.getSource();
                btn.setEnabled(false);
                procesarLetra(btn.getText().charAt(0));
            });
            letrasPanel.add(boton);
        }
        add(letrasPanel, BorderLayout.SOUTH);

        actualizarPalabra();
        setVisible(true);
    }

    // Mostrar palabra con guiones
    private void actualizarPalabra() {
        StringBuilder sb = new StringBuilder();
        for (char c : palabraSecreta.toCharArray()) {
            if (letrasAdivinadas.contains(c)) {
                sb.append(c).append(" ");
            } else {
                sb.append("_ ");
            }
        }
        palabraLabel.setText(sb.toString());
    }

    // Procesar letra seleccionada
    private void procesarLetra(char letra) {
        if (palabraSecreta.indexOf(letra) >= 0) {
            letrasAdivinadas.add(letra);
        } else {
            errores++;
            actualizarImagen();
        }
        actualizarPalabra();
        chequearFinJuego();
    }

    // Cambiar imagen según los errores
    private void actualizarImagen() {
        String ruta = "imagenes/stitch" + errores + ".png";
        ImageIcon icon = new ImageIcon(ruta);
        Image imagen = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        icon = new ImageIcon(imagen);
        imagenLabel.setIcon(icon);
    }

    // Verificar si ganó o perdió
    private void chequearFinJuego() {
        if (errores >= MAX_ERRORES) {
            JOptionPane.showMessageDialog(this, "¡Perdiste! La palabra era: " + palabraSecreta);
            reiniciarJuego();
        } else if (palabraCompleta()) {
            JOptionPane.showMessageDialog(this, "¡Ganaste! 🌟");
            reiniciarJuego();
        }
    }

    // Verifica si completó toda la palabra
    private boolean palabraCompleta() {
        for (char c : palabraSecreta.toCharArray()) {
            if (!letrasAdivinadas.contains(c)) {
                return false;
            }
        }
        return true;
    }

    // Reiniciar juego con una nueva palabra
    private void reiniciarJuego() {
        letrasAdivinadas.clear();
        errores = 0;
        palabraSecreta = palabras[new Random().nextInt(palabras.length)].toUpperCase();

        for (Component c : letrasPanel.getComponents()) {
            if (c instanceof JButton) {
                c.setEnabled(true);
            }
        }

        actualizarImagen();
        actualizarPalabra();
    }
}
