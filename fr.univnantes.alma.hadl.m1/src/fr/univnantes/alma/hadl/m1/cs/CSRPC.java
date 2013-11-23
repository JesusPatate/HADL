package fr.univnantes.alma.hadl.m1.cs;

import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Role;


public class CSRPC extends AtomicConnector {    
    public CSRPC(String label) {
        super(label);
        Role callerRole = new Role("caller");
        Role calleeRole = new Role("callee");
        addRole(calleeRole);
        addRole(callerRole);
        
        // ajouter services
    }
}
