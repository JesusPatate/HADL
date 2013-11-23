package fr.univnantes.alma.hadl.m1.serverDetails;

import java.util.HashMap;
import java.util.Map;

import fr.univnantes.alma.hadl.m2.Response;
import fr.univnantes.alma.hadl.m2.component.AtomicComponent;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.service.ProvidedService;


public class Database extends AtomicComponent {
    
    private class SecurityManagementService extends ProvidedService {
        
        public SecurityManagementService() {
            super("securityManagement", null, null);
        }
        
        @Override
        public void receive(Message msg) {
            System.out.println("La BD reçoit : " + msg); // DBG
            
            check(msg);
        }

		@Override
		public Response excecute(Map<String, Object> parameters) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    private class ManageUsersService extends ProvidedService {

        public ManageUsersService() {
            super("manageUsers", null, null);
        }

        @Override
        public void receive(Message msg) {
            System.out.println("La BD reçoit : " + msg); // DBG
            
            if (msg.header.contentEquals("ADMQUERY")) {
                String operation = msg.getBodyElement(0);
                
                final String login = msg.getBodyElement(1);
                final String pwd = msg.getBodyElement(2);
                
                if(operation.contentEquals("add")) {
                    addUser(login, pwd);
                }
                else if (operation.contentEquals("delete")) {
                    deleteUser(login, pwd);
                }
            }
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
        Port securityManagementPort = new Port("securityManagement");
        Port manageUsersPort = new Port("manageUsers");
        
        addProvidedConnection(securityManagementPort, new SecurityManagementService());
        addProvidedConnection(manageUsersPort, new ManageUsersService());
    }
    
    private boolean addUser(final String login, final String pwd) {
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
    }
}
