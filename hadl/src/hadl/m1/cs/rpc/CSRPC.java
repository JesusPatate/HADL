package hadl.m1.cs.rpc;

import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.FromRole;
import hadl.m2.connector.ToRole;


public class CSRPC extends AtomicConnector {
    
    class Caller extends FromRole {
        
        public Caller(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            System.out.println("DBG Le role " + this.label +
                    " reçoit : " + msg); // DBG
            
            calleeRole.receive(msg);
        }
    }
    
    class Callee extends ToRole {
        
        public Callee(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            System.out.println("DBG Le role " + this.label +
                    " reçoit : " + msg); // DBG
            
            this.attachment.send(this, msg);
        }
    }
    
    private final Caller callerRole = new Caller("caller");
    
    private final Callee calleeRole = new Callee("callee");
    
    public CSRPC(String label) {
        super(label);
        this.addFromRole(callerRole);
        this.addToRole(calleeRole);
    }
}
