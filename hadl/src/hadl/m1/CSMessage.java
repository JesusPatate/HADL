package hadl.m1;

import hadl.m2.Message;

import java.util.StringTokenizer;


public class CSMessage implements Message {
    
    private final String header;
    
    private final String body;
    
    public CSMessage(final String header, final String body) {
        this.header = header;
        this.body = body;
    }
    
    @Override
    public String getHeader() {
        return this.header;
    }
    
    @Override
    public String getBody() {
        return this.body;
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
    
    @Override
    public String toString() {
        return this.header + ": " + this.body;
    }
    
}
