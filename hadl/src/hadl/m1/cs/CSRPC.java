package hadl.m1.cs;

import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.FromRole;
import hadl.m2.connector.ToRole;


public class CSRPC extends AtomicConnector {
    
    /**
     * RPC's caller role
     */
    class Caller extends FromRole {
        
        public Caller(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            calleeRole.receive(msg);
        }
    }
    
    /**
     * RPC's callee role
     */
    class Callee extends ToRole {
        
        public Callee(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
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
