package proyecto.teoria.de.automatas;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ERAFD extends JFrame {

    private StringBuilder process;
    private Operations operations;
    private Automata displayA;

    public ERAFD() {
        process=new StringBuilder("");
        operations=new Operations();

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

        botonCaptura.addActionListener(e -> capturarPantalla());

        botonERaAFD.addActionListener(e->{
            String regex = JOptionPane.showInputDialog(this, "Ingrese una expresión regular:", "Generar Autómata", JOptionPane.PLAIN_MESSAGE);
            if (!operations.isValidRegex(regex)) {
                JOptionPane.showMessageDialog(this, "La expresión regular no es válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            process.setLength(0);
            
            process.append(String.format("""
                    Logs/proceso de transformacion
                    %s
                    regex ->AFD-Lambda
                    \n
                    """,regex));

            
            displayA = operations.processRegexSA(regex,process);
            if (displayA == null) {
                JOptionPane.showMessageDialog(null, "Error al generar el autómata.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            displayA.nameStates();
            displayA.getFinalState().name="final";
            displayA.displayAutomata(process);
            displayA.arrangeStates(50, 10);
        });
        botonPasos.addActionListener(e->{
            JTextArea textArea = new JTextArea(process.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            if (process.length()<3) {
                return;
            }
            JOptionPane.showMessageDialog(this, scrollPane, "Proceso de Transformación", JOptionPane.INFORMATION_MESSAGE);
        });
        botonCadena.addActionListener(e->{
            if (displayA==null) {
                JOptionPane.showMessageDialog(this, "no hay automata, por favor introduzca una exprecion regular");
                return;
            }
            boolean continuar = true;
        while (continuar) {
            String testString = JOptionPane.showInputDialog(null,
                    "Ingrese una cadena para evaluar (o escriba 'salir' para terminar):", "Probar Autómata",
                    JOptionPane.PLAIN_MESSAGE);

            if (testString == null || testString.equalsIgnoreCase("salir")) {
                continuar = false;
            } else {
                boolean result = displayA.evaluateString(testString); // Suponiendo que tienes un método `evaluate` en
                                                                      // Automata
                String message = result ? "La cadena es aceptada por el autómata."
                        : "La cadena no es aceptada por el autómata.";
                JOptionPane.showMessageDialog(null, message, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        }
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

    private void capturarPantalla() {
        try {
            BufferedImage imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagen.createGraphics();
            paint(g2d);
            g2d.dispose();

            File directorio = new File("Proyecto-Teoria-de-Automatas\\src\\proyecto\\teoria\\de\\automatas\\capturas");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            File archivo = new File(directorio, "captura_" + System.currentTimeMillis() + ".png");
            ImageIO.write(imagen, "png", archivo);

            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Captura guardada correctamente");
            panel.add(label, BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(400, 100));

            Object[] options = {"ACEPTAR"};
            JOptionPane.showOptionDialog(this, panel, "Captura de Pantalla", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la captura de pantalla.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}