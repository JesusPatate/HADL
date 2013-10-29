package hadl.m1.cs.server;

import hadl.m2.Message;
import hadl.m2.component.RequiredPort;


public class ReceiveResponsePort extends RequiredPort {
    
    public ReceiveResponsePort(final String label) {
        super(label);
    }
    
    @Override
    public void receive(Message msg) {
        System.out.println("DBG Le port " + this.label + " reçoit : " + msg); // DBG
    }
    
}
