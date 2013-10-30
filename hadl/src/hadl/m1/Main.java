package hadl.m1;

import hadl.m1.cs.client.CSClient;
import hadl.m1.cs.client.ReceiveRequestPort;
import hadl.m1.cs.client.ReceiveRequestService;
import hadl.m1.cs.client.ReceiveResponsePort;
import hadl.m1.cs.client.ReceiveResponseService;
import hadl.m1.cs.client.SendRequestPort;
import hadl.m1.cs.client.SendRequestService;
import hadl.m1.cs.rpc.CSRPC;
import hadl.m1.cs.server.CSServer;
import hadl.m1.cs.server.SendResponseService;
import hadl.m2.component.NoSuchPortException;
import hadl.m2.component.NoSuchServiceException;
import hadl.m2.component.ProvidedPort;
import hadl.m2.component.RequiredPort;
import hadl.m2.connector.FromRole;
import hadl.m2.connector.ToRole;


public class Main {
    
    private static CSClient client = new CSClient("client");
    
    private static CSServer server = new CSServer("server");
    
    private static CSRPC rpc1 = new CSRPC("RPC1");
    
    private static CSRPC rpc2 = new CSRPC("RPC2");
    
    private static CSConfiguration config = new CSConfiguration("cs");
    
    public static void main(String[] args) {
        try {
            buildClient();
            buildServer();
            buildConfiguration();
            
            MessageImpl msg = new MessageImpl("test");
            
            SendRequestService sendRequest = (SendRequestService)
                    client.getProvidedServices().get("sendRequest");
            
            sendRequest.send(msg);
        }
        catch (NoSuchServiceException e) {
            e.printStackTrace();
        }
        catch (NoSuchPortException e) {
            e.printStackTrace();
        }
    }
    
    private static void buildClient() throws NoSuchServiceException,
            NoSuchPortException {
        
        ReceiveRequestService s = new ReceiveRequestService("receiveRequest");
        
        client.addRequiredService(s);
        client.addRequiredPort(new ReceiveRequestPort("receiveRequest"));
        
        client.addProvidedService(new SendRequestService("sendRequest", s));
        client.addProvidedService(new ReceiveResponseService("receiveResponse"));
        
        client.addProvidedPort(new SendRequestPort("sendRequest"));
        client.addProvidedPort(new ReceiveResponsePort("receiveResponse"));
        
        client.addProvidedConnection("sendConnection", "sendRequest",
                "sendRequest");
        client.addProvidedConnection("receiveResponse", "receiveResponse",
                "receiveResponse");
        
        client.addRequiredConnection("receiveRequest", "receiveRequest",
                "receiveRequest");
    }
    
    private static void buildServer() throws NoSuchServiceException,
            NoSuchPortException {
        
        hadl.m1.cs.server.ReceiveResponseService reqS =
                new hadl.m1.cs.server.ReceiveResponseService("receiveResponse");
        
        server.addRequiredService(reqS);
        
        SendResponseService proS =
                new SendResponseService("sendResponse", reqS);
        
        server.addProvidedService(proS);
        server.addProvidedService(
                new hadl.m1.cs.server.ReceiveRequestService("receiveRequest",
                        proS));
        
        server.addRequiredPort(
                new hadl.m1.cs.server.ReceiveResponsePort("receiveResponse"));
        
        server.addProvidedPort(
                new hadl.m1.cs.server.ReceiveRequestPort("receiveRequest"));
        server.addProvidedPort(
                new hadl.m1.cs.server.SendResponsePort("sendResponse"));
        
        server.addProvidedConnection("receiveRequest", "receiveRequest",
                "receiveRequest");
        server.addProvidedConnection("sendResponse", "sendResponse",
                "sendResponse");
        server.addRequiredConnection("receiveResponse", "receiveResponse",
                "receiveResponse");
    }
    
    private static void buildConfiguration() {
        config.addComponent(client);
        config.addComponent(server);
        config.addConnector(rpc1); // Request
        config.addConnector(rpc2); // Response
        
        // Request
        
        RequiredPort reqPort = client.getRequestingPort("receiveRequest");
        ProvidedPort proPort = server.getProvidingPort("receiveRequest");
        
        FromRole caller = rpc1.getFromRoles().get("caller");
        ToRole callee = rpc1.getToRoles().get("callee");
        
        config.addFromAttachment("receiveRequest", reqPort, caller);
        config.addToAttachment("receiveRequest", proPort, callee);
        
        // Response
        
        reqPort = server.getRequestingPort("receiveResponse");
        proPort = client.getProvidingPort("receiveResponse");
        
        caller = rpc2.getFromRoles().get("caller");
        callee = rpc2.getToRoles().get("callee");
        
        config.addFromAttachment("receiveResponse", reqPort, caller);
        config.addToAttachment("receiveResponse", proPort, callee);
    }
}
