package hadl.m1.serverDetails;

import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.Port;
import hadl.m2.component.ProvidedService;
import hadl.m2.component.RequiredService;


public class ConnectionManager extends AtomicComponent {
    
    class ReceiveRequest extends ProvidedService {
        
        public ReceiveRequest(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            System.out.println("Le gestionnaire de connection reçoit : " + msg);
            
            if (msg.header.contentEquals("QUERY")) {
                getAuthorization(msg);
            }
        }
    }
    
    class ReceiveResponse extends RequiredService {
        
        public ReceiveResponse(final String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    class HandleQuery extends RequiredService {
        
        public HandleQuery(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    class SecurityAuthorization extends RequiredService {
        
        public SecurityAuthorization(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
        
    }
    
    class ExternalSocketPro extends Port {
        
        public ExternalSocketPro(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            // TODO Router le message
        }
    }
    
    class ExternalSocketReq extends Port {
        
        public ExternalSocketReq(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
            else if (this.binding != null) {
                this.binding.send(this, msg);
            }
        }
    }
    
    class DBQuery extends Port {
        
        public DBQuery(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
            else if (this.binding != null) {
                this.binding.send(this, msg);
            }
        }
    }
    
    class SecurityQuery extends Port {
        
        public SecurityQuery(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
            else if (this.binding != null) {
                this.binding.send(this, msg);
            }
        }
    }
    
    private final ReceiveRequest receiveRequest =
            new ReceiveRequest("receiveRequest");
    
    private final HandleQuery handleQuery = new HandleQuery("handleQuery");
    
    private final SecurityAuthorization securityAuth =
            new SecurityAuthorization("securityAuthorization");
    
    public ConnectionManager(String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        this.addProvidedService(this.receiveRequest);
        this.addPort(new ExternalSocketPro("externalSocket"));
        this.addConnection("receiveRequest", "receiveRequest",
                "externalSocket");
        
        this.addRequiredService(this.securityAuth);
        this.addPort(new SecurityQuery("securityQuery"));
        this.addConnection("securityAuthorization",
                "securityAuthorization", "securityQuery");
        
        this.addRequiredService(this.handleQuery);
        this.addPort(new DBQuery("dbQuery"));
        this.addConnection("handleQuery", "handleQuery", "dbQuery");
    }
    
    private void getAuthorization(final Message msg) {
        int idx = msg.body.lastIndexOf(',');
        Message query = new Message("AUTH", msg.body.substring(0, idx));
        
        this.securityAuth.receive(query, null);
    }
}
