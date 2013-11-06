package hadl.m1.cs;

import hadl.m1.Call;
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
        public void receive(Message msg) {
            if(msg.getHeader().equals("CALL")) {
                Call call = (Call) msg;
                String service = call.getCalledService();
                
                if(service.equals("receiveRequest")) {
                    calleeRole.receive(msg);
                }
                
                else if (service.equals("receiveResponse")) {
                    if(this.attachment != null) {
                        this.attachment.send(this, msg);
                    }
                }
            }
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
        public void receive(Message msg) {
            if(msg.getHeader().equals("CALL")) {
                Call call = (Call) msg;
                String service = call.getCalledService();
                
                if(service.equals("receiveRequest")) {
                    if(this.attachment != null) {
                        this.attachment.send(this, msg);
                    }
                }
                
                else if (service.equals("receiveResponse")) {
                    callerRole.receive(msg);
                }
            }
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
