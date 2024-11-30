package proyecto.teoria.de.automatas;

import java.util.Stack;

import javax.swing.JOptionPane;

public class Operations {

    public Automata concatSA(Automata a1, Automata a2){
        if (a1 == null || a2 == null) {
            return null;
        }
        State a1Final=a1.getFinalState();
        a1Final.end=false;
        a1Final.addTransition(a2.head);
        a1.addState(a2.head);
        return a1;
    }

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
        result.addState(a2.head);
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

    public Automata processRegexSA(String regex,StringBuilder logs) {
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
                logs.append("\nse encontro ')'");

                
                boolean entered=false;

                while (!operators.isEmpty() && operators.peek() != '(') {
                    processSA(operators, automatas,logs);
                    if (!entered) {
                        entered=true;
                    }
                }
                if (!entered) {
                    logs.append("\nno hay operadores en este parentesis por lo que puede contener unautomata o estar vacio");
                    if (regex.charAt(i-1)!='(') {
                        logs.append("\nexiste dentro del parentesis, automata:"+automatas.peek().name);
                    }
                }
                operators.pop(); // Quitar '('
                
                logs.append("\nse cerro '('");
                if (i+1<regex.length()&&(0==precedence(regex.charAt(i+1))||regex.charAt(i+1)=='(')) {
                    operators.push('.');
                }
                
            } else if (c == '*') {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    processSA(operators, automatas,logs);
                }
                operators.push(c);
            }else if(c == '|'){
                operators.push(c);
            }else if(c == '.'){
                operators.push(c);
            } else {
                // Crear autómata para un símbolo y añadir al stack
                Automata a=createSA(c);
                while(i+1<regex.length()&&(0==precedence(regex.charAt(i+1))&&('('!=regex.charAt(i+1)))
                &&(i+2<regex.length()&&(0==precedence(regex.charAt(i+2)))&&('('!=regex.charAt(i+2)))
                &&regex.charAt(i+1)!=')'){
                    i++;
                    c=regex.charAt(i);
                    //if (i+1<regex.length()&&(0==precedence(regex.charAt(i+1)))) {
                        addToSA(a, c);
                    //}

                    

                }
                automatas.push(a);
                logs.append("\nSe creo el automata simple:"+a.name);
                if (i+1<regex.length()&&(0==precedence(regex.charAt(i+1)))&&regex.charAt(i+1)!=')'
                &&regex.charAt(i+1)!='(') {
                    operators.push('.');
                
                    i++;
                    c=regex.charAt(i);
                    
                    a=createSA(c);
                    automatas.push(a);
                    
                logs.append("\nSe creo el automata simple:"+a.name+"separado por que algo lo opera");

                }else if (i+1<regex.length()
                &&regex.charAt(i+1)=='(') {
                  
                    operators.push('.');  
                }

                

                /*
                 * Automata single = new Automata("Symbol_" + c, String.valueOf(c));
                State start = new State("q_start", false);
                State end = new State("q_end", true);
                start.addTransition(c, end);
                single.addState(start);
                single.addState(end);
                single.head = start;
                automatas.push(single);
                 */
                

            }
        }

        // Procesar el resto de la pila
        while (!operators.isEmpty()) {
            processSA(operators, automatas,logs);
        }
        
        logs.append("\nSe culmino el automata");
        return automatas.pop();
    }

    public Automata createSA(char c){

        Automata a=new Automata("sa_"+c, null);

        State s1=new State("s", false);
        State s2=new State("s", true);
        s1.addTransition(c, s2);
        a.addState(s1);
        a.addState(s2);
        return a;

    }

    public void addToSA(Automata a,char c){
        
        State s2=new State("s", false);
        a.addState(s2);
        State oldF=a.getFinalState();
        oldF.addTransition(c,s2);
        oldF.end=false;
        s2.end=true;
        a.name=a.name+c;


    }

    public Automata unionSA(Automata a1,Automata a2){
        State newStart=new State("s", false);

        newStart.addTransition(a1.head);
        newStart.addTransition(a2.head);
        State olda1FState=a1.getFinalState();
        State olda2FState=a2.getFinalState();

        if (olda1FState==null || olda2FState==null) {
            return null;
        }

        State newF=new State("sf", true);
        olda1FState.addTransition(newF);
        olda2FState.addTransition(newF);
        olda1FState.end=false;
        olda2FState.end=false;
        
        a1.addState(a2.head);
        a1.addState(newF);
        newStart.next=a1.head;

        a1.head=newStart;
        //a1.nameStates();
        return a1;
    }

    public Automata kleenSA(Automata a){
        State finals=a.getFinalState();
        if (finals==null) {
            return null;
        }
        a.head.addTransition(finals);
        finals.addTransition(a.head);
        a.name=a.name+"_kleen";

        return a;
    }

    public Automata plusKleenSA(Automata a){
        State finals=a.getFinalState();
        if (finals==null) {
            return null;
        }
        finals.addTransition(a.head);


        return a;
    }

    // Procesar autómatas según el operador
    private void processAutomata(Stack<Character> operators, Stack<Automata> automatas) {
        char operator = operators.pop();

        if (operator == '*') {
            Automata a = automatas.pop();
            automatas.push(kleeneStar(a));
            System.out.println("op: *, a:"+a.name);
        } else if (operator == '|') {
            Automata a2 = automatas.pop();
            Automata a1 = automatas.pop();
            automatas.push(union(a1, a2));
            System.out.println("op: |, a1:"+a1.name+", a1:"+a2.name);
        } else if (operator == '.') {
            Automata a2 = automatas.pop();
            Automata a1 = automatas.pop();
            automatas.push(concatSA(a1, a2));
            System.out.println("op: |, a1:"+a1.name+", a1:"+a2.name);
        }
    }

    private void processSA(Stack<Character> operators, Stack<Automata> automatas,StringBuilder logs) {
        char operator = operators.pop();

        if (operator == '*') {
            
            Automata a = automatas.pop();
            logs.append("\noperacion: *, a:"+a.name);
            automatas.push(kleenSA(a));
            
            
            System.out.println("op: *, a:"+a.name);
        } else if (operator == '|') {
            Automata a2 = automatas.pop();
            Automata a1 = automatas.pop();
            logs.append("\noperacion: |, a1:"+a1.name+", a2:"+a2.name);
            automatas.push(unionSA(a1, a2));
            
            //System.out.println("op: |, a1:"+a1.name+", a2:"+a2.name);
        } else if (operator == '.') {
            Automata a2 = automatas.pop();
            Automata a1 = automatas.pop();
            logs.append("\nop: ., a1:"+a1.name+", a2:"+a2.name);
            automatas.push(concate(a1, a2));
            //System.out.println("op: ., a1:"+a1.name+", a2"+a2.name);
        }else{
            Automata a=automatas.peek();
            logs.append("\nno se encontro una operacion valida pero si existe el automata:"+a.name);
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

        StringBuilder logs=new StringBuilder("");

        // Generar el autómata
        Automata automata = operations.processRegexSA(regex,logs);
        if (automata == null) {
            JOptionPane.showMessageDialog(null, "Error al generar el autómata.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        automata.nameStates();
        automata.getFinalState().name="final";
        System.out.println(logs);
        automata.displayAutomata();

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
