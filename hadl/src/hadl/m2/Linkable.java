package hadl.m2;

public interface Linkable {
    
    void plug(final Link link);
    
    /**
     * Delivers a message to the linkable entity.
     * 
     * @param msg The message to deliver
     * @param link The link that delivers the message
     */
    void receive(final Message msg, final Link link);
}
