package hadl.m2.configuration;

import hadl.m2.Link;
import hadl.m2.component.Port;


public abstract class Binding extends Link {
    
    private final String label;
    
    public Binding(final String label, final Port end1, final Port end2) {
        super(end1, end2);
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
}
