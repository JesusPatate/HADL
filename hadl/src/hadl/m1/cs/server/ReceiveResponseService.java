package hadl.m1.cs.server;

import hadl.m2.Message;
import hadl.m2.component.RequiredService;


public class ReceiveResponseService extends RequiredService {
    
    public ReceiveResponseService(final String label) {
        super(label);
    }
    
    @Override
    public void receive(final Message msg) {
        System.out.println("DBG Le service " + this.label + " reçoit : " + msg); // DBG
        
        if (this.connection != null) {
            this.connection.send(this, msg);
        }
    }
    
}
