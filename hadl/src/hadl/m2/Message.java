package hadl.m2;

public class Message {
    
    public final String header;
    
    public final String content;
    
    public Message(final String header, final String content) {
        this.header = header;
        this.content = content;
    }
    
    public String toString() {
        return String.format("%s:%s", this.header, this.content);
    }
}
