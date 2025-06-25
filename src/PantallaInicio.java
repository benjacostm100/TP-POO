import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PantallaInicio extends JFrame {
    public PantallaInicio() {
        setTitle("Ahorcado de Stitch - Bienvenido");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel titulo = new JLabel("AHORCADO DE STITCH", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnRegistro = new JButton("Registrarse");
        JButton btnLogin = new JButton("Iniciar Sesión");

        btnRegistro.addActionListener(e -> {
            new RegistroUsuario(this);
            setVisible(false);
        });

        btnLogin.addActionListener(e -> {
            new LoginUsuario(this);
            setVisible(false);
        });

        panel.add(titulo);
        panel.add(btnRegistro);
        panel.add(btnLogin);

        add(panel);
        setVisible(true);
    }
}
