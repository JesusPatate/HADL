package hadl.m1.serverDetails;

import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;


public class SecurityQuery extends AtomicConnector {
    
    class SecurityQuerySender extends Role {
        
        public SecurityQuerySender(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            receiverRole.receive(msg, null);
        }
    }
    
    class SecurityQueryReceiver extends Role {
        
        public SecurityQueryReceiver(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
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
        this.addRole(this.senderRole);
        this.addRole(this.receiverRole);
    }
}
