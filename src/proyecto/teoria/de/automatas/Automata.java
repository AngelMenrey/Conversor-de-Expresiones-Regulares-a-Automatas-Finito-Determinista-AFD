package proyecto.teoria.de.automatas;

public class Automata {
    public String name;
    public State head;
    public char[] alpha;
    public String regex;
    
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
                    currentState = findStateByName(transition.state);
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

    private State findStateByName(String name) {
        State temp = head;
        while (temp != null) {
            if (temp.name.equals(name)) {
                return temp;
            }
            temp = temp.next;
        }
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
    private class chnode{
        public char sign;
        public chnode next;
        public chnode(char sign){
            this.sign=sign;
            next=null;
        }
    }

    public static void main(String[] args) {
        Automata automata = new Automata("SimpleAutomata", "a*b");

        State s0 = new State("q0", false);
        State s1 = new State("q1", true);

        s0.addTransition('a', "q0");
        s0.addTransition('b', "q1");
        s1.addTransition('b', "q1");
        s1.addTransition('a', "q0");

        automata.addState(s0);
        automata.addState(s1);

        String testString = "aaba";
        boolean isAccepted = automata.evaluateString(testString);
        System.out.println("Cadena " + testString + " aceptada: " + isAccepted);
        testString = "aab";
        isAccepted = automata.evaluateString(testString);
        System.out.println("Cadena " + testString + " aceptada: " + isAccepted);
    }
}
