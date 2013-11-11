package hadl.m1.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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


public class CSClient extends AtomicComponent {
    
    class ReceiveRequestService extends RequiredService {
        
        public ReceiveRequestService() {
            super("receiveRequest");
        }
        
        @Override
        public void receive(final Message msg) {
        }
    }
    
    /**
     * Client reception service.
     */
    class ReceiveResponseService extends ProvidedService {
        
        public ReceiveResponseService() {
            super("receiveResponse");
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
                    
                    System.out.println("Le client reçoit : " + call); // DBG
                }
                else {
                    // TODO Gérer appel incorrect
                }
            }
        }
    }
    
    class SendRequestPort extends Port {
        
        public SendRequestPort(final CSClient component) {
            super("sendRequest");
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
    
    public CSClient(final String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        RequiredService receiveReq = new ReceiveRequestService();
        this.addRequiredService(receiveReq);
        
        ProvidedService receiveRes = new ReceiveResponseService();
        this.addProvidedService(receiveRes);
        
        Port port = new SendRequestPort(this);
        this.addPort(port);
        
        this.addConnection(receiveReq.getLabel(), receiveReq.getLabel(),
                port.getLabel());
        
        this.addConnection(receiveRes.getLabel(), receiveRes.getLabel(),
                port.getLabel());
    }
    
    public void query(final String query, final String login, final String pwd) {
        Random rand = new Random();
        Integer id = new Integer(rand.nextInt(100000) + 100000);
        CSMessage queryMsg = new CSMessage(id.toString());
        
        queryMsg.addHeaderElement("type", "QUERY");
        queryMsg.addBodyElement("query", query);
        queryMsg.addBodyElement("login", login);
        queryMsg.addBodyElement("password", pwd);
        
        this.sendRequest(queryMsg);
    }
    
    private void sendRequest(final Message msg) {
        List<Object> args = new ArrayList<Object>();
        args.add(msg);
        
        Call call = new RPCCall("receiveRequest", args);
        
        System.out.println("Le client envoie : " + call); // DBG
        
        try {
            Port port = getRequestingPort("receiveRequest");
            port.receive(call);
        }
        catch (NoSuchServiceException e) {
            // Couldn't be reachable
        }
    }
}
