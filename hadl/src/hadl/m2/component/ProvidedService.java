package hadl.m2.component;

import hadl.m2.Link;


/**
 * A service provided by a component.
 */
public abstract class ProvidedService extends Service {
    
    protected Link connection = null;
    
    public ProvidedService(final String label) {
        super(label);
    }
    
    @Override
    public void plug(final Link link) {
        this.connection = link;
    }
}
