package proyecto.teoria.de.automatas;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            Image icono = ImageIO.read(getClass().getResource("/proyecto/teoria/de/automatas/img/icono.png"));
            setIconImage(icono);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panelConFondo = new JPanel() {
            private BufferedImage imagen;

            {
                try {
                    imagen = ImageIO.read(getClass().getResource("/proyecto/teoria/de/automatas/img/fondo.jpg"));
                    if (imagen == null) {
                        System.out.println("La imagen no se pudo cargar.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagen != null) {
                    g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("Imagen es null.");
                }
            }
        };
        panelConFondo.setLayout(new BoxLayout(panelConFondo, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Conversor de ER a AFD");
        titulo.setFont(new Font("Montserrat", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton botonIniciar = crearBoton("Iniciar", "#837a7b");
        JButton botonSalir = crearBoton("Salir", "#716869");
        JButton botonAcercaDe = crearBoton("Acerca de Nosotros", "#020b07");

        panelConFondo.add(Box.createVerticalGlue());
        panelConFondo.add(titulo);
        panelConFondo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelConFondo.add(botonIniciar);
        panelConFondo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelConFondo.add(botonSalir);
        panelConFondo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelConFondo.add(botonAcercaDe);
        panelConFondo.add(Box.createVerticalGlue());

        setContentPane(panelConFondo);
    }

    private JButton crearBoton(String texto, String colorHex) {
        JButton boton = new RoundedButton(texto, Color.decode(colorHex));
        boton.setFont(new Font("Montserrat", Font.BOLD, 18)); 
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        return boton;
    }

}