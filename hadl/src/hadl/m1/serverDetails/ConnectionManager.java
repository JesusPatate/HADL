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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
                    
                    System.out
                            .println("Le gestionnaire de connection reçoit : "
                                    + call); // DBG
                    
                    CSMessage query = (CSMessage) call.getParameters().get(0);
                    
                    String msgType = (String) query.getElement(
                            CSMessage.Part.HEADER, "type");
                    
                    String queryID = (String) query.getElement(
                            CSMessage.Part.HEADER, "id");
                    
                    if (msgType.equals("QUERY")) {
                        ConnectionManager.this.queryBuffer.put(queryID, query);
                        ConnectionManager.this.checkSecurity(query);
                    }
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
                    
                    System.out
                            .println("Le gestionnaire de connection envoie : "
                                    + call); // DBG
                }
                else {
                    // TODO Gérer appel incorrect
                }
            }
        }
    }
    
    class ReceiveAuthorization extends ProvidedService {

        public ReceiveAuthorization() {
            super("receiveAuthorization");
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
                    
                    System.out.println("Le gestionnaire de connexion reçoit : "
                            + call); // DBG
                    
                    CSMessage response =
                            (CSMessage) call.getParameters().get(0);
                    
                    String msgType = (String) response.getElement(
                            CSMessage.Part.HEADER, "type");
                    
                    if (msgType.equals("AUTHRESP")) {
                        ConnectionManager.this.queryDatabase(response);
                    }
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
        public void receive(final Message msg) {}
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
    
    private final Map<String, CSMessage> queryBuffer =
            new HashMap<String, CSMessage>();
    
    public ConnectionManager(String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        RequiredService securityAuth = new SecurityAuthorization();
        ProvidedService receiveRequest = new ReceiveRequest();
        ProvidedService receiveAuth = new ReceiveAuthorization();
        
        Port externalSocket = new ExternalSocket(this);
        Port securityQuery = new SecurityQuery(this);
        
        this.addProvidedService(receiveRequest);
        this.addPort(externalSocket);
        this.addConnection(receiveRequest.getLabel(), receiveRequest,
                externalSocket);
        
        this.addProvidedService(receiveAuth);
        this.addPort(securityQuery);
        this.addConnection(receiveAuth.getLabel(), receiveAuth,
                securityQuery);
        
        this.addRequiredService(securityAuth);
        this.addConnection(securityAuth.getLabel(), securityAuth,
                securityQuery);
    }

    private void checkSecurity(final CSMessage msg) {
        String id = (String) msg.getElement(CSMessage.Part.HEADER, "id");
        String login = (String) msg.getElement(CSMessage.Part.BODY, "login");
        String pwd = (String) msg.getElement(CSMessage.Part.BODY, "password");
        
        CSMessage queryMsg = new CSMessage(id);
        
        queryMsg.addHeaderElement("type", "AUTH");
        queryMsg.addBodyElement("login", login);
        queryMsg.addBodyElement("password", pwd);
        
        List<Object> args = new ArrayList<Object>();
        args.add(queryMsg);
        
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
    
    private void queryDatabase(CSMessage msg) {
        String id = (String) msg.getElement(CSMessage.Part.HEADER, "id");
        Boolean auth = (Boolean) msg.getElement(CSMessage.Part.BODY, "auth");
        
        if (auth) {
            CSMessage queryMsg = this.queryBuffer.get(id);
            String query = (String) queryMsg.getElement(CSMessage.Part.BODY,
                    "query");
            
            if (queryMsg != null) {
                CSMessage response = new CSMessage(id);
                
                response.addHeaderElement("type", "QUERY");
                response.addBodyElement("query", query);
                
                List<Object> args = new ArrayList<Object>();
                args.add(response);
                
                Call call = new RPCCall("handleQuery", args);
                
                System.out.println("Le gestionnaire de connexion envoie : " + call); // DBG
                
                try {
                    Port port = getRequestingPort("receiveAuthorization");
                    port.receive(call);
                }
                catch (NoSuchServiceException e) {
                    // Couldn't be reachable
                }
            }
        }
        
        else {
            
        }
    }
}
