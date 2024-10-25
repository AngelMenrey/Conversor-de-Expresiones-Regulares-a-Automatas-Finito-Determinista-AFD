package proyecto.teoria.de.automatas;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MenuPrincipal extends JFrame {

    private JPanel panelConFondo;

    public MenuPrincipal() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            Image icono = ImageIO.read(getClass().getResource("/proyecto/teoria/de/automatas/img/icono.png"));
            setIconImage(icono);

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Cursor cursorPersonalizado = toolkit.createCustomCursor(icono, new Point(0, 0), "CursorPersonalizado");
            setCursor(cursorPersonalizado);
        } catch (IOException e) {
            e.printStackTrace();
        }

        panelConFondo = new JPanel() {
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

        botonSalir.addActionListener(e -> System.exit(0));

        botonIniciar.addActionListener(e -> {
            new ERAFD().setVisible(true);
            dispose();
        });

        botonAcercaDe.addActionListener(e -> mostrarAcercaDe());

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

    private void mostrarAcercaDe() {
        panelConFondo.removeAll();
        panelConFondo.setLayout(new BorderLayout());

        String textoHTML = "<html><body style='text-align: justify; color: white; font-family: Montserrat;'>" +
            "<h1 style='text-align: center;'>Acerca de Nosotros</h1>" +
            "<p>Somos un equipo de estudiantes de Ingeniería en Desarrollo de Software del Centro de Enseñanza Técnica Industrial (CETI), plantel Colomos, actualmente cursando el sexto semestre en el turno matutino. Este proyecto, desarrollado en el marco de la materia de Teoría de Autómatas, refleja nuestra pasión por la informática teórica y su aplicación práctica en el desarrollo de herramientas que faciliten la comprensión de los lenguajes formales y los autómatas.</p>" +
            "<p>Conversor de Expresiones Regulares a Autómatas Finito Determinista (AFD) es una aplicación de escritorio desarrollada en Java que permite a los usuarios ingresar expresiones regulares y convertirlas en autómatas finitos deterministas (AFD), proporcionando una visualización clara e interactiva de este proceso fundamental en la teoría de lenguajes formales. Nuestro objetivo con esta herramienta es no solo poner en práctica los conceptos aprendidos, sino también ofrecer una aplicación útil para estudiantes, docentes y cualquier persona interesada en el análisis y procesamiento de lenguajes.</p>" +
            "<p>Este proyecto ha sido posible gracias a nuestra formación en CETI y a la dedicación de nuestro equipo, que se ha esforzado en unir la teoría con la práctica de una manera eficaz y didáctica.</p>" +
            "<p><b>Integrantes del equipo:</b></p>" +
            "<p>Ángel Emanuel Mendoza Reyes<br>" +
            "Francisco Javier Herrera Ruiz<br>" +
            "Muriel Cristobal Becerra Jiménez</p>" +
            "<p><b>Escuela:</b> Centro de Enseñanza Técnica Industrial (CETI)<br>" +
            "<b>Carrera:</b> Ingeniería en Desarrollo de Software<br>" +
            "<b>Semestre:</b> Sexto<br>" +
            "<b>Materia:</b> Teoría de Autómatas</p>" +
            "<p>Nos complace presentar esta herramienta con la confianza de que contribuirá al entendimiento de los autómatas y las expresiones regulares en un contexto práctico y accesible.</p>" +
            "</body></html>";

        JTextPane textoAcercaDe = new JTextPane();
        textoAcercaDe.setContentType("text/html");
        textoAcercaDe.setText(textoHTML);
        textoAcercaDe.setFont(new Font("Montserrat", Font.PLAIN, 14));
        textoAcercaDe.setForeground(Color.WHITE);
        textoAcercaDe.setOpaque(false);
        textoAcercaDe.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textoAcercaDe);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panelConFondo.add(scrollPane, BorderLayout.CENTER);

        JButton botonRegresar = crearBoton("Regresar", "#837a7b");
        botonRegresar.addActionListener(e -> regresarAlMenu());
        panelConFondo.add(botonRegresar, BorderLayout.NORTH);

        panelConFondo.revalidate();
        panelConFondo.repaint();
    }

    private void regresarAlMenu() {
        panelConFondo.removeAll();
        panelConFondo.setLayout(new BoxLayout(panelConFondo, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Conversor de ER a AFD");
        titulo.setFont(new Font("Montserrat", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton botonIniciar = crearBoton("Iniciar", "#837a7b");
        JButton botonSalir = crearBoton("Salir", "#716869");
        JButton botonAcercaDe = crearBoton("Acerca de Nosotros", "#020b07");

        botonSalir.addActionListener(e -> System.exit(0));

        botonIniciar.addActionListener(e -> {
            new ERAFD().setVisible(true);
            dispose();
        });

        botonAcercaDe.addActionListener(e -> mostrarAcercaDe());

        panelConFondo.add(Box.createVerticalGlue());
        panelConFondo.add(titulo);
        panelConFondo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelConFondo.add(botonIniciar);
        panelConFondo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelConFondo.add(botonSalir);
        panelConFondo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelConFondo.add(botonAcercaDe);
        panelConFondo.add(Box.createVerticalGlue());

        panelConFondo.revalidate();
        panelConFondo.repaint();
    }
}