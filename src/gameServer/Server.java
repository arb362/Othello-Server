/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 *
 * @author ablankenship
 */
public class Server extends WebSocketServer {

    public Server(InetSocketAddress address) {
        super(address);
    }
    private HashMap<WebSocket,String> clients=new HashMap<>();
    private HashMap<String,ArrayList<WebSocket>> games = new HashMap<>();
            
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Connected");        
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        ((ArrayList)games.get(clients.get(conn))).remove(conn);
        clients.keySet().remove(conn);
        
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        if(!clients.containsKey(conn)){
            String id = message.substring(0, message.indexOf(":"));
            clients.put(conn, message.substring(0, message.indexOf(":")));
            if(!games.containsKey(id)){
                games.put(id, new ArrayList<>());
                ((ArrayList)(games.get(id))).add(conn);
            } else{
                ((ArrayList)(games.get(id))).add(conn);
            }
            ArrayList connections = ((ArrayList)(games.get(id)));
            System.out.println("Checking Size");
            if(connections.size()==2){
                if(Math.random() > .5){
                    System.out.println("Sent 1");
                    ((WebSocket)(connections.get(0))).send("Player:2");
                    ((WebSocket)(connections.get(1))).send("Player:1");
                } else{
                    System.out.println("Sent 2");
                    ((WebSocket)(connections.get(1))).send("Player:2");
                    ((WebSocket)(connections.get(0))).send("Player:1");
                }
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
       String id = clients.get(conn);
       WebSocket sendConn;
       if( conn == ((ArrayList)(games.get(id))).get(0)){
           sendConn = ((WebSocket)((ArrayList)(games.get(id))).get(1));
       }else{
           sendConn = ((WebSocket)((ArrayList)(games.get(id))).get(0));
       }
        System.out.println("Got message from" + conn.toString());
        System.out.println("Sending message to"+  sendConn.toString());
       sendConn.send(message);
       
    }
    
    

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("ERROR:");
        System.out.println(ex.toString());
    }
    
}
