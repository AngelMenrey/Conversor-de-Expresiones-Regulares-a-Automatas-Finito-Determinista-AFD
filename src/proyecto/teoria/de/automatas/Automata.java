package proyecto.teoria.de.automatas;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.swing.JOptionPane;

public class Automata {
    public String name;
    public State head;
    public char[] alpha;
    public String regex;
    public boolean afnl=true;

    public State getFinalState(){
        if(!afnl){
            return null;
        }
        State aux=head;

        while (aux!=null) {
            if (aux.end) {
                return aux;
            }
            aux=aux.next;
        }
        return null;
    }

    public State getLastState(){

        if (head==null) {
            return head;
        }
        State aux=head;
        while (aux.next!=null) {
            aux=aux.next;
            
        }
        return aux;
    }

    
    
    public Automata(String name,String regex){
        this.name=name;
        this.regex=regex;
        head=null;
        get_alpha_from_regex();
    }

    public void addState(State newState) {
        if (head == null) {
            head = newState;
        } else {
            // Verificar si el nombre ya existe
            String originalName = newState.name;
            int counter = 1;
            while (findStateByName(newState.name) != null) {
                newState.name = originalName + "_" + counter;
                counter++;
            }
    
            // Añadir el estado al final de la lista
            State temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newState;
        }
    }

    public void nameStates(){
        if (head == null) {
            return;
        }

        int stateCount = 0;
        State currentState = head;

        while (currentState != null) {
            currentState.name="q"+(stateCount++);
            if (currentState.end) {
                currentState.name=currentState.name+"F";
            }
            currentState = currentState.next;
        }

    }

    public void arrangeStates(int diameter) {
        if (head == null) {
            return;
        }

        // Contar el número de estados
        int stateCount = 0;
        State currentState = head;
        while (currentState != null) {
            stateCount++;
            currentState = currentState.next;
        }

        // Determinar el ángulo para espaciar los estados de forma circular
        double angleStep = 2 * Math.PI / stateCount;
        currentState = head;
        int index = 0;

        // Asignar coordenadas (x, y) a cada estado
        while (currentState != null) {
            double angle = angleStep * index;
            currentState.x = (int) (diameter * Math.cos(angle)); // Cálculo de coordenada X
            currentState.y = (int) (diameter * Math.sin(angle)); // Cálculo de coordenada Y
            index++;
            currentState = currentState.next;
        }
    }
    

    public boolean evaluateStringAFD(String input) {
        State currentState = head;
        int i = 0;
        
        while (currentState != null && i < input.length()) {
            char currentChar = input.charAt(i);
            TransF transition = currentState.transHead;
            boolean foundTransition = false;

            while (transition != null) {
                if (transition.path == currentChar) {
                    currentState = findStateByName(transition.stateName);
                    currentState=transition.state;
                    foundTransition = true;
                    break;
                }
                transition = transition.next;
            }

            if (!foundTransition) {
                return false;
            }
            i++;
        }
        
        return currentState != null && currentState.end;
    }

    public State findStateByName(String name) {
        State temp = head;
        while (temp != null) {
            if (temp.name.equals(name)) {
                return temp;
            }
            temp = temp.next;
        }
        //Stack<Automata> automatas=new Stack<Automata>();
        return null;
    }






    //TODO terminar funcion no usar funcion todavia
    public void get_alpha_from_regex(){
        if (regex.isEmpty()||regex==null||regex.isBlank()) {
            return;
        }
        int group=0;
        for(int i=0;i<regex.length();i++){
            if (Character.isLetterOrDigit(regex.charAt(i) )) {
                
            }else if (regex.charAt(i)=='(') {
             group++;
            }else if (regex.charAt(i)==')') {
                group--;
            }else if (regex.charAt(i)=='^') {
                if (regex.charAt(i+1)=='*') {
                    
                }
            }
        }
        
    }
    //esta clase se va a usar para evaluar expreciones regulares

    public static void main(String[] args) {
        // Crear el autómata con transiciones lambda
        Automata automata = new Automata("Lambda Automata", "((a|b)(a|b)a(a|b)*b(a|b))|((b|a)(b|a)*b(b|a)*a(b|a))*");

        // Definir estados
        State s0 = new State("q0", false); // Estado inicial
        State s1 = new State("q1", false);
        State s2 = new State("q2", false);
        State s3 = new State("q3", false);
        State s4 = new State("q4", false);
        State s5 = new State("q5", false);
        State s6 = new State("q6", false);
        State s7 = new State("q7", false);
        State s8 = new State("q8", true); // Estado final de aceptación

        // Agregar estados al autómata
        automata.addState(s0);
        automata.addState(s1);
        automata.addState(s2);
        automata.addState(s3);
        automata.addState(s4);
        automata.addState(s5);
        automata.addState(s6);
        automata.addState(s7);
        automata.addState(s8);

        // Definir transiciones para la primera parte ((a|b)(a|b)a(a|b)*b(a|b))
        s0.addTransition( s1); // Lambda para elegir primera parte
        s1.addTransition('a', s2);
        s1.addTransition('b', s2);
        s2.addTransition('a', s3);
        s2.addTransition('b', s3);
        s3.addTransition('a', s4);
        s4.addTransition('a', s4); // Bucle en (a|b)*
        s4.addTransition('b', s5);
        s5.addTransition('a', s6);
        s5.addTransition('b', s6);
        s6.addTransition( s8); // Lambda al estado final

        // Definir transiciones para la segunda parte ((b|a)(b|a)*b(b|a)*a(b|a))*
        s0.addTransition( s7); // Lambda para elegir segunda parte
        s7.addTransition('b', s7);
        s7.addTransition('a', s7);
        s7.addTransition('b', s7); // Bucle en (b|a)*
        s7.addTransition('b', s7);
        s7.addTransition('a', s7);
        s7.addTransition( s8); // Lambda al estado final

        // Definir bucles lambda para combinaciones de estados
        s7.addTransition( s1); // Segunda parte puede regresar a la primera
        // Ciclo de pruebas con JOptionPane
        while (true) {
            String input = JOptionPane.showInputDialog(
                null,
                "Introduce una cadena para probar el autómata (o escribe 'salir' para terminar):",
                "Prueba de Automata",
                JOptionPane.QUESTION_MESSAGE
            );

            // Salir si el usuario cancela o escribe "salir"
            if (input == null || input.equalsIgnoreCase("salir")) {
                JOptionPane.showMessageDialog(
                    null,
                    "Programa finalizado. ¡Gracias por probar!",
                    "Salir",
                    JOptionPane.INFORMATION_MESSAGE
                );
                break;
            }

            // Evaluar la cadena
            boolean isAccepted = automata.evaluateString(input);

            // Mostrar resultado
            JOptionPane.showMessageDialog(
                null,
                "Cadena: \"" + input + "\"\nResultado: " + (isAccepted ? "Aceptada" : "Rechazada"),
                "Resultado de la Evaluación",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }



    public static void mainD(String[] args) {
        Automata automata = new Automata("nombre del automata", "expresion regular");

        State s0 = new State("q0", false);
        State s1 = new State("q1", true);

        automata.addState(s0);
        automata.addState(s1);

        s0.addTransition('a', automata.findStateByName("q0"));
        s0.addTransition('b', automata.findStateByName("q1"));
        s1.addTransition('b', automata.findStateByName("q1"));
        s1.addTransition('a', automata.findStateByName("q0"));

        

        String testString = "aaba";
        boolean isAccepted = automata.evaluateString(testString);
        String m1="Cadena " + testString + " aceptada: " + isAccepted;
        testString = "aab";
        isAccepted = automata.evaluateString(testString);
        
        String m2="Cadena " + testString + " aceptada: " + isAccepted;

        String[] mensajes = {m1,m2
        };

        for (String mensaje : mensajes) {
            int option = JOptionPane.showOptionDialog(
                null,
                mensaje,
                "Información",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Siguiente"},
                "Siguiente"
            );

            if (option != JOptionPane.OK_OPTION) {
                break; // Si se cierra el diálogo, termina la secuencia
            }
        }

        JOptionPane.showMessageDialog(
            null,
            m1+"\n"+m2,
            "Información",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    public boolean evaluateString(String input) {
        return evaluateRecursively(head, input, 0, new HashSet<>(), input);
    }
    
    private boolean evaluateRecursively(State currentState, String input, int index, Set<State> visited, String buffer) {
        // Si ya visitamos este estado, evitamos ciclos
        if (visited.contains(currentState)) {
            return false;
        }
        
    
        // Si hemos consumido todo el buffer, verificamos si estamos en un estado final o seguimos con lambdas
        if (index == input.length()) {
            if (currentState.end) {
                return true; // Estado de aceptación
            }
            visited.add(currentState);
    
            // Explorar transiciones lambda
            TransF lambdaTransition = currentState.transHead;
            while (lambdaTransition != null) {
                if (lambdaTransition.path == null) { // Transición lambda
                    if (evaluateRecursively(lambdaTransition.state, input, index, new HashSet<>(visited), buffer)) {
                        return true;
                    }
                }
                lambdaTransition = lambdaTransition.next;
            }
    
            return false;
        }
    
        // Obtener el carácter actual del buffer si no estamos en una transición lambda
        char currentChar = input.charAt(index);
    
        // Explorar transiciones normales
        TransF transition = currentState.transHead;
        while (transition != null) {
            if (transition.path != null && transition.path == currentChar) { // Transición normal
                // Consumir carácter y explorar
                
                if (evaluateRecursively(transition.state, input, index + 1, new HashSet<>(), buffer.substring(1))) {
                    return true;
                }
            }
            transition = transition.next;
        }
        visited.add(currentState);
    
        // Explorar transiciones lambda
        transition = currentState.transHead;
        while (transition != null) {
            if (transition.path == null) { // Transición lambda
                // Pasar el buffer completo
                if (evaluateRecursively(transition.state, input, index, new HashSet<>(visited), buffer)) {
                    return true;
                }
            }
            transition = transition.next;
        }
    
        // Si ninguna transición lleva a aceptación, se rechaza
        return false;
    }
    
    

}

/*
 * Cadenas aceptadas:
abaa
babab
ababaa
baaaa
abababab
Cadenas no aceptadas:
abc (contiene caracteres no válidos)
aaa (no cumple con la estructura)
babba (estructura incorrecta)
aaaa (estructura incorrecta)
b (demasiado corta)
 */