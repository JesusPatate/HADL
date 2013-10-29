package hadl.m1.cs.server;

import hadl.m2.Message;
import hadl.m2.component.ProvidedPort;


public class SendResponsePort extends ProvidedPort {
    
    public SendResponsePort(final String label) {
        super(label);
    }
    
    @Override
    public void receive(final Message msg) {
        System.out.println("DBG Le port " + this.label + " reçoit : " + msg); // DBG
        
        if (this.attachment != null) {
            this.attachment.send(this, msg);
        }
    }
    
}
