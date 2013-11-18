package hadl.m1.serverDetails;

import hadl.m1.CSMessage;
import hadl.m1.RPCCall;
import hadl.m2.Call;
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
                    
                    CSMessage query =
                            (CSMessage) call.getParameters().get(0);
                    
                    String msgType = (String) query.getElement(
                            CSMessage.Part.HEADER, "type");
                    
                    if (msgType.equals("AUTH")) {
                        SecurityManager.this.checkCredentials(query);
                    }
                }
                else {
                    // TODO Gérer appel incorrect
                }
            }
        }
    }
    
    class ReceiveCredAuth extends ProvidedService {
        
        public ReceiveCredAuth() {
            super("receiveCredentialsAuthentication");
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
                    
                    CSMessage queryMsg =
                            (CSMessage) call.getParameters().get(0);
                    
                    String msgType = (String) queryMsg.getElement(
                            CSMessage.Part.HEADER, "type");
                    
                    if (msgType.equals("CREDRESP")) {
                        SecurityManager.this.sendAuthorization(queryMsg);
                    }
                }
                else {
                    // TODO Gérer appel incorrect
                }
            }
        }
    }
    
    class SecurityManagement extends RequiredService {
        
        public SecurityManagement() {
            super("securityManagement");
        }
        
        @Override
        public void receive(Message msg) {
        }
    }
    
    class ReceiveAuthorization extends RequiredService {

        public ReceiveAuthorization() {
            super("receiveAuthorization");
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
    
    class CredentialQuery extends Port {
        
        public CredentialQuery(final SecurityManager component) {
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
        
        RequiredService securityMgmt = new SecurityManagement();
        RequiredService receiveAuth = new ReceiveAuthorization();
        ProvidedService securityAuth = new SecurityAuthService();
        ProvidedService receiveCredAuth = new ReceiveCredAuth();
        
        Port securityAuthPort = new SecurityAuthPort(this);
        Port credentialQuery = new CredentialQuery(this);
        
        this.addProvidedService(securityAuth);
        this.addPort(securityAuthPort);
        this.addConnection(securityAuth.getLabel(), securityAuth,
                securityAuthPort);
        
        this.addRequiredService(receiveAuth);
        this.addConnection(receiveAuth.getLabel(), receiveAuth,
                securityAuthPort);
        
        this.addProvidedService(receiveCredAuth);
        this.addPort(credentialQuery);
        this.addConnection(receiveCredAuth.getLabel(), receiveCredAuth,
                credentialQuery);
        
        this.addRequiredService(securityMgmt);
        this.addConnection(securityMgmt.getLabel(), securityMgmt,
                credentialQuery);
    }
    
    private void checkCredentials(final CSMessage msg) {
        String id = (String) msg.getElement(CSMessage.Part.HEADER, "id");
        String login = (String) msg.getElement(CSMessage.Part.BODY, "login");
        String pwd = (String) msg.getElement(CSMessage.Part.BODY, "password");
        
        CSMessage queryMsg = new CSMessage(id);
        
        queryMsg.addHeaderElement("type", "CREDQUERY");
        queryMsg.addBodyElement("login", login);
        queryMsg.addBodyElement("password", pwd);
        
        List<Object> args = new ArrayList<Object>();
        args.add(queryMsg);
        
        Call call = new RPCCall("securityManagement", args);
        
        System.out.println("Le gestionnaire de sécurité envoie : " + call); // DBG
        
        try {
            Port port = this.getRequestingPort("securityManagement");
            
            if (port != null) {
                port.receive(call);
            }
        }
        catch (NoSuchServiceException e) {
            // Couldn't be reachable
        }
    }
    
    private void sendAuthorization(CSMessage msg) {
        String id = (String) msg.getElement(CSMessage.Part.HEADER, "id");
        Boolean auth = (Boolean) msg.getElement(CSMessage.Part.BODY, "auth");
        
        CSMessage response = new CSMessage(id);
        
        response.addHeaderElement("type", "AUTHRESP");
        response.addBodyElement("auth", auth);
        
        List<Object> args = new ArrayList<Object>();
        args.add(response);
        
        Call call = new RPCCall("receiveAuthorization", args);
        
        System.out.println("Le gestionnaire de sécurité envoie : " + call); // DBG
        
        try {
            Port port = getRequestingPort("receiveAuthorization");
            port.receive(call);
        }
        catch (NoSuchServiceException e) {
            // Couldn't be reachable
        }
    }
}
