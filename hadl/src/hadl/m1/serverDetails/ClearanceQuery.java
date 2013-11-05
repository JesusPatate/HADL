package hadl.m1.serverDetails;

import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;


public class ClearanceQuery extends AtomicConnector {
    
    class ClearanceQuerySender extends Role {
        
        public ClearanceQuerySender(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            receiverRole.receive(msg, null);
        }
    }
    
    class ClearanceQueryReceiver extends Role {
        
        public ClearanceQueryReceiver(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    private final ClearanceQuerySender senderRole =
            new ClearanceQuerySender("sender");
    
    private final ClearanceQueryReceiver receiverRole =
            new ClearanceQueryReceiver("receiver");
    
    public ClearanceQuery(String label) {
        super(label);
        this.addRole(this.senderRole);
        this.addRole(this.receiverRole);
    }
}
