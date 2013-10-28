package hadl.m1.cs;
import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.component.ProvidedService;



public class ReceiveResponseService extends ProvidedService {
    
    public ReceiveResponseService(String label) {
        super(label);
    }

    @Override
    public void plug(Link link) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void receive(Message msg) {
        System.out.println(msg);
    }
    
}
