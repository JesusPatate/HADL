package hadl.m1.cs;

import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.ProvidedService;
import hadl.m2.component.RequiredPort;
import hadl.m2.component.RequiredService;


public class CSClient extends AtomicComponent {
    
    class ReceiveRequestService extends RequiredService {
        
        public ReceiveRequestService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    class ReceiveRequestPort extends RequiredPort {
        
        public ReceiveRequestPort(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    class ReceiveResponseService extends ProvidedService {
        
        public ReceiveResponseService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            System.out.println("Le client reçoit : " + msg);
        }
    }
    
    class ReceiveResponsePort extends ProvidedPort {
        
        public ReceiveResponsePort(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    class SendRequestService extends ProvidedService {
        
        public SendRequestService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg) {
            System.out.println("Le client envoie : " + msg);
            recReqService.receive(msg);
        }
    }
    
    class SendRequestPort extends ProvidedPort {
        
        public SendRequestPort(final String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    private final ReceiveRequestService recReqService =
            new ReceiveRequestService("receiveRequest");
    
    private final SendRequestService sendReqService =
            new SendRequestService("sendRequest");
    
    public CSClient(final String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        
        this.addRequiredService(this.recReqService);
        this.addRequiredPort(new ReceiveRequestPort("receiveRequest"));
        
        this.addProvidedService(this.sendReqService);
        this.addProvidedService(new ReceiveResponseService("receiveResponse"));
        
        this.addProvidedPort(new SendRequestPort("sendRequest"));
        this.addProvidedPort(new ReceiveResponsePort("receiveResponse"));
        
        this.addRequiredConnection("receiveRequest", "receiveRequest",
                "receiveRequest");
        
        this.addProvidedConnection("sendRequest", "sendRequest", "sendRequest");
        this.addProvidedConnection("receiveResponse", "receiveResponse",
                "receiveResponse");
    }
}
