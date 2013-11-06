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


public class ConnectionManager extends AtomicComponent {
    
    class ReceiveRequest extends ProvidedService {
        
        public ReceiveRequest() {
            super("receiveRequest");
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
                    
                    System.out.println("Le gestionnaire de connection reçoit : "
                            + call); // DBG
                    
                    Message request = (Message) call.getParameters().get(0);
                    
                    ConnectionManager.this.checkSecurity(request);
                }
                else {
                    // TODO Gérer appel incorrect
                }
            }
        }
    }
    
    class SendResponse extends ProvidedService {
        
        public SendResponse() {
            super("sendResponse");
        }
        
        @Override
        public void receive(Message msg) {
            boolean wrongParam = false;
            
            if (msg.getHeader().equals("CALL")) {
                Call call = (Call) msg;
                
                if (call.getCalledService().equals(this.label)) {
                    // TODO Vérifier les paramètres
                    
                    if (wrongParam) {
                        // TODO Gérer appel incorrect
                    }
                    
                    System.out.println("Le gestionnaire de connection envoie : "
                            + call); // DBG
                }
                else {
                    // TODO Gérer appel incorrect
                }
            }
        }
    }
    
    class ReceiveResponse extends RequiredService {
        
        public ReceiveResponse() {
            super("receiveResponse");
        }
        
        @Override
        public void receive(Message msg) {
        }
    }
    
    class HandleQuery extends RequiredService {
        
        public HandleQuery() {
            super("handleQuery");
        }
        
        @Override
        public void receive(final Message msg) {
        }
    }
    
    class SecurityAuthorization extends RequiredService {
        
        public SecurityAuthorization() {
            super("securityAuthorization");
        }
        
        @Override
        public void receive(final Message msg) {
        }
        
    }
    
    class ExternalSocket extends Port {
        
        public ExternalSocket(final ConnectionManager component) {
            super("externalSocket");
        }
        
        @Override
        public void receive(final Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if (this.linked(service)) {
                this.getLink(service).send(this, msg);
            }
            
            else {
                if (this.binding != null) {
                    this.binding.send(this, msg);
                }
            }
        }
    }
    
    class DBQuery extends Port {
        
        public DBQuery(final ConnectionManager component) {
            super("DBQuery");
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
    
    class SecurityQuery extends Port {
        
        public SecurityQuery(final ConnectionManager component) {
            super("securityQuery");
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
    
    public ConnectionManager(String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        RequiredService securityAuth = new SecurityAuthorization();
        ProvidedService receiveRequest = new ReceiveRequest();
        
        Port externalSocket = new ExternalSocket(this);
        Port securityQuery = new SecurityQuery(this);
        
        this.addProvidedService(receiveRequest);
        this.addPort(externalSocket);
        this.addConnection(receiveRequest.getLabel(), receiveRequest,
                externalSocket);
        
        this.addRequiredService(securityAuth);
        this.addPort(securityQuery);
        this.addConnection(securityAuth.getLabel(), securityAuth,
                securityQuery);
    }
    
    public void checkSecurity(final Message msg) {
        if(msg.getHeader().equals("QUERY")) {
            String login = ((CSMessage) msg).getBodyElement(0);
            String pwd = ((CSMessage) msg).getBodyElement(1);
            
            Message query = new CSMessage("AUTH", "\'" + login + "\',\""
                    + pwd + "\'");
            
            List<Object> args = new ArrayList<Object>();
            args.add(query);
            
            Call call = new RPCCall("securityAuthorization", args);
            
            System.out.println("Le gestionnaire de connection envoie : "
                    + call); // DBG
            
            try {
                Port port = getRequestingPort("securityAuthorization");
                port.receive(call);
            }
            catch (NoSuchServiceException e) {
                // Couldn't be reachable
            }
        }
    }
}
