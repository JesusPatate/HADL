package hadl.m1;

import hadl.m1.cs.CSClient;
import hadl.m1.cs.CSConfiguration;
import hadl.m1.cs.CSRPC;
import hadl.m1.cs.CSServer;
import hadl.m1.serverDetails.ConnectionManager;
import hadl.m1.serverDetails.SecurityManager;
import hadl.m1.serverDetails.ServerDetailsConfiguration;
import hadl.m2.Message;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.ProvidedConnection;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.RequiredPort;
import hadl.m2.connector.FromRole;
import hadl.m2.connector.ToRole;


public class Main {
    
    private static CSClient client;
    
    private static CSServer server;
    
    private static ConnectionManager conMgr;
    
    private static SecurityManager secuMgr;
    
    private static CSRPC rpc1 = new CSRPC("RPC1");
    
    private static CSRPC rpc2 = new CSRPC("RPC2");
    
    private static CSConfiguration cs = new CSConfiguration("cs");
    
    private static ServerDetailsConfiguration serverDetails =
            new ServerDetailsConfiguration("serverDetails");
    
    public static void main(String[] args) {
        try {
            buildServerDetails();
            buildCS();
            
            Message msg = new Message("QUERY", "'Lundi, mardi','monPwd','SELECT * FROM data'");
            ProvidedPort port = client.getProvidingPort("sendRequest");
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
        
        conMgr = new ConnectionManager("connectionManager");
        secuMgr = new SecurityManager("securityManager");
        
        serverDetails.addComponent(conMgr);
        serverDetails.addComponent(secuMgr);
    }
    
    private static void buildCS() throws NoSuchServiceException,
            NoSuchPortException {
        
        client = new CSClient("client");
        server = new CSServer("server");
        
        cs.addComponent(client);
        cs.addComponent(server);
        cs.addConnector(rpc1); // Request
        cs.addConnector(rpc2); // Response
        
        // Request
        
        RequiredPort reqPort = client.getRequestingPort("receiveRequest");
        ProvidedPort proPort = server.getProvidingPort("receiveRequest");
        
        FromRole caller = rpc1.getFromRoles().get("caller");
        ToRole callee = rpc1.getToRoles().get("callee");
        
        cs.addFromAttachment("receiveRequest", reqPort, caller);
        cs.addToAttachment("receiveRequest", proPort, callee);
        
        // Response
        
        reqPort = server.getRequestingPort("receiveResponse");
        proPort = client.getProvidingPort("receiveResponse");
        
        caller = rpc2.getFromRoles().get("caller");
        callee = rpc2.getToRoles().get("callee");
        
        cs.addFromAttachment("receiveResponse", reqPort, caller);
        cs.addToAttachment("receiveResponse", proPort, callee);
        
        ProvidedPort port1 = server.getProvidedPorts().get("receiveRequest");
        ProvidedPort port2 = conMgr.getProvidedPorts().get("externalSocket");
        
        ProvidedConnection con =
                server.getProvidedConnections().get("receiveRequest");
        
        server.removeProvidedConnection(con);
        cs.addProvidedBinding("receiveRequest", port1, port2);
    }
}
