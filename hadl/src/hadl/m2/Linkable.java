package hadl.m2;

public interface Linkable {
    
    /**
     * Plugs the linkable entity to a link.
     * 
     * @param link The link to plug
     */
    void plug(final Link link);
    
    /**
     * Delivers a message to the linkable entity.
     * 
     * @param msg The message to deliver
     */
    void receive(final Message msg);
}
