package hadl.m1.serverDetails;

import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.ProvidedService;
import hadl.m2.component.RequiredPort;
import hadl.m2.component.RequiredService;


public class SecurityManager extends AtomicComponent {
    
    class SecurityAuthService extends ProvidedService {
        
        public SecurityAuthService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
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
        public void receive(Message msg) {
            
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
    
    class SecurityAuthorization extends ProvidedPort {
        
        public SecurityAuthorization(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    class CredentialQueryPort extends RequiredPort {
        
        public CredentialQueryPort(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
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
        this.addProvidedPort(new SecurityAuthorization("securityAuthorization"));
        this.addProvidedConnection("securityAuthorization",
                "securityAuthorization", "securityAuthorization");
        
        this.addRequiredService(this.credentialQueryService);
        this.addRequiredPort(new CredentialQueryPort("credentialQuery"));
        this.addRequiredConnection("credentialQuery", "credentialQuery",
                "credentialQuery");
    }
}
