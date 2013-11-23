package fr.univnantes.alma.hadl.m1.serverDetails;

import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Role;


public class SecurityQuery extends AtomicConnector {        
    public SecurityQuery(String label) {
        super(label);
        Role senderRole = new Role("sender");
        Role receiverRole = new Role("receiver");
        
        addRole(senderRole);
        addRole(receiverRole);
    }
}
