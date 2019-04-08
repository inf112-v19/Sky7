package sky7.host;

import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import sky7.card.ICard;
import sky7.net.KryoRegister;
import sky7.net.packets.Hand;

public class HostNetHandler {
    
    Server server;
    
    public HostNetHandler(IHost host) throws IOException {

        server = new Server();
        new KryoRegister(server.getKryo());
        server.addListener(new HostListener());
        server.start();
        server.bind(27273, 27283);
    }
    
    public int getNumberOfConnectedPlayers() {
        return server.getConnections().length;
    }
    
    public void dealCards(int playerID, ArrayList<ICard> cards) {
        Hand h = new Hand();
        h.cards = cards;
        server.sendToTCP(playerID, h);
    }
    
    private class HostListener extends Listener {
        public void connected (Connection connection) {
            
        }

        public void disconnected (Connection connection) {
            
        }

        public void received (Connection connection, Object object) {
            
        }
    }
}
