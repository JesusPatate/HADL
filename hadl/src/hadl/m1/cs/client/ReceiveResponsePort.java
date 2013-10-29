package hadl.m1.cs.client;

import hadl.m2.Message;
import hadl.m2.component.ProvidedPort;


public class ReceiveResponsePort extends ProvidedPort {
    
    public ReceiveResponsePort(final String label) {
        super(label);
    }
    
    @Override
    public void receive(final Message msg) {
        this.connection.send(this, msg);
    }
    
}
