package hadl.m1.cs;

import hadl.m1.Call;
import hadl.m1.CSMessage;
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


public class CSServer extends AtomicComponent {
    
    class ReceiveResponseService extends RequiredService {
        
        public ReceiveResponseService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {}
    }
    
    class ReceiveRequestService extends ProvidedService {
        
        public ReceiveRequestService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            boolean wrongParam = false;
            
            System.out.println("Le serveur reçoit : " + msg); // DBG
            
            if (msg.getHeader().equals("CALL")) {
                Call call = (Call) msg;
                
                if (call.getCalledService().equals(this.label)) {
                    // TODO Vérifier les paramètres
                    
                    if (wrongParam) {
                        // TODO Gérer appel incorrect
                    }
                    
                    Message request = (Message) call.getParameters().get(0);
                    
                    CSServer.this.sendResponse(request);
                }
                else {
                    // TODO Gérer appel incorrect
                }
            }
        }
    }
    
    class ServerPort extends Port {
        
        public ServerPort(final CSServer component) {
            super("port");
        }
        
        @Override
        public void receive(final Message msg) {
            Call call = (Call) msg;
            String service = call.getCalledService();
            
            if (this.linked(service)) {
                this.getLink(service).send(this, msg);
            }
            
            else {
                if(this.binding != null) {
                    this.binding.send(this, msg);
                }
            }
        }
    }
    
    private final ReceiveResponseService recResService =
            new ReceiveResponseService("receiveResponse");
    
    private final ReceiveRequestService recReqService =
            new ReceiveRequestService("receiveRequest");
    
    public CSServer(final String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        this.addRequiredService(this.recResService);
        this.addProvidedService(this.recReqService);
        
        Port port = new ServerPort(this);
        this.addPort(port);
        
        this.addConnection(this.recResService.getLabel(), this.recResService,
                port);
        
        this.addConnection(this.recReqService.getLabel(), this.recReqService,
                port);
    }

    private void sendResponse(Message request) {
        // TODO Auto-generated method stub
        
    }
}
