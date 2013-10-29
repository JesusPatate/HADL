package hadl.m2.component;

import hadl.m2.Link;


/**
 * A service required by a component.
 */
public abstract class RequiredService extends Service {
    
    protected Link connection = null;
    
    public RequiredService(final String label) {
        super(label);
    }
    
    @Override
    public void plug(final Link link) {
        this.connection = link;
    }
}
