package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;
import fr.univnantes.alma.hadl.m2.service.Service;


public class Database extends AtomicComponent {
    
    private class SecurityManagementService extends Service {
        public SecurityManagementService() {
        	// TODO: signature à compléter
            super("securityManagement", null, null);
        }
    }
    
    private class QueryService extends ProvidedService {
        public QueryService() {
        	// TODO: signature à compléter
            super("manageUsers", null, null);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    private Map<String, String> users = new HashMap<String, String>();
    
    public Database(String label){
        super(label);
        Port securityManagement = new Port("securityManagement");
        Port query = new Port("query");
        
        addRequiredConnection(securityManagement, new SecurityManagementService());
        addProvidedConnection(query, new QueryService());
    }
    
    /*private boolean addUser(final String login, final String pwd) {
        boolean res = false;
        
        String output = this.users.get(login);
        
        if(output == null) {
            this.users.put(login, pwd);
            res = true;
            
            System.out.println("Utilisateur \"" + login + "\" ajouté."); // DBG
        }
        
        return res;
    }
    
    private boolean deleteUser(final String login, final String pwd) {
        boolean res = false;
        
        final String suggestedPwd = this.users.get(login);
        
        if(suggestedPwd != null && pwd.contentEquals(suggestedPwd)) {
            this.users.remove(login);
            res = true;
            
            System.out.println("Utilisateur supprimé."); // DBG
        }
        
        return res;
    }
    
    private boolean check(final Message msg) {
        boolean res = false;
        
        if(msg.header.contentEquals("CREDQUERY")) {
            String login = msg.getBodyElement(0);
            String suggestedPwd = msg.getBodyElement(1);
            
            String pwd = this.users.get(login);
            
            if(pwd != null) { // Existing user
                res = suggestedPwd.contentEquals(pwd);
            }
            
            System.out.println("Utilisateur trouvé dans la bd : " + res); // DBG
        }
        
        return res;
    }*/
}
