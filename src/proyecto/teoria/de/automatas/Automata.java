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
        Automata automata = new Automata("Lambda Automata", "expresión regular");

        // Definir estados
        State s0 = new State("q0", false);
        State s1 = new State("q1", false);
        State s2 = new State("q2", true); // Estado de aceptación

        // Agregar estados al autómata
        automata.addState(s0);
        automata.addState(s1);
        automata.addState(s2);

        // Definir transiciones
        s0.addTransition('a', s1);                // Transición normal con 'a'
        s1.addTransition( s2);              // Transición lambda (null)
        s1.addTransition('b', automata.findStateByName("q1")); // Bucle en 'b'
        s2.addTransition('c', s2);               // Bucle en 'c'

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
