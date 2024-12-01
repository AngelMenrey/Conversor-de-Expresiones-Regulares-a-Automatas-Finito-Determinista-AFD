package proyecto.teoria.de.automatas;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class determinate {
    

    public Set<Character> generateAlphabet(Automata a) {
        Set<Character> alphabet = new HashSet<>();
        State currentState = a.head;
    
        while (currentState != null) {
            TransF transition = currentState.transHead;
            while (transition != null) {
                if (transition.path != null) { // Ignorar transiciones lambda
                    alphabet.add(transition.path);
                }
                transition = transition.next;
            }
            currentState = currentState.next;
        }
    
        return alphabet;
    }

    public Set<State> getLambdaClosure(State state) {
    Set<State> lambdaClosure = new HashSet<>();
    Stack<State> stack = new Stack<>();

    lambdaClosure.add(state);
    stack.push(state);

    while (!stack.isEmpty()) {
        State current = stack.pop();
        TransF transition = current.transHead;

        while (transition != null) {
            if (transition.path == null && !lambdaClosure.contains(transition.state)) {
                lambdaClosure.add(transition.state);
                stack.push(transition.state);
            }
            transition = transition.next;
        }
    }

    return lambdaClosure;
}

public Set<State> getTransitionsForChar(State state, char character) {
    Set<State> resultStates = new HashSet<>();
    Set<State> lambdaClosure = getLambdaClosure(state);

    for (State s : lambdaClosure) {
        TransF transition = s.transHead;

        while (transition != null) {
            if (transition.path != null && transition.path == character) {
                resultStates.add(transition.state);
            }
            transition = transition.next;
        }
    }

    return resultStates;
}

public void convertAFNLToAFN(Automata a) {
    Set<Character> alphabet = generateAlphabet(a);
    State currentState = a.head;

    while (currentState != null) {
        Map<Character, Set<State>> newTransitions = new HashMap<>();

        // Calcular nuevas transiciones para cada carácter del alfabeto
        for (char c : alphabet) {
            Set<State> transitions = getTransitionsForChar(currentState, c);
            newTransitions.put(c, transitions);
        }

        // Eliminar las transiciones actuales
        currentState.transHead = null;

        // Establecer las nuevas transiciones sin lambda
        for (Map.Entry<Character, Set<State>> entry : newTransitions.entrySet()) {
            char c = entry.getKey();
            for (State target : entry.getValue()) {
                currentState.addTransition(c, target);
            }
        }

        currentState = currentState.next;
    }

    // Eliminar transiciones lambda
    a.afnl = false;
}

    
}
