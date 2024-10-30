package proyecto.teoria.de.automatas;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class ERAFD extends JFrame {

    public ERAFD() {
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

        JPanel panelConFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int gridSize = 20;
                int width = getWidth();
                int height = getHeight();
                g.setColor(Color.LIGHT_GRAY);
                for (int x = 0; x < width; x += gridSize) {
                    g.drawLine(x, 0, x, height);
                }
                for (int y = 0; y < height; y += gridSize) {
                    g.drawLine(0, y, width, y);
                }

                g.setColor(Color.BLACK);
                int rectHeight = 70;
                g.fillRect(0, 0, width, rectHeight);
            }
        };
        panelConFondo.setLayout(null);

        JButton botonERaAFD = crearBoton("ER A AFD", "#837a7b");
        JButton botonCadena = crearBoton("CADENA", "#837a7b");
        JButton botonFuentes = crearBoton("FUENTES", "#837a7b");
        JButton botonRegresar = crearBoton("REGRESAR", "#837a7b");
        JButton botonPasos = crearBoton("PASOS", "#837a7b");
        JButton botonCaptura = crearBoton("CAPTURA", "#837a7b");

        int buttonWidth = 120;
        int buttonHeight = 40;
        int buttonY = 15; 
        int spacing = 10; 

        botonERaAFD.setBounds(10, buttonY, buttonWidth, buttonHeight);
        botonCadena.setBounds(10 + buttonWidth + spacing, buttonY, buttonWidth, buttonHeight);
        botonFuentes.setBounds(10 + 2 * (buttonWidth + spacing), buttonY, buttonWidth, buttonHeight);
        botonRegresar.setBounds(10 + 3 * (buttonWidth + spacing), buttonY, buttonWidth, buttonHeight);
        botonPasos.setBounds(10 + 4 * (buttonWidth + spacing), buttonY, buttonWidth, buttonHeight);
        botonCaptura.setBounds(10 + 5 * (buttonWidth + spacing), buttonY, buttonWidth, buttonHeight);

        panelConFondo.add(botonERaAFD);
        panelConFondo.add(botonCadena);
        panelConFondo.add(botonFuentes);
        panelConFondo.add(botonRegresar);
        panelConFondo.add(botonPasos);
        panelConFondo.add(botonCaptura);

        botonFuentes.addActionListener(e -> mostrarFuentes());

        botonRegresar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        setContentPane(panelConFondo);
    }

    private JButton crearBoton(String texto, String colorHex) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Montserrat", Font.BOLD, 12));
        boton.setBackground(Color.decode(colorHex));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    private void mostrarFuentes() {
        String fuentes = "<html><body style='font-family: Montserrat;'>" +
            "<h2>FUENTES BIBLIOGRÁFICAS</h2>" +
            "<ol>" +
            "<li>Hopcroft, J. E., Motwani, R., & Ullman, J. D. (2006). <i>Introduction to Automata Theory, Languages, and Computation</i>. Addison-Wesley.</li>" +
            "<li>Thompson, K. (1968). <i>Regular Expression Search Algorithm</i>. Communications of the ACM, 11(6), 419-422.</li>" +
            "<li>De Castro Korgi, R. (2004). <i>Teoría de la computación: Lenguajes, autómatas, gramáticas (1ª ed.)</i> [PDF]. Universidad Nacional de Colombia, Facultad de Ciencias. Impresión: UNIBIBLOS.</li>" +
            "</ol>" +
            "</body></html>";

        JEditorPane editorPane = new JEditorPane("text/html", fuentes);
        editorPane.setEditable(false);
        editorPane.setFont(new Font("Montserrat", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        Object[] options = {"ACEPTAR"};
        JOptionPane.showOptionDialog(this, scrollPane, "FUENTES", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }
}