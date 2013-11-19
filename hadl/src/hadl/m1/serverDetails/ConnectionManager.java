package hadl.m1.serverDetails;

import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.RequiredPort;
import hadl.m2.service.ProvidedService;
import hadl.m2.service.RequiredService;


public class ConnectionManager extends AtomicComponent {
    
    class ReceiveRequest extends ProvidedService {
        
        public ReceiveRequest(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
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
        public void receive(Message msg) {
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
        public void receive(final Message msg) {
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
        public void receive(final Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
        
    }
    
    class ExternalSocketPro extends ProvidedPort {
        
        public ExternalSocketPro(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
            else if (this.binding != null) {
                this.binding.send(this, msg);
            }
        }
    }
    
    class ExternalSocketReq extends RequiredPort {
        
        public ExternalSocketReq(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
            else if (this.binding != null) {
                this.binding.send(this, msg);
            }
        }
    }
    
    class DBQuery extends RequiredPort {
        
        public DBQuery(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
            else if (this.binding != null) {
                this.binding.send(this, msg);
            }
        }
    }
    
    class SecurityQuery extends RequiredPort {
        
        public SecurityQuery(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
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
        this.addProvidedPort(new ExternalSocketPro("externalSocket"));
        this.addProvidedConnection("receiveRequest", "receiveRequest",
                "externalSocket");
        
        this.addRequiredService(this.securityAuth);
        this.addRequiredPort(new SecurityQuery("securityQuery"));
        this.addRequiredConnection("securityAuthorization",
                "securityAuthorization", "securityQuery");
        
        this.addRequiredService(this.handleQuery);
        this.addRequiredPort(new DBQuery("dbQuery"));
        this.addRequiredConnection("handleQuery", "handleQuery", "dbQuery");
    }
    
    private void getAuthorization(final Message msg) {
        int idx = msg.body.lastIndexOf(',');
        Message query = new Message("AUTH", msg.body.substring(0, idx));
        
        this.securityAuth.receive(query);
    }
}
