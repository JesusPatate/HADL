package hadl.m1.cs.client;

import hadl.m2.Message;
import hadl.m2.component.ProvidedPort;


public class SendRequestPort extends ProvidedPort {
    
    public SendRequestPort(final String label) {
        super(label);
    }
    
    @Override
    public void receive(final Message msg) {
        System.out.println("DBG Le port " + this.label + " reçoit : " + msg); // DBG
        
        if (this.connection != null) {
            this.connection.send(this, msg);
        }
    }
}
