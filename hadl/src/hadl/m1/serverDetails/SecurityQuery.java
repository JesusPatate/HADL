package hadl.m1.serverDetails;

import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.FromRole;
import hadl.m2.connector.ToRole;


public class SecurityQuery extends AtomicConnector {
    
    class SecurityQuerySender extends FromRole {
        
        public SecurityQuerySender(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            receiverRole.receive(msg);
        }
    }
    
    class SecurityQueryReceiver extends ToRole {
        
        public SecurityQueryReceiver(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    private final SecurityQuerySender senderRole =
            new SecurityQuerySender("sender");
    
    private final SecurityQueryReceiver receiverRole =
            new SecurityQueryReceiver("receiver");
    
    public SecurityQuery(String label) {
        super(label);
        this.addFromRole(this.senderRole);
        this.addToRole(this.receiverRole);
    }
}
