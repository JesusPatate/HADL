package hadl.m1.serverDetails;

import java.util.StringTokenizer;

import hadl.m2.Message;
import hadl.m2.component.AtomicComponent;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.ProvidedService;


public class SecurityManager extends AtomicComponent {
    
    class SecurityAuthService extends ProvidedService {
        
        public SecurityAuthService(final String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            System.out.println("Le gestionnaire de sécurité reçoit : " + msg);
            
            if (msg.header.contentEquals("AUTH")) {
                final String login = getLogin(msg);
                final String pwd = getPwd(msg);
                
                if (authorize(login, pwd)) {
                    System.out.println("DBG connection autorisée");
                }
                else {
                    System.out.println("DBG connection non autorisée");
                }
            }
        }
    }
    
    class SecurityAuthorization extends ProvidedPort {
        
        public SecurityAuthorization(String label) {
            super(label);
        }
        
        @Override
        public void receive(Message msg) {
            if (this.connection != null) {
                this.connection.send(this, msg);
            }
        }
    }
    
    public SecurityManager(String label) throws NoSuchServiceException,
            NoSuchPortException {
        
        super(label);
        this.addProvidedService(new SecurityAuthService("securityAuth"));
        this.addProvidedPort(new SecurityAuthorization("securityAuth"));
        this.addProvidedConnection("securityAuth", "securityAuth",
                "securityAuth");
    }
    
    private boolean authorize(final String login, final String pwd) {
        return true; // XXX
    }
    
    private String getLogin(final Message msg) {
        String login = "";
        
        StringTokenizer tok = new StringTokenizer(msg.content, "'");
        boolean stop = false;
        
        while (stop == false && tok.hasMoreTokens()) {
            login += tok.nextToken();
            
            if (login.endsWith("\\") == false) {
                stop = true;
            }
            else {
                login += "'";
            }
        }
        
        return login;
    }
    
    private String getPwd(final Message msg) {
        String pwd = "";
        
        StringTokenizer tok = new StringTokenizer(msg.content, "'");
        boolean stop = false;
        
        // Ignore login
        if (tok.hasMoreTokens()) {
            tok.nextToken(); // login
            tok.nextToken(); // coma
        }
        
        while (stop == false && tok.hasMoreTokens()) {
            pwd += tok.nextToken();
            
            if (pwd.endsWith("\\") == false) {
                stop = true;
            }
            else {
                pwd += "'";
            }
        }
        
        return pwd;
    }
}
