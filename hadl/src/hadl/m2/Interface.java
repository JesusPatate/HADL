package hadl.m2;

public abstract class Interface implements Linkable {
    
    private final String label;
    
    private Link link;
    
    protected Interface(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void plug(final Link link) {
    	this.link = link;
    }
}
