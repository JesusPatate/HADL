package hadl.m1.serverDetails;

import hadl.m1.CSMessage;
import hadl.m1.Call;
import hadl.m1.RPCCall;
import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.Port;
import hadl.m2.component.ProvidedService;
import hadl.m2.component.RequiredService;

import java.util.ArrayList;
import java.util.List;


public class SecurityManager extends AtomicComponent {
    
    class SecurityAuthService extends ProvidedService {
        
        public SecurityAuthService() {
            super("securityAuthorization");
        }
        
        @Override
        public void receive(final Message msg) {
            boolean wrongParam = false;
            
            if (msg.getHeader().equals("CALL")) {
                Call call = (Call) msg;
                
                if (call.getCalledService().equals(this.label)) {
                    // TODO Vérifier les paramètres
                    
                    if (wrongParam) {
                        // TODO Gérer appel incorrect
                    }
                    
                    System.out.println("Le gestionnaire de sécurité reçoit : "
                            + call); // DBG
                    
                    Message queryMsg = (Message) call.getParameters().get(0);
                    
                    if(queryMsg.getHeader().equals("AUTH")) {
                        SecurityManager.this.checkCredentials(queryMsg);
                    }
                }
                else {
                    // TODO Gérer appel incorrect
                }
            }
        }
    }
    
    class CredentialQueryService extends RequiredService {
        
        public CredentialQueryService() {
            super("credentialQuery");
        }
        
        @Override
        public void receive(Message msg) {}
    }
    
    class SecurityAuthPort extends Port {
        
        public SecurityAuthPort(final SecurityManager component) {
            super("securityAuthorization");
        }
        
        @Override
        public void receive(final Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if (this.linked(service)) {
                this.getLink(service).send(this, msg);
            }
        }
    }
    
    class CredentialQueryPort extends Port {
        
        public CredentialQueryPort(final SecurityManager component) {
            super("credentialQuery");
        }
        
        @Override
        public void receive(final Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if (this.linked(service)) {
                this.getLink(service).send(this, msg);
            }
        }
    }
    
    public SecurityManager(String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        RequiredService credentialQuery = new CredentialQueryService();
        ProvidedService securityAuth = new SecurityAuthService();
        
        Port securityAuthPort = new SecurityAuthPort(this);
        
        this.addProvidedService(securityAuth);
        this.addPort(securityAuthPort);
        this.addConnection(securityAuth.getLabel(), securityAuth,
                securityAuthPort);
        
        this.addRequiredService(credentialQuery);
        this.addConnection(credentialQuery.getLabel(), credentialQuery,
                securityAuthPort);
    }
    
    private void checkCredentials(final Message msg) {
        Message queryMsg = new CSMessage("CREDQUERY", msg.getBody());
        
        List<Object> args = new ArrayList<Object>();
        args.add(queryMsg);
        
        Call call = new RPCCall("securityManagement", args);
        
        System.out.println("Le gestionnaire de sécurité envoie : "
                + call); // DBG
        
        try {
            Port port = this.getRequestingPort("securityManagement");
            
            if(port != null) {
                port.receive(call);
            }
        }
        catch(NoSuchServiceException e) {
            // Couldn't be reachable
        }
    }
}
