package hadl.m1.serverDetails;

import hadl.m1.Call;
import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;


public class ClearanceQuery extends AtomicConnector {
    
    class ClearanceQuerySender extends Role {
        
        public ClearanceQuerySender() {
            super("sender");
        }
        
        @Override
        public void receive(Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if(service.equals("receiveAuthorization")) {
                if(this.attachment != null) {
                    this.attachment.send(this, msg);
                }
            }
            else {
                receiverRole.receive(msg);
            }
        }
    }
    
    class ClearanceQueryReceiver extends Role {
        
        public ClearanceQueryReceiver() {
            super("receiver");
        }
        
        @Override
        public void receive(Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if(service.equals("receiveAuthorization")) {
                ClearanceQuery.this.senderRole.receive(msg);
            }
            
            else if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    private final ClearanceQuerySender senderRole =
            new ClearanceQuerySender();
    
    private final ClearanceQueryReceiver receiverRole =
            new ClearanceQueryReceiver();
    
    public ClearanceQuery(String label) {
        super(label);
        this.addRole(this.senderRole);
        this.addRole(this.receiverRole);
    }
}
