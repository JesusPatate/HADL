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
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;


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
        try {
            buildServerDetails();
            buildCS();
            
            // Add users to database
            
            Message msg = new Message("ADMQUERY", "\'add\',\'monLogin\',\'monPwd\'");
            
            ProvidedPort port = database.getProvidingPort("manageUsers");
            port.receive(msg);
            
            msg = new Message("ADMQUERY", "\'add\',\'Georges\',\'Bondiou\'");
            port.receive(msg);
            
            // Send SQL query
            
            msg = new Message("QUERY", "'Georges','Bondiou',"
                    + "'SELECT * FROM data'");
            
            port = client.getProvidingPort("sendRequest");
            port.receive(msg);
        }
        catch (NoSuchServiceException e) {
            e.printStackTrace();
        }
        catch (NoSuchPortException e) {
            e.printStackTrace();
        }
    }
    
    private static void buildServerDetails() throws NoSuchServiceException,
            NoSuchPortException {
        
        connectionMgr = new ConnectionManager("connectionManager");
        serverDetails.addComponent(connectionMgr);
        
        database = new Database("database");
        serverDetails.addComponent(database);
        
        securityMgr = new SecurityManager("securityManager");
        serverDetails.addComponent(securityMgr);
        
        // Clearance query
        
        ClearanceQuery clearanceQuery = new ClearanceQuery("clearanceQuery");
        serverDetails.addConnector(clearanceQuery);
        
        RequiredPort senderPort =
                connectionMgr.getRequestingPort("securityAuthorization");
        FromRole senderRole = clearanceQuery.getFromRoles().get("sender");
        serverDetails.addFromAttachment("clearanceQueryFA", senderPort,
                senderRole);
        
        ProvidedPort receiverPort =
                securityMgr.getProvidingPort("securityAuthorization");
        ToRole receiverRole = clearanceQuery.getToRoles().get("receiver");
        serverDetails.addToAttachment("clearanceQueryTA", receiverPort,
                receiverRole);
        
        // Security query
        
        SecurityQuery securityQuery = new SecurityQuery("securityQuery");
        serverDetails.addConnector(securityQuery);
        
        senderPort = securityMgr.getRequestingPort("credentialQuery");
        senderRole = securityQuery.getFromRoles().get("sender");
        serverDetails.addFromAttachment("credentialQueryFA", senderPort,
                senderRole);
        
        receiverPort = database.getProvidingPort("securityManagement");
        receiverRole = securityQuery.getToRoles().get("receiver");
        serverDetails.addToAttachment("credentialQueryTA", receiverPort,
                receiverRole);
    }
    
    private static void buildCS() throws NoSuchServiceException,
            NoSuchPortException {
        
        client = new CSClient("client");
        cs.addComponent(client);
        
        server = new CSServer("server");
        cs.addComponent(server);
        
        CSRPC rpc1 = new CSRPC("RPC1"); // Request
        cs.addConnector(rpc1);
        
        CSRPC rpc2 = new CSRPC("RPC2"); // Response
        cs.addConnector(rpc2);
        
        // Request
        
        RequiredPort reqPort = client.getRequestingPort("receiveRequest");
        FromRole caller = rpc1.getFromRoles().get("caller");
        cs.addFromAttachment("receiveRequest", reqPort, caller);
        
        ProvidedPort proPort = server.getProvidingPort("receiveRequest");
        ToRole callee = rpc1.getToRoles().get("callee");
        cs.addToAttachment("receiveRequest", proPort, callee);
        
        // Response
        
        reqPort = server.getRequestingPort("receiveResponse");
        caller = rpc2.getFromRoles().get("caller");
        cs.addFromAttachment("receiveResponse", reqPort, caller);
        
        proPort = client.getProvidingPort("receiveResponse");
        callee = rpc2.getToRoles().get("callee");
        cs.addToAttachment("receiveResponse", proPort, callee);
        
        // Binding
        
        ProvidedPort port1 = server.getProvidedPorts().get("receiveRequest");
        ProvidedPort port2 =
                connectionMgr.getProvidedPorts().get("externalSocket");
        
        ProvidedConnection con =
                server.getProvidedConnections().get("receiveRequest");
        
        server.removeProvidedConnection(con);
        cs.addProvidedBinding("receiveRequest", port1, port2);
    }
}
