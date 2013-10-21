package hadl.m2;

import java.util.HashMap;
import java.util.Map;


/**
 * An architectural basic element.
 * 
 * <p>
 * Superclass of components, connectors and configurations. Each element is
 * identified by a label and can have functional and non-functional properties.
 * </p>
 */
public abstract class ArchitecturalElement {
    
    /**
     * Name of the element.
     */
    private final String label;
    
    /**
     * Functional properties of the element.
     */
    private final Map<String, FunctionalProperty> functionalProperties =
            new HashMap<String, FunctionalProperty>();
    
    /**
     * Non-functional properties of the element.
     */
    private final Map<String, NonFunctionalProperty> nonFunctionalProperties =
            new HashMap<String, NonFunctionalProperty>();
    
    protected ArchitecturalElement(final String label) {
        this.label = label;
    }
    
    /**
     * Returns the name of the element
     * 
     * @return The element label
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Returns a functional property of the element.
     * 
     * @param label
     *            Name of the sought property
     * 
     * @return The functional property with the given name or null.
     */
    public FunctionalProperty getFunctionalProperty(final String label) {
        return this.functionalProperties.get(label);
    }
    
    /**
     * Returns a non-functional property of the element.
     * 
     * @param label
     *            Name of the sought property
     * 
     * @return The non-functional property with the given name or null.
     */
    public NonFunctionalProperty getNonFunctionalProperty(final String label) {
        return this.nonFunctionalProperties.get(label);
    }
    
    /**
     * Returns all functional properties of the element.
     * 
     * @return A map in wich properties are indexed using their name.
     */
    public Map<String, FunctionalProperty> getFunctionalProperties() {
        return new HashMap<String, FunctionalProperty>(
                this.functionalProperties);
    }
    
    /**
     * Returns all non-functional properties of the element.
     * 
     * @return A map in wich properties are indexed using their name.
     */
    public Map<String, NonFunctionalProperty> getNonFunctionalProperties() {
        return new HashMap<String, NonFunctionalProperty>(
                this.nonFunctionalProperties);
    }
}
