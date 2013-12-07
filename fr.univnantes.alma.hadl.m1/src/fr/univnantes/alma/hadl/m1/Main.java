package fr.univnantes.alma.hadl.m1;

import fr.univnantes.alma.hadl.m1.cs.CSClient;
import fr.univnantes.alma.hadl.m1.cs.CSConfiguration;
import fr.univnantes.alma.hadl.m1.cs.CSRPC;
import fr.univnantes.alma.hadl.m1.cs.CSServer;
import fr.univnantes.alma.hadl.m1.serverDetails.ClearanceQuery;
import fr.univnantes.alma.hadl.m1.serverDetails.ConnectionManager;
import fr.univnantes.alma.hadl.m1.serverDetails.Database;
import fr.univnantes.alma.hadl.m1.serverDetails.SecurityManager;
import fr.univnantes.alma.hadl.m1.serverDetails.SecurityQuery;
import fr.univnantes.alma.hadl.m1.serverDetails.ServerDetailsConfiguration;
import fr.univnantes.alma.hadl.m2.component.Port;
import fr.univnantes.alma.hadl.m2.configuration.IllegalAttachmentException;
import fr.univnantes.alma.hadl.m2.connector.Role;


public class Main {
    
    private static CSClient client;
    
    private static CSServer server;
    
    private static ConnectionManager connectionMgr;
    
    private static SecurityManager securityMgr;
    
    private static Database database;
    
    private static CSConfiguration cs = new CSConfiguration("cs");
    
    private static ServerDetailsConfiguration serverDetails =
            new ServerDetailsConfiguration("serverDetails");
    
    public static void main(String[] args) throws IllegalAttachmentException {
        buildServerDetails();
        buildCS();
        
        DBRequest req = new DBRequest("meuh", "login", "pwd");
        client.send(req);
    }
    
    private static void buildServerDetails() throws IllegalAttachmentException {
        
        connectionMgr = new ConnectionManager("connectionManager");
        serverDetails.addComponent(connectionMgr);
        
        database = new Database("database");
        serverDetails.addComponent(database);
        
        securityMgr = new SecurityManager("securityManager");
        serverDetails.addComponent(securityMgr);
        
        // Clearance query
        
        ClearanceQuery clearanceQuery = new ClearanceQuery("clearanceQuery");
        serverDetails.addConnector(clearanceQuery);
        
        Port senderPort = connectionMgr.getRequestingPort(
        		"securityAuthorization");
        Role senderRole = clearanceQuery.getRequestingRole(
        		"securityAuthorization");
        serverDetails.addAttachment(senderPort, connectionMgr, senderRole,
                clearanceQuery);
        
        Port receiverPort = securityMgr.getProvidingPort(
        		"securityAuthorization");
        Role receiverRole = clearanceQuery.getProvidingRole(
        		"securityAuthorization");
        serverDetails.addAttachment(receiverPort, securityMgr, receiverRole,
                clearanceQuery);
        
        // Security query
        
        SecurityQuery securityQuery = new SecurityQuery("securityQuery");
        serverDetails.addConnector(securityQuery);
        
        senderPort = database.getRequestingPort("securityManagement");
        senderRole = securityQuery.getRequestingRole("securityManagement");        		
        serverDetails.addAttachment(senderPort, database, senderRole,
                securityQuery);
        
        receiverPort = securityMgr.getProvidingPort("securityManagement");
        receiverRole = securityQuery.getProvidingRole("securityManagement");
        serverDetails.addAttachment(receiverPort, securityMgr, receiverRole,
                securityQuery);
        
        // Binding
        
        Port configPort = serverDetails.getProvidingPort("receiveRequest");
        Port conMgrPort = connectionMgr.getProvidingPort("receiveRequest");
        serverDetails.addBinding(configPort, conMgrPort);
    }
    
    private static void buildCS() throws IllegalAttachmentException {
        
        client = new CSClient("client");
        cs.addComponent(client);
        
        server = new CSServer("server");
        cs.addComponent(server);
        //cs.addComponent(serverDetails);
        
        CSRPC rpc = new CSRPC("RPC"); // Request
        cs.addConnector(rpc);
        
        // Request
        
        Port reqPort = client.getRequestingPort("receiveRequest");
        Role caller = rpc.getRequestingRole("receiveRequest");
        cs.addAttachment(reqPort, client, caller, rpc);
        
        Port proPort = server.getProvidingPort("receiveRequest");
        Role callee = rpc.getProvidingRole("receiveRequest");
        cs.addAttachment(proPort, server, callee, rpc);
        
        // Binding
        
        Port compositePort = serverDetails.getProvidingPort("receiveRequest");
        Port componentPort = server.getProvidingPort("receiveRequest");
        cs.addBinding(compositePort, componentPort);
    }
}
