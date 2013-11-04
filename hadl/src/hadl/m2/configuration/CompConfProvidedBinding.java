package hadl.m2.configuration;

import hadl.m2.component.ProvidedPort;


public class CompConfProvidedBinding extends Binding {
    
    public CompConfProvidedBinding(final String label, final ProvidedPort end1,
            final ProvidedPort end2) {
        
        super(label, end1, end2);
    }
}
