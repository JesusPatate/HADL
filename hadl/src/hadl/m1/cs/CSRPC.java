package hadl.m1.cs;

import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;


public class CSRPC extends AtomicConnector {
    
    /**
     * RPC's caller role
     */
    class Caller extends Role {
        
        public Caller(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            calleeRole.receive(msg, null);
        }
    }
    
    /**
     * RPC's callee role
     */
    class Callee extends Role {
        
        public Callee(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            this.attachment.send(this, msg);
        }
    }
    
    private final Caller callerRole = new Caller("caller");
    
    private final Callee calleeRole = new Callee("callee");
    
    public CSRPC(String label) {
        super(label);
        this.addRole(callerRole);
        this.addRole(calleeRole);
    }
}
