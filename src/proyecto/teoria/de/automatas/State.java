package proyecto.teoria.de.automatas;

public class State {
    public int x,y;
    public String name;
    public TransF transHead;
    public State next;
    public boolean end;
    public State(String name, boolean end){
        this.name=name;
        this.end=end;
        x=0;
        y=0;
        transHead=null;
        next=null;
    }
    public State(String name, boolean end,TransF transFunctions){
        this.name=name;
        this.end=end;
        x=0;
        y=0;
        transHead=transFunctions;
        next=null;
    }

    public void addTransition(Character path, State stateName) {
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
    public void addTransition(State s2) {
        // TODO Auto-generated method stub

        TransF newTransition = new TransF( s2);

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
}
