package fr.univnantes.alma.hadl.m2;

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
    protected final String label;
    
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
