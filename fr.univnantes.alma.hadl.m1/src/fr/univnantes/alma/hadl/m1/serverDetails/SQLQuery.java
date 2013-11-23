package fr.univnantes.alma.hadl.m1.serverDetails;

import fr.univnantes.alma.hadl.m2.connector.AtomicConnector;
import fr.univnantes.alma.hadl.m2.connector.Role;
import fr.univnantes.alma.hadl.m2.service.IncompatibleServiceException;
import fr.univnantes.alma.hadl.m2.service.NotConnectedServiceException;
import fr.univnantes.alma.hadl.m2.service.Service;

public class SQLQuery extends AtomicConnector {
	private class SQLQueryService extends Service {
		SQLQueryService() {
			// TODO: signature à compléter
			super("sqlQuery", null, null);
		}
    }
	
    public SQLQuery(String label) {
        super(label);
        Role caller = new Role("caller");
        Role callee = new Role("callee");
        SQLQueryService required = new SQLQueryService();
        SQLQueryService provided = new SQLQueryService();
        
        addRequiredService(callee, required);
        try {
			addProvidedService(caller, provided, required);
		} catch (NotConnectedServiceException e) {
			e.printStackTrace();
		} catch (IncompatibleServiceException e) {
			e.printStackTrace();
		}
    }
}
