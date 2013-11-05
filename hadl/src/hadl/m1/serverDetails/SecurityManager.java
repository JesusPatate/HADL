package hadl.m1.serverDetails;

import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.Port;
import hadl.m2.component.ProvidedService;
import hadl.m2.component.RequiredService;


public class SecurityManager extends AtomicComponent {
    
    class SecurityAuthService extends ProvidedService {
        
        public SecurityAuthService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            System.out.println("Le gestionnaire de sécurité reçoit : " + msg);
            
            if (msg.header.contentEquals("AUTH")) {
                final String login = msg.getBodyElement(0);
                final String pwd = msg.getBodyElement(1);
                
                credentialQueryService.requestDatabase(login, pwd);
            }
        }
    }
    
    class CredentialQueryService extends RequiredService {
        
        public CredentialQueryService(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            
        }
        
        public void requestDatabase(final String login, final String pwd) {
            Message query = new Message("CREDQUERY",
                    "\'" + login + "\',\'" + pwd + "\'");
            
            System.out.println("Le gestionnaire de sécurité envoie : " + query); // DBG
            
            if (this.connection != null) {
                this.connection.send(this, query);
            }
        }
    }
    
    class SecurityAuthorization extends Port {
        
        public SecurityAuthorization(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            // XXX Router le message
        }
    }
    
    class CredentialQueryPort extends Port {
        
        public CredentialQueryPort(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    private final CredentialQueryService credentialQueryService =
            new CredentialQueryService("credentialQuery");
    
    public SecurityManager(String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        this.addProvidedService(new SecurityAuthService("securityAuthorization"));
        this.addPort(new SecurityAuthorization("securityAuthorization"));
        this.addConnection("securityAuthorization",
                "securityAuthorization", "securityAuthorization");
        
        this.addRequiredService(this.credentialQueryService);
        this.addPort(new CredentialQueryPort("credentialQuery"));
        this.addConnection("credentialQuery", "credentialQuery",
                "credentialQuery");
    }
}
