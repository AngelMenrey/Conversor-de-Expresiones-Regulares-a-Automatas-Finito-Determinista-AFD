package proyecto.teoria.de.automatas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ERAFD extends JFrame {

    private StringBuilder process;
    private Operations operations;
    private Automata displayA;
    private JLabel automataLabel;
    private Point initialClick;

    private JPanel drawingPanel;

    // Clase determinar la vista grafica principal
    public ERAFD() {
        process = new StringBuilder("");
        operations = new Operations();

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
                if (displayA != null) {

                    // displayA.arrangeStatesInCircle(100, 200, 100);
                    // drawAutomaton(g);
                }
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

        // seccion nueva para el panel conscroll
        // Panel de dibujo con scroll
        drawingPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (displayA != null) {
                    int spacing = 150; // Espaciado entre estados
                    int totalStates = displayA.countStates();

                    // Calcular el radio usando la fórmula en arrangeStatesInCircle
                    double radius = (spacing * totalStates) / (2 * Math.PI);

                    // Diametro completo del círculo (incluye margen adicional)
                    int stateDiameter = 60; // Diámetro de cada estado (aproximado)
                    int diameter = (int) (2 * radius + spacing + 120); // 120 = margen adicional
                    diameter = spacing * (totalStates / 2) + 120;
                    // Redimensionar dinámicamente el panel
                    displayA.arrangeStatesInCircle(0, 50, spacing);
                    setPreferredSize(new Dimension(diameter, diameter+50));
                    revalidate();
                    drawAutomaton(g);
                }
            }
        };

        drawingPanel.setPreferredSize(new Dimension(800, 800));
        JScrollPane scrollPanedraw = new JScrollPane(drawingPanel);
        scrollPanedraw.setBounds(0, buttonY + buttonHeight + spacing, 600, 600 - buttonY - buttonHeight - spacing - 50);
        panelConFondo.add(scrollPanedraw);

        automataLabel = new JLabel("", SwingConstants.CENTER);
        automataLabel.setBounds(350, buttonY + buttonHeight + spacing + 50, 760, 400);
        automataLabel.setVerticalAlignment(SwingConstants.TOP);
        panelConFondo.add(automataLabel);

        automataLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        automataLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = automataLabel.getLocation().x;
                int thisY = automataLabel.getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                automataLabel.setLocation(X, Y);
            }
        });

        botonFuentes.addActionListener(e -> mostrarFuentes());

        botonRegresar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        botonCaptura.addActionListener(e -> capturarPantalla());

        botonERaAFD.addActionListener(e -> {
            String regex = JOptionPane.showInputDialog(this, "Ingrese una expresión regular:", "Generar Autómata",
                    JOptionPane.PLAIN_MESSAGE);
            if (!operations.isValidRegex(regex)) {
                JOptionPane.showMessageDialog(this, "La expresión regular no es válida.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            process.setLength(0);

            process.append(String.format("""
                    Logs/proceso de transformacion
                    %s
                    regex ->AFD-Lambda
                    \n
                    """, regex));

            displayA = operations.processRegexSA(regex, process);
            if (displayA == null) {
                JOptionPane.showMessageDialog(null, "Error al generar el autómata.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            displayA.convertToAFD();
            displayA.nameStates();
            // displayA.getFinalState().name = "final";
            displayA.displayAutomata(process);
            // displayA.arrangeStates(50, 10);

            // Update the automata label
            String processText = process.toString();
            int index = processText.indexOf("Se culmino el automata");
            if (index != -1) {
                processText = processText.substring(index + "Se culmino el automata".length());
            }
            automataLabel.setText("<html>" + processText.replace("\n", "<br>") + "</html>");

            drawingPanel.repaint();

        });

        botonPasos.addActionListener(e -> {
            JTextArea textArea = new JTextArea(process.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            if (process.length() < 3) {
                return;
            }
            JOptionPane.showMessageDialog(this, scrollPane, "Proceso de Transformación",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        botonCadena.addActionListener(e -> {
            if (displayA == null) {
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
                    boolean result = displayA.evaluateString(testString); // Suponiendo que tienes un método `evaluate`
                                                                          // en
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
                "<li>Hopcroft, J. E., Motwani, R., & Ullman, J. D. (2006). <i>Introduction to Automata Theory, Languages, and Computation</i>. Addison-Wesley.</li>"
                +
                "<li>Thompson, K. (1968). <i>Regular Expression Search Algorithm</i>. Communications of the ACM, 11(6), 419-422.</li>"
                +
                "<li>De Castro Korgi, R. (2004). <i>Teoría de la computación: Lenguajes, autómatas, gramáticas (1ª ed.)</i> [PDF]. Universidad Nacional de Colombia, Facultad de Ciencias. Impresión: UNIBIBLOS.</li>"
                +
                "</ol>" +
                "</body></html>";

        JEditorPane editorPane = new JEditorPane("text/html", fuentes);
        editorPane.setEditable(false);
        editorPane.setFont(new Font("Montserrat", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        Object[] options = { "ACEPTAR" };
        JOptionPane.showOptionDialog(this, scrollPane, "FUENTES", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    private void capturarPantalla() {
        try {
            // Crear una imagen del tamaño del drawingPanel
            BufferedImage imagen = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagen.createGraphics();
    
            // Pintar el contenido del drawingPanel en la imagen
            drawingPanel.paintAll(g2d);
            g2d.dispose();
    
            // Crear directorio si no existe
            File directorio = new File("Proyecto-Teoria-de-Automatas\\src\\proyecto\\teoria\\de\\automatas\\capturas");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
    
            // Guardar la imagen como archivo
            File archivo = new File(directorio, "captura_" + System.currentTimeMillis() + ".png");
            ImageIO.write(imagen, "png", archivo);
    
            // Mostrar mensaje de confirmación
            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("Captura guardada correctamente");
            panel.add(label, BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(400, 100));
    
            Object[] options = { "ACEPTAR" };
            JOptionPane.showOptionDialog(this, panel, "Captura de Pantalla", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la captura de pantalla.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void capturarPantallaDeprecated() {
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

            Object[] options = { "ACEPTAR" };
            JOptionPane.showOptionDialog(this, panel, "Captura de Pantalla", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la captura de pantalla.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawAutomaton(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; // Usar Graphics2D para mayor precisión
        State state = displayA.head;
    
        while (state != null) {
            int x = state.x, y = state.y;
            int radius = 30;
    
            // Dibujar flecha de inicio
            if (state == displayA.head) {
                g.drawLine(x - 40 - 20, y - 30, x - 15 - 20, y);
                g.drawLine(x - 40 - 20, y + 30, x - 15 - 20, y);
            }
    
            // Dibujar círculo del estado
            g.setColor(Color.WHITE);
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.setColor(Color.BLACK);
            g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
    
            // Dibujar círculo exterior si es final
            if (state.end)
                g.drawOval(x - radius - 5, y - radius - 5, 2 * radius + 10, 2 * radius + 10);
    
            // Dibujar nombre del estado
            g.drawString(state.name, x - 10, y + 5);
    
            // Dibujar transiciones
            TransF transition = state.transHead;
            Map<State, StringBuilder> selfTransitions = new HashMap<>();
    
            while (transition != null) {
                State target = transition.state;
    
                if (target == state) {
                    // Acumular caracteres de transiciones a sí mismo
                    selfTransitions.putIfAbsent(target, new StringBuilder());
                    selfTransitions.get(target).append(transition.path).append(", ");
                } else {
                    // Dibujar línea con dirección
                    int dx = target.x - x, dy = target.y - y;
                    double angle = Math.atan2(dy, dx);
                    int startX = x + (int) (radius * Math.cos(angle));
                    int startY = y + (int) (radius * Math.sin(angle));
                    int endX = target.x - (int) (radius * Math.cos(angle));
                    int endY = target.y - (int) (radius * Math.sin(angle));
                    g.drawLine(startX, startY, endX, endY);
    
                    // Dibujar flecha
                    int arrowSize = 10;
                    double arrowAngle = Math.PI / 6;
                    int arrowX1 = (int) (endX - arrowSize * Math.cos(angle - arrowAngle));
                    int arrowY1 = (int) (endY - arrowSize * Math.sin(angle - arrowAngle));
                    int arrowX2 = (int) (endX - arrowSize * Math.cos(angle + arrowAngle));
                    int arrowY2 = (int) (endY - arrowSize * Math.sin(angle + arrowAngle));
                    g.drawLine(endX, endY, arrowX1, arrowY1);
                    g.drawLine(endX, endY, arrowX2, arrowY2);
    
                    // Etiqueta de transición
                    int labelX = (startX + endX) / 2 - 10; // Más cerca del estado
                    int labelY = (startY + endY) / 2 - 10;
                    g.drawString(String.valueOf(transition.path), labelX, labelY);
                }
    
                transition = transition.next;
            }
    
            // Dibujar arcos para transiciones a sí mismo
            for (Map.Entry<State, StringBuilder> entry : selfTransitions.entrySet()) {
                State selfState = entry.getKey();
                String label = entry.getValue().toString().replaceAll(", $", ""); // Eliminar la última coma y espacio
    
                // Dibujar arco hacia la derecha del estado
                int arcX = selfState.x - radius;
                int arcY = selfState.y - radius - 40;
                int arcWidth = radius * 2;
                int arcHeight = radius * 2;
    
                g2.drawArc(arcX, arcY, arcWidth, arcHeight, 0, 360); // Arco completo
    
                // Etiqueta del arco
                g2.drawString(label, arcX + arcWidth / 2 - 10, arcY - 10);
            }
    
            state = state.next;
        }
    }
    

    private void drawAutomatonD(Graphics g) {

        State state = displayA.head;
        while (state != null) {

            int x = state.x, y = state.y;
            int radius = 30;

            // Dibujar flecha de inicio
            if (state == displayA.head) {
                // g.drawLine(x - 40-20, y, x - 15-20, y);
                g.drawLine(x - 40 - 20, y - 30, x - 15 - 20, y);
                g.drawLine(x - 40 - 20, y + 30, x - 15 - 20, y);
            }

            // Dibujar círculo del estado
            g.setColor(Color.WHITE);
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.setColor(Color.BLACK);
            g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

            // Dibujar círculo exterior si es final
            if (state.end)
                g.drawOval(x - radius - 5, y - radius - 5, 2 * radius + 10, 2 * radius + 10);

            // Dibujar nombre del estado
            g.drawString(state.name, x - 10, y + 5);

            // Dibujar transiciones
            TransF transition = state.transHead;
            Map<State, StringBuilder> selfTransitions = new HashMap<>();

            while (transition != null) {
                State target = transition.state;

                if (target == state) {
                    // Si es una transición a sí mismo, acumular el carácter
                    selfTransitions.putIfAbsent(target, new StringBuilder());
                    selfTransitions.get(target).append(transition.path).append(", ");
                } else {
                    // Dibujar transición normal
                    int dx = target.x - x, dy = target.y - y;
                    double angle = Math.atan2(dy, dx);
                    int startX = x + (int) (radius * Math.cos(angle));
                    int startY = y + (int) (radius * Math.sin(angle));
                    int endX = target.x - (int) (radius * Math.cos(angle));
                    int endY = target.y - (int) (radius * Math.sin(angle));
                    g.drawLine(startX, startY, endX, endY);

                    // Etiqueta de transición
                    int labelX = (startX + endX) / 2;
                    int labelY = (startY + endY) / 2;
                    g.drawString(String.valueOf(transition.path), labelX + 5, labelY - 5);
                }

                transition = transition.next;
            }

            // Dibujar arcos para las transiciones a sí mismo
            for (Map.Entry<State, StringBuilder> entry : selfTransitions.entrySet()) {
                State selfState = entry.getKey();
                String label = entry.getValue().toString().replaceAll(", $", ""); // Eliminar la última coma y espacio

                // Dibujar arco hacia la derecha del estado
                int arcX = selfState.x + radius;
                int arcY = selfState.y - radius / 2;
                int arcWidth = radius * 2;
                int arcHeight = radius;

                g.drawArc(arcX, arcY, arcWidth, arcHeight, 0, 180); // Arco hacia la derecha

                // Etiqueta del arco
                g.drawString(label, arcX + arcWidth / 2 - 10, arcY - 5);
            }

            state = state.next;
        }
    }

}