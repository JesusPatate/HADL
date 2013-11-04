package hadl.m2.configuration;

import hadl.m2.component.RequiredPort;


public class CompConfRequiredBinding extends Binding {
    
    public CompConfRequiredBinding(final String label, final RequiredPort end1,
            final RequiredPort end2) {
        
        super(label, end1, end2);
    }
}
