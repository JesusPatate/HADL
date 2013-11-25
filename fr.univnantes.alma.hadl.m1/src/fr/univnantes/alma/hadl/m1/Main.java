package fr.univnantes.alma.hadl.m1;

import fr.univnantes.alma.hadl.m1.cs.CSClient;
import fr.univnantes.alma.hadl.m1.cs.CSConfiguration;
import fr.univnantes.alma.hadl.m1.cs.CSRPC;
import fr.univnantes.alma.hadl.m1.cs.CSServer;
import fr.univnantes.alma.hadl.m1.serverDetails.ConnectionManager;
import fr.univnantes.alma.hadl.m1.serverDetails.Database;
import fr.univnantes.alma.hadl.m1.serverDetails.SecurityManager;
import fr.univnantes.alma.hadl.m1.serverDetails.ServerDetailsConfiguration;
import fr.univnantes.alma.hadl.m2.component.Port;
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
    
    public static void main(String[] args) {
//        try {
//            buildServerDetails();
//            buildCS();
//            
//            // Add users to database
//            
//            Message msg = new Message("ADMQUERY", "\'add\',\'monLogin\',\'monPwd\'");
//            
//            ProvidedPort port = database.getProvidingPort("manageUsers");
//            port.receive(msg);
//            
//            msg = new Message("ADMQUERY", "\'add\',\'Georges\',\'Bondiou\'");
//            port.receive(msg);
//            
//            // Send SQL query
//            
//            msg = new Message("QUERY", "'Georges','Bondiou',"
//                    + "'SELECT * FROM data'");
//            
//            port = client.getProvidingPort("sendRequest");
//            port.receive(msg);
//        }
//        catch (NoSuchServiceException e) {
//            e.printStackTrace();
//        }
//        catch (NoSuchPortException e) {
//            e.printStackTrace();
//        }
        
        buildCS();
        
        DBRequest req = new DBRequest("meuh", "login", "pwd");
        client.send(req);
    }
    
//    private static void buildServerDetails() {
//        
//        connectionMgr = new ConnectionManager("connectionManager");
//        serverDetails.addComponent(connectionMgr);
//        
//        database = new Database("database");
//        serverDetails.addComponent(database);
//        
//        securityMgr = new SecurityManager("securityManager");
//        serverDetails.addComponent(securityMgr);
//        
//        // Clearance query
//        
//        ClearanceQuery clearanceQuery = new ClearanceQuery("clearanceQuery");
//        serverDetails.addConnector(clearanceQuery);
//        
//        RequiredPort senderPort =
//                connectionMgr.getRequestingPort("securityAuthorization");
//        FromRole senderRole = clearanceQuery.getFromRoles().get("sender");
//        serverDetails.addFromAttachment("clearanceQueryFA", senderPort,
//                senderRole);
//        
//        ProvidedPort receiverPort =
//                securityMgr.getProvidingPort("securityAuthorization");
//        ToRole receiverRole = clearanceQuery.getToRoles().get("receiver");
//        serverDetails.addToAttachment("clearanceQueryTA", receiverPort,
//                receiverRole);
//        
//        // Security query
//        
//        SecurityQuery securityQuery = new SecurityQuery("securityQuery");
//        serverDetails.addConnector(securityQuery);
//        
//        senderPort = securityMgr.getRequestingPort("credentialQuery");
//        senderRole = securityQuery.getFromRoles().get("sender");
//        serverDetails.addFromAttachment("credentialQueryFA", senderPort,
//                senderRole);
//        
//        receiverPort = database.getProvidingPort("securityManagement");
//        receiverRole = securityQuery.getToRoles().get("receiver");
//        serverDetails.addToAttachment("credentialQueryTA", receiverPort,
//                receiverRole);
//    }
    
    private static void buildCS() {
        
        client = new CSClient("client");
        cs.addComponent(client);
        
        server = new CSServer("server");
        cs.addComponent(server);
        
        CSRPC rpc = new CSRPC("RPC"); // Request
        cs.addConnector(rpc);
        
        // Request
        
        Port reqPort = client.getRequestingPort("receiveRequest");
        Role caller = rpc.getRequestingRole("receiveRequest");
        cs.addAttachment(reqPort, caller);
        
        Port proPort = server.getProvidingPort("receiveRequest");
        Role callee = rpc.getProvidingRole("receiveRequest");
        cs.addAttachment(proPort, callee);
    }
}
