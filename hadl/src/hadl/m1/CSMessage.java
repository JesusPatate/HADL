package hadl.m1;

import hadl.m2.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class CSMessage implements Message {
    
    public enum Part {
        HEADER, BODY
    };
    
    private final Map<String, Object> header;
    
    private final Map<String, Object> body;
    
    /**
     * Creates a new empty message.
     * 
     * @param id
     *            The message identifier
     */
    public CSMessage(final String id) {
        this.header = new HashMap<String, Object>();
        this.body = new HashMap<String, Object>();
        
        this.header.put("id", id);
    }
    
    /**
     * Creates a shallow copy of a message.
     * 
     * @param msg
     *            The message to copy
     */
    public CSMessage(final CSMessage msg) {
        this.header = new HashMap<String, Object>(msg.header);
        this.body = new HashMap<String, Object>(msg.body);
    }
    
    @Override
    public String getHeader() {
        StringBuffer buf = new StringBuffer("[");
        
        for (Entry<String, Object> e : this.header.entrySet()) {
            buf.append("\"" + e.getKey() + "\":" + e.getValue() + ",");
        }
        
        buf.deleteCharAt(buf.length() - 1);
        buf.append("]");
        
        return buf.toString();
    }
    
    @Override
    public String getBody() {
        StringBuffer buf = new StringBuffer("[");
        
        for (Entry<String, Object> e : this.body.entrySet()) {
            buf.append("\"" + e.getKey() + "\":" + e.getValue() + ",");
        }
        
        buf.deleteCharAt(buf.length() - 1);
        buf.append("]");
        
        return buf.toString();
    }
    
    /**
     * Returns message element.
     * 
     * @param part
     *            Part of the message where to fetch the element (HEADER or
     *            BODY).
     * @param label
     *            Label of the requested element.
     * 
     * @return The value associated to the given label.
     */
    public Object getElement(final Part part, final String label) {
        Object value = null;
        
        if (part == Part.HEADER) {
            value = this.header.get(label);
        }
        else if (part == Part.BODY) {
            value = this.body.get(label);
        }
        
        return value;
    }
    
    public void addHeaderElement(final String label, final Object value) {
        this.header.put(label, value);
    }
    
    public void addBodyElement(final String label, final Object value) {
        this.body.put(label, value);
    }
    
    @Override
    public String toString() {
        return this.getHeader() + " : " + this.getBody();
    }
    
}
