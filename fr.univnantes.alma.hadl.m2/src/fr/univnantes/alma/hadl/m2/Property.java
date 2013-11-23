package fr.univnantes.alma.hadl.m2;

/**
 * A property of an architecural element.
 * 
 * <p>
 * Abstract superclass of functional and non-functional properties. Any
 * architectural element can be enriched with properties so as to refine its
 * representation.
 * </p>
 */
public abstract class Property {
    
    /**
     * Property name
     */
    private final String label;
    
    /**
     * Property value
     */
    private final Object value;
    
    protected Property(final String label, final Object value) {
        this.label = label;
        this.value = value;
    }
    
    /**
     * Returns the name of the property.
     * 
     * @return The name of the property.
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Returns the value of the property.
     * 
     * @return The value of the property.
     */
    public Object getValue() {
        return this.value;
    }
}
