package hadl.m1;

import hadl.m1.cs.CSClient;
import hadl.m1.cs.CSConfiguration;
import hadl.m1.cs.CSRPC;
import hadl.m1.cs.CSServer;
import hadl.m1.serverDetails.ClearanceQuery;
import hadl.m1.serverDetails.ConnectionManager;
import hadl.m1.serverDetails.Database;
import hadl.m1.serverDetails.SecurityManager;
import hadl.m1.serverDetails.SecurityQuery;
import hadl.m1.serverDetails.ServerDetailsConfiguration;
import hadl.m2.Message;
import hadl.m2.component.Connection;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.Port;
import hadl.m2.connector.FromRole;
import hadl.m2.connector.Role;
import hadl.m2.connector.ToRole;


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
//            buildServerDetails();
            buildCS();
            
            // Add users to database
            
//            Message msg = new Message("ADMQUERY", "\'add\',\'monLogin\',\'monPwd\'");
//            
//            Port port = database.getProvidingPort("manageUsers");
//            port.receive(msg, null);
//            
//            msg = new Message("ADMQUERY", "\'add\',\'Georges\',\'Bondiou\'");
//            port.receive(msg, null);
            
            // Send SQL query
            
            Message msg = new Message("QUERY", "'Georges','Bondiou',"
                    + "'SELECT * FROM data'");
            
            Port port = client.getProvidingPort("sendRequest");
            port.receive(msg, null);
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
        
        Port senderPort =
                connectionMgr.getRequestingPort("securityAuthorization");
        Role senderRole = clearanceQuery.getRoles().get("sender");
        serverDetails.addAttachment("clearanceQueryFA", senderPort,
                senderRole);
        
        Port receiverPort =
                securityMgr.getProvidingPort("securityAuthorization");
        Role receiverRole = clearanceQuery.getRoles().get("receiver");
        serverDetails.addAttachment("clearanceQueryTA", receiverPort,
                receiverRole);
        
        // Security query
        
        SecurityQuery securityQuery = new SecurityQuery("securityQuery");
        serverDetails.addConnector(securityQuery);
        
        senderPort = securityMgr.getRequestingPort("credentialQuery");
        senderRole = securityQuery.getRoles().get("sender");
        serverDetails.addAttachment("credentialQueryFA", senderPort,
                senderRole);
        
        receiverPort = database.getProvidingPort("securityManagement");
        receiverRole = securityQuery.getRoles().get("receiver");
        serverDetails.addAttachment("credentialQueryTA", receiverPort,
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
        
        Port reqPort = client.getRequestingPort("receiveRequest");
        Role caller = rpc1.getRoles().get("caller");
        cs.addAttachment("receiveRequest", reqPort, caller);
        
        Port proPort = server.getProvidingPort("receiveRequest");
        Role callee = rpc1.getRoles().get("callee");
        cs.addAttachment("receiveRequest", proPort, callee);
        
        // Response
        
//        reqPort = server.getRequestingPort("receiveResponse");
//        caller = rpc2.getRoles().get("caller");
//        cs.addAttachment("receiveResponse", reqPort, caller);
//        
//        proPort = client.getProvidingPort("receiveResponse");
//        callee = rpc2.getRoles().get("callee");
//        cs.addAttachment("receiveResponse", proPort, callee);
        
        // Binding
        
//        Port port1 = server.getPorts().get("receiveRequest");
//        Port port2 = connectionMgr.getPorts().get("externalSocket");
//        
//        Connection con = server.getConnections().get("receiveRequest");
//        
//        server.removeProvidedConnection(con);
//        cs.addBinding("receiveRequest", port1, port2);
    }
}
