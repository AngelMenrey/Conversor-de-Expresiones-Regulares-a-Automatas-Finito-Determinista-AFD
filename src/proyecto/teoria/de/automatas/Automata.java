package proyecto.teoria.de.automatas;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Automata {
    public String name;
    public State head;
    public char[] alpha;
    public String regex;
    public boolean afnl = true;

    public void displayAutomata(StringBuilder output) {
        if (head == null) {
            System.out.println("El autómata está vacío.");
            return;
        }
        output.append("\n");

        State currentState = head;
        while (currentState != null) {
            // Imprimir el estado actual
            output.append(currentState.name + ":");

            // Recorrer las transiciones de este estado
            TransF transition = currentState.transHead;
            while (transition != null) {
                output.append(transition.path)
                        .append("->")
                        .append(transition.state.name)
                        .append(", ");
                transition = transition.next;
            }
            // Eliminar la coma y el espacio extra al final, si hay transiciones
            if (output.toString().endsWith(", ")) {
                output.setLength(output.length() - 2);
            }
            output.append("\n");

            // Mostrar el resultado
            //

            // Ir al siguiente estado
            currentState = currentState.next;
        }
        System.out.println(output);
    }

    // Funcion para determinar si hay un automata
    public void displayAutomata() {
        if (head == null) {
            System.out.println("El autómata está vacío.");
            return;
        }
        State currentState = head;
        while (currentState != null) {
            // Imprimir el estado actual
            StringBuilder output = new StringBuilder(currentState.name + ":");

            // Recorrer las transiciones de este estado
            TransF transition = currentState.transHead;
            while (transition != null) {
                output.append(transition.path)
                        .append("->")
                        .append(transition.state.name)
                        .append(", ");
                transition = transition.next;
            }

            // Eliminar la coma y el espacio extra al final, si hay transiciones
            if (output.toString().endsWith(", ")) {
                output.setLength(output.length() - 2);
            }

            // Mostrar el resultado
            System.out.println(output);

            // Ir al siguiente estado
            currentState = currentState.next;
        }
    }

    // Funcion para obtener el ultimo estado
    public State getFinalState() {
        if (!afnl) {
            return null;
        }
        State aux = head;

        while (aux != null) {
            if (aux.end) {
                return aux;
            }
            aux = aux.next;
        }
        return null;
    }

    public State getLastState() {

        if (head == null) {
            return head;
        }
        State aux = head;
        while (aux.next != null) {
            aux = aux.next;

        }
        return aux;
    }

    // Funcion para obtener el automata
    public Automata(String name, String regex) {
        this.name = name;
        this.regex = regex;
        head = null;
        // get_alpha_from_regex();
    }

    // Funcion para agregar estados
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

    // Funcion para nombrar estados
    public void nameStates() {
        if (head == null) {
            return;
        }

        int stateCount = 0;
        State currentState = head;

        while (currentState != null) {
            currentState.name = "q" + (stateCount++);
            if (currentState.end) {
                currentState.name = currentState.name + "F";
            }
            currentState = currentState.next;
        }

    }

    // Cantidad de estados regresados

    public int countStates(){
        int count=0;
        State currentState = head;
        while (currentState != null) {
            count++;
            currentState = currentState.next;
        }
        return count;

    }

    public void arrangeStatesInCircle(int x, int y, int circumference) {
        int totalStates = countStates();
        if (totalStates == 0) return;
    
        // Calcular el radio del círculo basado en la circunferencia de los estados
        double radius = (circumference * totalStates) / (2 * Math.PI);
    
        // Ángulo entre cada estado en radianes
        double angleStep = (2 * Math.PI) / totalStates;
    
        // Coordenadas iniciales (desplazamiento)
        int centerX = x+(int)radius*2;
        int centerY = y+(int)radius*2;
    
        // Iterar por los estados y asignar coordenadas
        State currentState = head;
        int index = 0;
        while (currentState != null) {
            // Calcular el ángulo actual
            double angle = index * angleStep;
    
            // Calcular las nuevas coordenadas
            int newX = centerX + (int) (radius * Math.cos(angle));
            int newY = centerY + (int) (radius * Math.sin(angle));
    
            // Asignar las coordenadas al estado
            currentState.x = newX;
            currentState.y = newY;
    
            // Pasar al siguiente estado
            currentState = currentState.next;
            index++;
        }
    }
    

    // Funcion para medir estados
    public void arrangeStates(int diameter, int rows) {
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

        // Calcular el número de columnas necesarias
        int cols = (int) Math.ceil((double) stateCount / rows);

        // Espaciado entre los centros de los círculos
        int spacing = diameter + 10; // 10 unidades de separación adicional

        // Asignar coordenadas (x, y) para cada estado
        currentState = head;
        int index = 0;

        while (currentState != null) {
            // Calcular fila y columna para el estado actual
            int row = index / cols; // División entera para obtener la fila
            int col = index % cols; // Resto para obtener la columna

            // Asignar coordenadas
            currentState.x = col * spacing; // Posición x basada en la columna
            currentState.y = row * spacing; // Posición y basada en la fila

            index++;
            currentState = currentState.next;
        }
    }

    public State findStateByName(String name) {
        State temp = head;
        while (temp != null) {
            if (temp.name.equals(name)) {
                return temp;
            }
            temp = temp.next;
        }
        // Stack<Automata> automatas=new Stack<Automata>();
        return null;
    }

    public boolean evaluateString(String input) {
        return evaluateRecursively(head, input, 0, new HashSet<>(), input);
    }

    private boolean evaluateRecursively(State currentState, String input, int index, Set<State> visited,
            String buffer) {
        // Si ya visitamos este estado, evitamos ciclos
        if (visited.contains(currentState)) {
            return false;
        }

        // Si hemos consumido todo el buffer, verificamos si estamos en un estado final
        // o seguimos con lambdas
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

    // --------------------------------------
    public Set<State> findStates(Character input,State state) {
        
        Set<State> seti = new HashSet<>();
        findStateRecursively(seti, state, input, 0, new HashSet<>(), "");
        return seti;

    }

    private boolean findStateRecursively(Set<State> save, State currentState, Character input, int index,
            Set<State> visited, String buffer) {
        // Si ya visitamos este estado, evitamos ciclos
        if (visited.contains(currentState)) {
            return false;
        }

        // Obtener el carácter actual del buffer si no estamos en una transición lambda
        char currentChar = input;

        // Explorar transiciones normales
        TransF transition = currentState.transHead;
        if (transition==null) {
            return false;
            
        }

        visited.add(currentState);
        while (transition != null) {
            if (transition.path != null && transition.path == currentChar) { // Transición normal

                if (!save.contains(transition.state)) {
                    save.add(transition.state);
                    exploreLambda(save, new HashSet<>(), transition.state);
                }

            }
            if (transition.path == null) { // Transición lambda
                // Pasar el buffer completo

                findStateRecursively(save, transition.state, input, index, new HashSet<>(visited), buffer);

            }
            transition = transition.next;

        }

        // Si ninguna transición lleva a aceptación, se rechaza
        return false;
    }

    public void exploreLambda(Set<State> save, Set<State> visited, State currentState) {
        // Si ya visitamos este estado, evitamos ciclos
        if (visited.contains(currentState)) {
            return;
        }
        visited.add(currentState);
        if (!save.contains(currentState)) {
            save.add(currentState);
        }

        TransF transition = currentState.transHead;
        if (transition==null) {
            return;
            
        }

        while (transition != null) {
            if (transition.path == null) { // Transición lambda
                // Pasar el buffer completo
                exploreLambda(save, new HashSet<>(visited), transition.state);
            }
            transition = transition.next;
        }

    }
    public Set<Character> getAlpha(){
        
        Set<Character> alphabet = new HashSet<>();

        State s=head;
        TransF t;
        while (s!=null) {
            t=s.transHead;
            while (t!=null) {
                if (t.path!=null) {
                    if (!alphabet.contains(t.path)) {
                        alphabet.add(t.path);
                    }
                }
                t=t.next;
            }

            s=s.next;
        }
        return alphabet;
    }

    public void convertToAFN() {
        if (!afnl) {
            System.out.println("El autómata ya es un AFN.");
            return;
        }

        State finalS=getFinalState();
        
        Set<State> setL = new HashSet<>();
        exploreLambda(setL, new HashSet<>(), head);
        if (setL.contains(finalS)) {
            head.end=true;
        }
    
        // Paso 1: Extraer el alfabeto
        Set<Character> alphabet = getAlpha();
    
        // Paso 2: Crear un hash map global para todas las transiciones
        Map<State, Map<Character, Set<State>>> globalTransitionsMap = new HashMap<>();
    
        // Paso 3: Para cada estado, construir su mapa de transiciones
        State currentState = head;
        while (currentState != null) {
            Map<Character, Set<State>> stateTransitionsMap = new HashMap<>();
    
            // Para cada carácter en el alfabeto, obtener el conjunto de estados alcanzables
            for (Character c : alphabet) {
                Set<State> reachableStates = findStates(c, currentState);
                stateTransitionsMap.put(c, reachableStates);
            }
    
            // Guardar el mapa de transiciones de este estado en el mapa global
            globalTransitionsMap.put(currentState, stateTransitionsMap);
    
            // Ir al siguiente estado
            currentState = currentState.next;
        }
    
        // Paso 4: Eliminar todas las transiciones originales de los estados
        currentState = head;
        while (currentState != null) {
            currentState.transHead = null; // Eliminar transiciones
            currentState = currentState.next;
        }
    
        // Paso 5: Crear nuevas transiciones a partir del mapa global
        currentState = head;
        while (currentState != null) {
            Map<Character, Set<State>> stateTransitionsMap = globalTransitionsMap.get(currentState);
    
            for (Map.Entry<Character, Set<State>> entry : stateTransitionsMap.entrySet()) {
                Character c = entry.getKey();
                Set<State> reachableStates = entry.getValue();
    
                for (State targetState : reachableStates) {
                    currentState.addTransition(c, targetState);
                }
            }
    
            // Ir al siguiente estado
            currentState = currentState.next;
        }
    
        // Actualizar el flag para indicar que el autómata ya no tiene lambdas
        afnl = false;
        System.out.println("El autómata ha sido convertido a un AFN.");
    }

    //------------------------------------------------------
    public void convertToAFD() {
    if (!afnl) {
        System.out.println("El autómata ya es un AFN.");
        return;
    }

    State finalS = getFinalState();

    // Validar si el estado inicial tiene conexión lambda al final
    Set<State> lambdaClosure = new HashSet<>();
    exploreLambda(lambdaClosure, new HashSet<>(), head);
    if (lambdaClosure.contains(finalS)) {
        head.end = true;
    }

    // Paso 1: Extraer el alfabeto
    Set<Character> alphabet = getAlpha();

    // Paso 2: Crear un hash map global para transiciones de los estados actuales
    Map<State, Map<Character, Set<State>>> globalTransitionsMap = new HashMap<>();
    State currentState = head;

    // Construir transiciones del mapa global
    while (currentState != null) {
        Map<Character, Set<State>> stateTransitionsMap = new HashMap<>();
        for (Character c : alphabet) {
            Set<State> reachableStates = findStates(c, currentState);
            stateTransitionsMap.put(c, reachableStates);
        }
        globalTransitionsMap.put(currentState, stateTransitionsMap);
        currentState = currentState.next;
    }

    // Paso 3: Construir el nuevo autómata
    Automata newAutomaton = new Automata("auxiliar","");
    Map<Set<State>, State> stateSetMapping = new HashMap<>();
    Queue<Set<State>> stateQueue = new LinkedList<>();
    Set<State> initialSet = lambdaClosure;

    // Crear estado inicial en el nuevo autómata
    State newInitialState = new State("newStart", containsFinalState(initialSet));
    newAutomaton.head=newInitialState;
    newInitialState.end = containsFinalState(initialSet);
    stateSetMapping.put(initialSet, newInitialState);
    stateQueue.add(initialSet);

    // Procesar estados
    while (!stateQueue.isEmpty()) {
        Set<State> currentSet = stateQueue.poll();
        State currentNewState = stateSetMapping.get(currentSet);

        // Crear transiciones para cada carácter
        for (Character c : alphabet) {
            Set<State> combinedSet = new HashSet<>();
            for (State s : currentSet) {
                Set<State> transitions = globalTransitionsMap.get(s).get(c);
                if (transitions != null) {
                    combinedSet.addAll(transitions);
                }
            }

            if (!combinedSet.isEmpty()) {
                if (!stateSetMapping.containsKey(combinedSet)) {
                    State newState = new State("s", containsFinalState(combinedSet));
                    newAutomaton.addState(newState);
                    stateSetMapping.put(combinedSet, newState);
                    stateQueue.add(combinedSet);
                }

                currentNewState.addTransition(c, stateSetMapping.get(combinedSet));
            }
        }
    }

    // Paso 4: Reemplazar el autómata actual con el nuevo
    this.head = newAutomaton.head;
    this.afnl = false;
    System.out.println("El autómata ha sido convertido a un AFD.");
}

// Helper: Verifica si un conjunto de estados contiene un estado final
private boolean containsFinalState(Set<State> stateSet) {
    for (State state : stateSet) {
        if (state.end) {
            return true;
        }
    }
    return false;
}

    


    public static void main(String[] args) {
        Automata a = new Automata("newAutomata", null);
        Operations o = new Operations();
        StringBuilder log = new StringBuilder("");
        // a=o.processRegexSA("", log);
        State s0 = new State("s", false);
        State s1 = new State("s", false);
        State s2 = new State("s", false);
        State s3 = new State("s", false);
        State s4 = new State("s", false);
        State s5 = new State("s", false);
        State s6 = new State("s", false);
        State s7 = new State("s", false);
        State s8 = new State("s", false);
        State s9 = new State("s", true);
        s0.addTransition('0', s1);
        s0.addTransition(s2);
        s2.addTransition('1', s3);
        s1.addTransition(s4);
        s1.addTransition(s5);
        s3.addTransition(s6);
        s6.addTransition(s7);
        s3.addTransition(s8);
        a.addState(s0);
        a.addState(s1);
        a.addState(s2);
        a.addState(s3);
        a.addState(s4);
        a.addState(s5);
        a.addState(s6);
        a.addState(s7);
        a.addState(s8);
        a.displayAutomata();
        //(a|b|c*(a|v)*)
        a=o.processRegexSA("(a|b|c*(a|v)*)", log);
        System.out.println("");
        a.displayAutomata();
        a.convertToAFD();
        System.out.println("");
        a.nameStates();
        a.displayAutomata();
        Set<Character> alfa =a.getAlpha();
        System.out.println("alfabeto");
        for(Character chara:alfa){
            System.out.println(chara);
        }

        Set<State> states = a.findStates('a',a.head); // Ejemplo con el carácter 'a'
        System.out.println("Estados alcanzables:");
        //for (State state : states) {
        //    System.out.println(state.name); // Imprime el nombre de cada estado
        //}

        //states = a.findStates('b',a.head); // Ejemplo con el carácter 'a'
        System.out.println("Estados alcanzables:");
        //for (State state : states) {
        //    System.out.println(state.name); // Imprime el nombre de cada estado
        //}

    }
}
