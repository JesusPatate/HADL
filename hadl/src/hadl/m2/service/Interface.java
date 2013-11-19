package hadl.m2.service;

import hadl.m2.configuration.Configuration;

public abstract class Interface{
    protected final String label;
    protected Configuration configuration = null;
	
    public Interface(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    /*public void setConfiguration(Configuration configuration){
    	this.configuration = configuration;
    }
    
    void addProvidedService(ProvidedService service){
    	providedServices.put(service.getLabel(), service);
    }
    
    void removeProvidedService(ProvidedService service){
    	providedServices.remove(service);
    }
    
    void addRequiredService(RequiredService service){
    	service.setInterface(this);
    	requiredServices.add(service);
    }
    
    void removeRequiredService(RequiredService service){
    	service.setInterface(null);
    	requiredServices.remove(service);
    }

    
    public boolean isProvide(String service){
    	return providedServices.containsKey(service);
    }
    
    public boolean isRequire(String service){
    	Iterator<RequiredService> it = requiredServices.iterator();
    	boolean found = false;
    	
    	while(it.hasNext() && !found){
    		RequiredService requiredService = it.next();
    		found = requiredService.getLabel().equals(service);
    	}
    	
    	return found;
    }*/
    
    //abstract void send(Call call);
    
    //public abstract void receive(Call call);
}
