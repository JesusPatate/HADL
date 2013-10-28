package hadl.m1.cs;

import hadl.m2.Message;
import hadl.m2.component.ProvidedPort;


public class SendRequestPort extends ProvidedPort {
    
    public SendRequestPort(final String label) {
        super(label);
    }
    
    @Override
    public void receive(Message msg) {
        this.attachment.send(this, msg);
    }
}
