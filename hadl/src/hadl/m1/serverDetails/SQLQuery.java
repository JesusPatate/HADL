package hadl.m1.serverDetails;

import hadl.m2.Call;
import hadl.m2.Message;
import hadl.m2.connector.AtomicConnector;
import hadl.m2.connector.Role;

public class SQLQuery extends AtomicConnector {
    
    class SQLQuerySender extends Role {
        
        public SQLQuerySender() {
            super("sender");
        }
        
        @Override
        public void receive(Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if(service.equals("receiveDBResponse")) {
                if(this.attachment != null) {
                    this.attachment.send(this, msg);
                }
            }
            else {
                receiverRole.receive(msg);
            }
        }
    }
    
    class SQLQueryReceiver extends Role {
        
        public SQLQueryReceiver() {
            super("receiver");
        }
        
        @Override
        public void receive(Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if(service.equals("receiveDBResponse")) {
                SQLQuery.this.senderRole.receive(msg);
            }
            
            else if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    private final SQLQuerySender senderRole =
            new SQLQuerySender();
    
    private final SQLQueryReceiver receiverRole =
            new SQLQueryReceiver();
    
    public SQLQuery() {
        super("SQLQuery");
        this.addRole(this.senderRole);
        this.addRole(this.receiverRole);
    }
}
