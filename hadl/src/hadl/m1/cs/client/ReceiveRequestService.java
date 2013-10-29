package hadl.m1.cs.client;

import hadl.m2.Message;
import hadl.m2.component.RequiredService;


public class ReceiveRequestService extends RequiredService {
    
    public ReceiveRequestService(final String label) {
        super(label);
    }
    
    @Override
    public void receive(final Message msg) {
        System.out.println("DBG Le service " + this.label + " reçoit : " + msg); // DBG
        
        this.connection.send(this, msg);
    }
}
