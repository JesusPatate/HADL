package hadl.m2;

public abstract class Interface implements Linkable {
    
    protected final String label;
    
    protected Interface(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
}
