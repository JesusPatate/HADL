package hadl.m1.cs;

import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.Connection;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.ProvidedService;
import hadl.m2.component.RequiredService;
import hadl.m2.component.Port;
import hadl.m2.configuration.Attachment;


public class CSServer extends AtomicComponent {
    
    class ReceiveResponseService extends RequiredService {
        
        public ReceiveResponseService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    class ReceiveRequestService extends ProvidedService {
        
        public ReceiveRequestService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            System.out.println("Le serveur reçoit : " + msg); // DBG
            
            Message response = new Message("RESPONSE", msg.body + " OK");
            sendResService.receive(response, null);
        }
    }
    
    class SendResponseService extends ProvidedService {
        
        public SendResponseService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            System.out.println("Le serveur envoie : " + msg);
            recResService.receive(msg, null);
        }
    }
    
    class ServerPort extends Port {
        
        public ServerPort(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
        	if(link instanceof Attachment) {
        		for(Connection con : this.connections) {
        			String service = con.getConnectedService().getLabel();
        			if(service.equals("receiveRequest")) {
        				con.send(this, msg);
        			}
        		}
        	}
        	else if (link instanceof Connection) {
        		if (this.attachment != null) {
        			this.attachment.send(this, msg);
        		}
        	}
        }
    }
    
    private final ReceiveResponseService recResService =
            new ReceiveResponseService("receiveResponse");
    
    private final ReceiveRequestService recReqService =
            new ReceiveRequestService("receiveRequest");
    
    private final SendResponseService sendResService =
            new SendResponseService("sendResponse");
    
    public CSServer(final String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        this.addRequiredService(this.recResService);
        this.addProvidedService(this.recReqService);
        this.addProvidedService(this.sendResService);
        
        this.addPort(new ServerPort("io"));
        
        this.addConnection("receiveResponse", "receiveResponse",
                "io");
        
        this.addConnection("receiveRequest", "receiveRequest",
                "io");
        this.addConnection("sendResponse", "sendResponse",
                "io");
    }
}
