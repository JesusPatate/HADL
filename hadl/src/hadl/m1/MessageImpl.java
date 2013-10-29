package hadl.m1;

import hadl.m2.Message;


public class MessageImpl implements Message {
    
    private final String content;
    
    public MessageImpl(final String content) {
        this.content = content;
    }
    
    @Override
    public String format() {
        return this.content;
    }
    
    @Override
    public String toString() {
        return this.format();
    }
}
