package proyecto.teoria.de.automatas;

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
    

    public boolean evaluateString(String input) {
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
}
