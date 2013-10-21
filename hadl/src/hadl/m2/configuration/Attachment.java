package hadl.m2.configuration;


/**
 * An attachment link of a configuration.
 */
abstract class Attachment {
    
    private final String label;
    
    public Attachment(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
}
