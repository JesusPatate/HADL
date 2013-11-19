package hadl.m1.cs;

import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.RequiredPort;
import hadl.m2.service.ProvidedService;
import hadl.m2.service.RequiredService;


public class CSServer extends AtomicComponent {
    
    class ReceiveResponseService extends RequiredService {
        
        public ReceiveResponseService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    class ReceiveResponsePort extends RequiredPort {
        
        public ReceiveResponsePort(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    class ReceiveRequestService extends ProvidedService {
        
        public ReceiveRequestService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            System.out.println("Le serveur reçoit : " + msg);
            
            Message response = new Message("RESPONSE", msg.body + " OK");
            sendResService.receive(response);
        }
    }
    
    class ReceiveRequestPort extends ProvidedPort {
        
        public ReceiveRequestPort(final String label) {
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
    
    class SendResponseService extends ProvidedService {
        
        public SendResponseService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            System.out.println("Le serveur envoie : " + msg);
            recResService.receive(msg);
        }
    }
    
    class SendResponsePort extends ProvidedPort {
        
        public SendResponsePort(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
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
        this.addRequiredPort(new ReceiveResponsePort("receiveResponse"));
        
        this.addProvidedService(this.recReqService);
        this.addProvidedPort(new ReceiveRequestPort("receiveRequest"));
        
        this.addProvidedService(this.sendResService);
        this.addProvidedPort(new SendResponsePort("sendResponse"));
        
        this.addRequiredConnection("receiveResponse", "receiveResponse",
                "receiveResponse");
        
        this.addProvidedConnection("receiveRequest", "receiveRequest",
                "receiveRequest");
        this.addProvidedConnection("sendResponse", "sendResponse",
                "sendResponse");
    }
}
