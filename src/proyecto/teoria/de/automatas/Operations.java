package proyecto.teoria.de.automatas;

import java.util.Stack;

import javax.swing.JOptionPane;

public class Operations {

    // Concatenación de dos autómatas con un solo estado final
    public Automata concate(Automata a1, Automata a2) {
        if (a1 == null || a2 == null) {
            return null;
        }

        Automata result = new Automata("ConcatAutomata", a1.regex + a2.regex);
        State finalState1 = a1.getFinalState();
        State finalState2 = a2.getFinalState();

        if (finalState1 != null) {
            finalState1.end = false; // Deja de ser final
            finalState1.addTransition( a2.head); // Transición vacía hacia el segundo autómata
        }

        State newFinal = new State("q_final", true);
        if (finalState2 != null) {
            finalState2.end = false; // Deja de ser final
            finalState2.addTransition( newFinal); // Conexión al nuevo estado final
        }

        result.head = a1.head; // El inicio del autómata es el del primero
        result.addState(newFinal);
        return result;
    }

    // Unión de dos autómatas con un solo estado final
    public Automata union(Automata a1, Automata a2) {
        if (a1 == null || a2 == null) {
            return null;
        }

        Automata result = new Automata("UnionAutomata", a1.regex + "|" + a2.regex);
        State newStart = new State("q_start", false);
        State newFinal = new State("q_final", true);

        // Conexión del nuevo inicio a los inicios de ambos autómatas
        newStart.addTransition( a1.head);
        newStart.addTransition( a2.head);

        // Conexión de los finales actuales al nuevo estado final
        State finalState1 = a1.getFinalState();
        State finalState2 = a2.getFinalState();

        if (finalState1 != null) {
            finalState1.end = false;
            finalState1.addTransition( newFinal);
        }

        if (finalState2 != null) {
            finalState2.end = false;
            finalState2.addTransition( newFinal);
        }

        result.addState(newStart);
        result.addState(newFinal);
        return result;
    }

    // Kleene star para un solo autómata con un estado final único
    public Automata kleeneStar(Automata automata) {
        if (automata == null || automata.head == null) {
            return null;
        }

        Automata result = new Automata("KleeneStar", automata.regex + "*");

        State newStart = new State("q_start", false);
        State newFinal = new State("q_final", true);

        State finalState = automata.getFinalState();
        if (finalState != null) {
            finalState.end = false;
            finalState.addTransition( automata.head); // Ciclo
            finalState.addTransition( newFinal); // Conexión al nuevo final
        }

        newStart.addTransition( automata.head); // Al original
        newStart.addTransition(newFinal); // Al vacío

        result.addState(newStart);
        result.addState(newFinal);
        return result;
    }

    // Función para procesar una expresión regular
    public Automata processRegex(String regex) {
        if (regex == null || regex.isEmpty()) {
            return null;
        }

        Stack<Character> operators = new Stack<>();
        Stack<Automata> automatas = new Stack<>();

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    processAutomata(operators, automatas);
                }
                operators.pop(); // Quitar '('
            } else if (c == '*' || c == '|' || c == '.') {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    processAutomata(operators, automatas);
                }
                operators.push(c);
            } else {
                // Crear autómata para un símbolo y añadir al stack
                Automata single = new Automata("Symbol_" + c, String.valueOf(c));
                State start = new State("q_start", false);
                State end = new State("q_end", true);
                start.addTransition(c, end);
                single.addState(start);
                single.addState(end);
                single.head = start;
                automatas.push(single);
            }
        }

        // Procesar el resto de la pila
        while (!operators.isEmpty()) {
            processAutomata(operators, automatas);
        }

        return automatas.pop();
    }

    // Procesar autómatas según el operador
    private void processAutomata(Stack<Character> operators, Stack<Automata> automatas) {
        char operator = operators.pop();

        if (operator == '*') {
            Automata a = automatas.pop();
            automatas.push(kleeneStar(a));
        } else if (operator == '|') {
            Automata a2 = automatas.pop();
            Automata a1 = automatas.pop();
            automatas.push(union(a1, a2));
        } else if (operator == '.') {
            Automata a2 = automatas.pop();
            Automata a1 = automatas.pop();
            automatas.push(concate(a1, a2));
        }
    }

    // Obtener precedencia de operadores
    private int precedence(char c) {
        switch (c) {
            case '*':
                return 3;
            case '.':
                return 2;
            case '|':
                return 1;
            default:
                return 0;
        }
    }

    public boolean isValidRegex(String regex) {
        if (regex == null || regex.isEmpty()) return false;

        int openParens = 0;
        char prev = 0;

        for (char c : regex.toCharArray()) {
            if (c == '(') openParens++;
            else if (c == ')') {
                if (openParens == 0) return false; // Paréntesis de cierre sin apertura
                openParens--;
            } else if (c == '*' || c == '+') {
                if (prev == '*' || prev == '+' || prev == '|') return false; // Operadores consecutivos no válidos
            } else if (c == '|') {
                if (prev == '|' || prev == '(') return false; // OR no puede seguir a otro OR o a '('
            }
            prev = c;
        }

        return openParens == 0; // Todos los paréntesis deben cerrarse
    }

    // Método main
    public static void main(String[] args) {
        Operations operations = new Operations();

        // Pedir expresión regular
        String regex = JOptionPane.showInputDialog(null, "Ingrese una expresión regular:", "Generar Autómata", JOptionPane.PLAIN_MESSAGE);

        // Validar expresión regular
        if (!operations.isValidRegex(regex)) {
            JOptionPane.showMessageDialog(null, "La expresión regular no es válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generar el autómata
        Automata automata = operations.processRegex(regex);
        if (automata == null) {
            JOptionPane.showMessageDialog(null, "Error al generar el autómata.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ciclo para probar cadenas
        boolean continuar = true;
        while (continuar) {
            String testString = JOptionPane.showInputDialog(null, "Ingrese una cadena para evaluar (o escriba 'salir' para terminar):", "Probar Autómata", JOptionPane.PLAIN_MESSAGE);

            if (testString == null || testString.equalsIgnoreCase("salir")) {
                continuar = false;
            } else {
                boolean result = automata.evaluateString(testString); // Suponiendo que tienes un método `evaluate` en Automata
                String message = result ? "La cadena es aceptada por el autómata." : "La cadena no es aceptada por el autómata.";
                JOptionPane.showMessageDialog(null, message, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(null, "Gracias por usar el programa.", "Fin", JOptionPane.INFORMATION_MESSAGE);
    }
}
