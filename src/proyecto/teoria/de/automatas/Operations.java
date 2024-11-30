package proyecto.teoria.de.automatas;

import java.util.Stack;

public class Operations {

    public Automata concate(Stack<Automata> automatas) { 
        if (automatas == null || automatas.isEmpty() || automatas.size() < 2) {
            return null; // No se puede concatenar con menos de 2 autómatas
        }
    
        // Sacamos el primer automata para iniciar la concatenación
        Automata result = automatas.pop();
    
        while (!automatas.isEmpty()) {
            Automata next = automatas.pop();
    
            // Obtener el estado final del primer automata
            State finalState = result.getFinalState();
            if (finalState != null) {
                finalState.end = false; // Deja de ser final al unirse con otro autómata
                finalState.addTransition( next.head); // Conexión con transición vacía (lambda)
            }
    
            // Conectar los estados del segundo autómata al primero
            State lastState = result.getLastState();
            if (lastState != null) {
                lastState.next = next.head; // Enlazar la lista de estados
            }
        }
    
        return result;
    }

    public Automata union(Stack<Automata> automatas) { 
        if (automatas == null || automatas.isEmpty()) {
            return null; // No se puede realizar unión sin autómatas
        }
    
        Automata result = new Automata("UnionAutomata", ""); // Crear autómata para la unión
        State newStart = new State("q_start", false); // Nuevo estado inicial
        State newFinal = new State("q_final", true); // Nuevo estado final
    
        // Procesar cada autómata en el stack
        while (!automatas.isEmpty()) {
            Automata current = automatas.pop();
            if (current.head != null) {
                // Conectar el nuevo inicio a los inicios de cada autómata con transición lambda
                newStart.addTransition( current.head);
    
                // Encontrar los estados finales del autómata actual y conectarlos al nuevo final
                State finalState = current.getFinalState();
                if (finalState != null) {
                    finalState.end = false; // Dejar de marcarlo como final
                    finalState.addTransition( newFinal); // Transición lambda hacia el nuevo estado final
                }
            }
        }
    
        // Establecer el nuevo estado inicial y agregar el nuevo estado final al autómata
        //result.head = newStart;
        result.addState(newStart);
        result.addState(newFinal);
    
        return result;
    }
    


    public Automata kleeneStar(Automata automata) {
        if (automata == null || automata.head == null) {
            return null;
        }
    
        Automata result = new Automata("KleeneStar", automata.regex + "*");
    
        State newStart = new State("q_start", false);
        State newEnd = new State("q_end", true);
    
        State oldFinal = automata.getFinalState();
        if (oldFinal != null) {
            oldFinal.end = false; // Ya no es final
            oldFinal.addTransition( automata.head); // Ciclo para repeticiones
            oldFinal.addTransition( newEnd); // Conexión al nuevo estado final
        }
    
        newStart.addTransition( automata.head); // Conexión al autómata original
        newStart.addTransition( newEnd); // Conexión al estado final para permitir vacío
    
        result.head= newStart;
        result.addState(newEnd);
    
        return result;
    }

    public Automata kleenePlus(Automata automata) {
        if (automata == null || automata.head == null) {
            return null;
        }
    
        Automata result = new Automata("KleenePlus", automata.regex + "+");
    
        State newStart = new State("q_start", false);
        State newEnd = new State("q_end", true);
    
        State oldFinal = automata.getFinalState();
        if (oldFinal != null) {
            oldFinal.end = false; // Ya no es final
            oldFinal.addTransition( automata.head); // Ciclo para repeticiones
            oldFinal.addTransition(newEnd); // Conexión al nuevo estado final
        }
    
        newStart.addTransition(null, automata.head); // Conexión al autómata original
    
        result.addState(newStart);
        result.addState(newEnd);
    
        return result;
    }

    
}
