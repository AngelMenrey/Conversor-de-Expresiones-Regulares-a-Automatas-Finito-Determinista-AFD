package proyecto.teoria.de.automatas;

public class TransF {
    public char path;
    public String state;
    public TransF next;

    public TransF(char path,String state){
        this.path=path;
        this.state=state;
        next=null;

    }
    
}
