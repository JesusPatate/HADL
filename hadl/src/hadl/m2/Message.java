package hadl.m2;


public interface Message {
    
    /**
     * Returns message header.
     * 
     * @return Message type as a String.
     */
    public String getHeader();
    
    /**
     * Returns message body.
     * 
     * @return Message data as a String.
     */
    public String getBody();
}
