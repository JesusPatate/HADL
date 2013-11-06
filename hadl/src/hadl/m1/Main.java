package hadl.m1;

import hadl.m1.cs.CSClient;
import hadl.m1.cs.CSConfiguration;
import hadl.m1.cs.CSRPC;
import hadl.m1.cs.CSServer;
import hadl.m1.serverDetails.ClearanceQuery;
import hadl.m1.serverDetails.ConnectionManager;
import hadl.m1.serverDetails.Database;
import hadl.m1.serverDetails.SecurityManager;
import hadl.m1.serverDetails.ServerDetailsConfiguration;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.Port;
import hadl.m2.connector.Role;


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
            
            database.addUser("monLogin", "monPwd");
            
            // Send SQL query
            
            String query = "SELECT * FROM data";
            String login = "monLogin";
            String pwd = "monPwd";
            
            client.query(query, login, pwd);
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
        
        // SecurityQuery securityQuery = new SecurityQuery("securityQuery");
        // serverDetails.addConnector(securityQuery);
        //
        // senderPort = securityMgr.getRequestingPort("credentialQuery");
        // senderRole = securityQuery.getRoles().get("sender");
        // serverDetails.addAttachment("credentialQueryFA", senderPort,
        // senderRole);
        //
        // receiverPort = database.getProvidingPort("securityManagement");
        // receiverRole = securityQuery.getRoles().get("receiver");
        // serverDetails.addAttachment("credentialQueryTA", receiverPort,
        // receiverRole);
    }
    
    private static void buildCS() throws NoSuchServiceException,
            NoSuchPortException {
        
        client = new CSClient("client");
        cs.addComponent(client);
        
        server = new CSServer("server");
        cs.addComponent(server);
        
        CSRPC rpc = new CSRPC("RPC"); // Request
        cs.addConnector(rpc);
        
        Port reqPort = client.getRequestingPort("receiveRequest");
        Role caller = rpc.getRoles().get("caller");
        cs.addAttachment("receiveRequest", reqPort, caller);
        
        Port proPort = server.getProvidingPort("receiveRequest");
        Role callee = rpc.getRoles().get("callee");
        cs.addAttachment("receiveRequest", proPort, callee);
        
        // Binding
        
        Port port1 = server.getProvidingPort("receiveRequest");
        Port port2 = connectionMgr.getProvidingPort("receiveRequest");
        
        server.removeConnection("receiveRequest");
        cs.addBinding("receiveRequest", port1, port2);
    }
}
