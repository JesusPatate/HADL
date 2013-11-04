package hadl.m2;

import java.util.StringTokenizer;

public class Message {
    
    public final String header;
    
    public final String body;
    
    public Message(final String header, final String body) {
        this.header = header;
        this.body = body;
    }
    
    public String getBodyElement(final int index) {
        String elt = "";
        
        StringTokenizer tok = new StringTokenizer(this.body, "'");
        boolean stop = false;
        int i = 0;
        
        // Ignore login
        while (tok.hasMoreTokens() && (i < index)) {
            tok.nextToken(); // login
            tok.nextToken(); // coma
            ++i;
        }
        
        while (stop == false && tok.hasMoreTokens()) {
            elt += tok.nextToken();
            
            if (elt.endsWith("\\") == false) {
                stop = true;
            }
            else {
                elt += "'";
            }
        }
        
        return elt;
    }
    
    public String toString() {
        return String.format("%s:%s", this.header, this.body);
    }
}
