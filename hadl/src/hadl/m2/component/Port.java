package hadl.m2.component;

import hadl.m2.Call;
import hadl.m2.Interface;
import hadl.m2.configuration.Configuration;


public abstract class Port extends Interface {
    
    protected Configuration config = null;
    
    protected Component component = null;
    
    /**
     * Creates a new port.
     * 
     * @param label
     *            Name of the port
     */
    public Port(final String label, Component component) {
        super(label);
        this.component = component;
    }

    public void setConfiguration(Configuration configuration){
    	this.config = configuration;
    }
    
    public void send(Call call){    	
    	this.config.receive(this, call);
    }
    
    public void receive(Call call){
    	this.component.send(call);
    }
}
