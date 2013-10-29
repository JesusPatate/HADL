package hadl.m1.cs.server;

import hadl.m2.Message;
import hadl.m2.component.ProvidedService;


public class ReceiveRequestService extends ProvidedService {
    
    private final SendResponseService sendService;
    
    public ReceiveRequestService(final String label,
            final SendResponseService sendService) {
        
        super(label);
        this.sendService = sendService;
    }
    
    @Override
    public void receive(final Message msg) {
        System.out.println("DBG Le service " + this.label + " reçoit : " + msg); // DBG
        this.sendService.receive(msg);
    }
    
}
