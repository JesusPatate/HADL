package hadl.m1.cs.client;

import hadl.m2.Message;
import hadl.m2.component.ProvidedService;


public class SendRequestService extends ProvidedService {
    
    private final ReceiveRequestService receiveService;
    
    public SendRequestService(final String label,
            final ReceiveRequestService receiveService) {
        
        super(label);
        this.receiveService = receiveService;
    }
    
    public void send(final Message msg) {
        System.out.println("DBG Le service " + this.label + " envoie : " + msg); // DBG
        
        this.receiveService.receive(msg);
    }
    
    @Override
    public void receive(final Message msg) {
        System.out.println("DBG Le service " + this.label + " reçoit : " + msg); // DBG
        this.send(msg);
    }
}
