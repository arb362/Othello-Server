package gameServer;

import java.net.InetSocketAddress;

/**
 *
 * @author ablankenship
 */
public class GameServer {
    public static void main(String args[]){
        System.out.println("Server Starting up on port 10101");
        System.out.println("Press ctrl-c to exit");
        
        Server server = new Server(new InetSocketAddress(10101) );
        server.start();
        for(;;);
    }
}