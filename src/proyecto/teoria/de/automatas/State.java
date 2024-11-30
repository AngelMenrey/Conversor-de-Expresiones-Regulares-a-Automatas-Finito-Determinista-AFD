package proyecto.teoria.de.automatas;

public class State {
    public int x, y;
    public String name;
    public TransF transHead;
    public State next;
    public boolean end;

    public State(String name, boolean end) {
        this.name = name;
        this.end = end;
        x = 0;
        y = 0;
        transHead = null;
        next = null;
    }

    public State(String name, boolean end, TransF transFunctions) {
        this.name = name;
        this.end = end;
        x = 0;
        y = 0;
        transHead = transFunctions;
        next = null;
    }

    /**
     * Añade una transición con un carácter, asegurándose de que no exista una igual.
     * 
     * @param path       Carácter de la transición.
     * @param stateName  Estado destino de la transición.
     */
    public void addTransition(Character path, State stateName) {
        if (existsTransition(path, stateName)) {
            System.out.println("Transición duplicada: '" + path + "' -> " + stateName.name + ". No se añade por que ya existe.");
            return;
        }

        TransF newTransition = new TransF(path, stateName);

        if (transHead == null) {
            transHead = newTransition;
        } else {
            TransF temp = transHead;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newTransition;
        }
    }

    /**
     * Añade una transición vacía (null), asegurándose de que no exista una igual.
     * 
     * @param stateName Estado destino de la transición.
     */
    public void addTransition(State stateName) {
        if (existsTransition(null, stateName)) {
            System.out.println("Transición duplicada: 'ε' -> " + stateName.name + ". No se añade por que ya existe.");
            return;
        }

        TransF newTransition = new TransF(stateName);

        if (transHead == null) {
            transHead = newTransition;
        } else {
            TransF temp = transHead;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newTransition;
        }
    }

    /**
     * Verifica si ya existe una transición con el mismo carácter y estado destino.
     * 
     * @param path      Carácter de la transición (puede ser null para vacías).
     * @param stateName Estado destino de la transición.
     * @return true si existe una transición idéntica, false en caso contrario.
     */
    private boolean existsTransition(Character path, State stateName) {
        TransF temp = transHead;
        while (temp != null) {
            if ((temp.path == null && path == null || temp.path != null && temp.path.equals(path)) &&
                temp.state==stateName) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
    public static void main(String[] args) {
        String s="12";
        System.out.println(s.length()+"  "+s.charAt(s.length()-1));
    }
}
