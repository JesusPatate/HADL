package hadl.m1.cs;

import hadl.m2.Message;
import hadl.m2.component.ProvidedService;


public class SendRequestService extends ProvidedService {
    
    public SendRequestService(final String label) {
        super(label);
    }
    
    public void send(final Message msg) {
        this.connection.send(this, msg);
    }

    @Override
    public void receive(final Message msg) {
        // Shouldn't receive anything
    }
}
