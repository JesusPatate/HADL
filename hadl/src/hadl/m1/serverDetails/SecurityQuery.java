package hadl.m1.serverDetails;

import hadl.m1.Call;
import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;


public class SecurityQuery extends AtomicConnector {
    
    class SecurityQuerySender extends Role {
        
        public SecurityQuerySender() {
            super("sender");
        }
        
        @Override
        public void receive(Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if(service.equals("receiveCredentialsAuthentication")) {
                if(this.attachment != null) {
                    this.attachment.send(this, msg);
                }
            }
            else {
                receiverRole.receive(msg);
            }
        }
    }
    
    class SecurityQueryReceiver extends Role {
        
        public SecurityQueryReceiver() {
            super("receiver");
        }
        
        @Override
        public void receive(Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if(service.equals("receiveCredentialsAuthentication")) {
                SecurityQuery.this.senderRole.receive(msg);
            }
            
            else if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    private final SecurityQuerySender senderRole =
            new SecurityQuerySender();
    
    private final SecurityQueryReceiver receiverRole =
            new SecurityQueryReceiver();
    
    public SecurityQuery(String label) {
        super(label);
        this.addRole(this.senderRole);
        this.addRole(this.receiverRole);
    }
}
