package hadl.m2.component;

import hadl.m2.Interface;
import hadl.m2.Link;
import hadl.m2.configuration.Attachment;
import hadl.m2.configuration.CompConfBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Port extends Interface {
    
    protected Map<String, Connection> providedServices =
            new HashMap<String, Connection>();
    
    protected List<String> requiredServices = new ArrayList<String>();
    
    protected Attachment attachment = null;
    
    protected CompConfBinding binding = null;
    
    /**
     * Creates a new port.
     * 
     * @param label
     *            Name of the port
     */
    public Port(final String label) {
        super(label);
    }
    
    @Override
    public void plug(final Link link) {
        if (link instanceof Connection) {
            Connection con = (Connection) link;
            this.providedServices
                    .put(con.getConnectedService().getLabel(), con);
        }
        else if (link instanceof Attachment) {
            this.attachment = (Attachment) link;
        }
        else if (link instanceof CompConfBinding) {
            this.binding = (CompConfBinding) link;
        }
    }
    
    /**
     * Connects a required service to the port.
     * 
     * @param service
     *            Service to connect
     */
    public void connectRequiredService(final String service) {
        this.requiredServices.add(service);
    }
    
    /**
     * Removes a connection between the port and a service.
     * 
     * @param service
     *            Service to disconnect
     */
    public void disconnect(final String service) {
        this.providedServices.remove(service);
        this.requiredServices.remove(service);
    }
    
    /**
     * Returns whether a service is linked to the port.
     * 
     * @param service
     *            A service
     * 
     * @return true if the service is linked to the port, false otherwise
     */
    public boolean linked(final String service) {
        boolean res = false;
        
        res = this.providedServices.containsKey(service);
        
        if (res == false) {
            res = this.requiredServices.contains(service);
        }
        
        return res;
    }
    
    /**
     * Returns the link that enables to reach a service.
     * 
     * @param service
     *            Service to reach
     * 
     * @return The link that connects the port to the given service or null if
     *         it doesn't exist.
     */
    protected Link getLink(final String service) {
        Link link = null;
        
        if (this.providedServices.containsKey(service)) {
            link = this.providedServices.get(service);
        }
        else if (this.requiredServices.contains(service)) {
            link = this.attachment;
        }
        
        return link;
    }
}
