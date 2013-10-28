package hadl.m1.cs;

import hadl.m2.component.AtomicComponent;


public class CSClient extends AtomicComponent {
    
    public CSClient(String label) {
        super(label);
        
        this.addProvidedService(new SendRequestService("sendRequest"));
        this.addProvidedService(new ReceiveResponseService("receiveResponse"));
        this.addProvidedPort(new SendRequestPort("sendRequest"));
    }
    
    
}
