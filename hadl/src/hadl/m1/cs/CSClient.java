package hadl.m1.cs;

import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.Connection;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.Port;
import hadl.m2.component.ProvidedService;
import hadl.m2.component.RequiredService;
import hadl.m2.configuration.Attachment;


public class CSClient extends AtomicComponent {
    
    class ReceiveRequestService extends RequiredService {
        
        public ReceiveRequestService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    class ReceiveResponseService extends ProvidedService {
        
        public ReceiveResponseService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            System.out.println("Le client reçoit : " + msg);
        }
    }
    
    class SendRequestService extends ProvidedService {
        
        public SendRequestService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            System.out.println("Le client envoie : " + msg);
            recReqService.receive(msg, null);
        }
    }
    
    class ClientPort extends Port {
        
        public ClientPort(final String label) {
            super(label);
        }

		@Override
		public void receive(Message msg, Link link) {
			if(link == null) {
				sendReqService.receive(msg, null); // XXX Utiliser la connection !!
			}
			if(link instanceof Connection) {
				if(this.attachment != null) {
					this.attachment.send(this, msg);
				}
			}
			if(link instanceof Attachment) {
				receiveResponseService.receive(msg, null); // XXX Utiliser la connection !!
			}
		}
    }
    
    class ReceiveRequestPort extends Port {
        
        public ReceiveRequestPort(final String label) {
            super(label);
        }
        
        @Override
        public void receive(final Message msg, final Link link) {
            if (this.attachment != null) {
                this.attachment.send(this, msg);
            }
        }
    }
    
    private final ReceiveResponseService receiveResponseService =
    		new ReceiveResponseService("receiveResponse");
    
    private final ReceiveRequestService recReqService =
            new ReceiveRequestService("receiveRequest");
    
    private final SendRequestService sendReqService =
            new SendRequestService("sendRequest");
    
    public CSClient(final String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);

        this.addPort(new ClientPort("io"));
        
        this.addRequiredService(this.recReqService);
        this.addPort(new ReceiveRequestPort("receiveRequest"));
        this.addConnection("receiveRequest", "receiveRequest","io");
        
        this.addProvidedService(this.sendReqService);
        this.addConnection("sendRequest", "sendRequest", "io");
        
        this.addProvidedService(this.receiveResponseService);
        this.addConnection("receiveResponse", "receiveResponse","io");
    }
}
