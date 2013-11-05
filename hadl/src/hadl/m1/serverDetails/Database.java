package hadl.m1.serverDetails;

import hadl.m2.Link;
import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.Port;
import hadl.m2.component.ProvidedService;

import java.util.HashMap;
import java.util.Map;


public class Database extends AtomicComponent {
    
    class SecurityManagementService extends ProvidedService {
        
        public SecurityManagementService(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            System.out.println("La BD reçoit : " + msg); // DBG
            
            check(msg);
        }
    }
    
    class ManageUsersService extends ProvidedService {

        public ManageUsersService(String label) {
            super(label);
        }

        @Override
        public void receive(Message msg, final Link link) {
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
    }
    
    class SecurityManagementPort extends Port {
        
        public SecurityManagementPort(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg, final Link link) {
            // TODO Router le message
        }
    }
    
    class ManageUsersPort extends Port {

        public ManageUsersPort(String label) {
            super(label);
        }

        @Override
        public void receive(Message msg, final Link link) {
            // TODO Router le message
        }
    }
    
    private Map<String, String> users = new HashMap<String, String>();
    
    public Database(String label) throws NoSuchServiceException,
            NoSuchPortException {
        super(label);
        
        this.addProvidedService(
                new SecurityManagementService("securityManagement"));
        this.addPort(new SecurityManagementPort("securityManagement"));
        this.addConnection("securityManagement", "securityManagement",
                "securityManagement");
        
        this.addProvidedService(new ManageUsersService("manageUsers"));
        this.addPort(new ManageUsersPort("manageUsers"));
        this.addConnection("manageUsers", "manageUsers", "manageUsers");
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
