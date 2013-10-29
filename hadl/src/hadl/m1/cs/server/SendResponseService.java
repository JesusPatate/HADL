package hadl.m1.cs.server;

import hadl.m2.Message;
import hadl.m2.component.ProvidedService;


public class SendResponseService extends ProvidedService {
    
    private final ReceiveResponseService receiveService;
    
    public SendResponseService(final String label,
            final ReceiveResponseService receiveService) {
        
        super(label);
        this.receiveService = receiveService;
    }
    
    @Override
    public void receive(final Message msg) {
        System.out.println("DBG Le service " + this.label + " envoie : " + msg); // DBG
        this.receiveService.receive(msg);
    }
    
}
