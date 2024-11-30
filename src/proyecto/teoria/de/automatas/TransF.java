package proyecto.teoria.de.automatas;

public class TransF {
    public Character path;
    public String stateName;
    public State state;
    public TransF next;

    public TransF(char path,String state){
        this.path=path;
        this.stateName=state;
        next=null;

    }

    public TransF(char path,State state){
        this.path=path;
        this.state=state;
        next=null;

    }
    
}
