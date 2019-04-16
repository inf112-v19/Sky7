package sky7.host;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import sky7.card.ICard;
import sky7.net.KryoRegister;
import sky7.net.packets.Hand;
import sky7.net.packets.ProcessRound;
import sky7.net.packets.RegistryDiscard;

public class HostNetHandler {
    
    Server server;
    IHost host;
    
    public HostNetHandler(IHost host) throws IOException {
        this.host = host;
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
        server.sendToTCP(playerID, h); //need to resolve connection ID =/= player ID
    }
    
    public void distributeRegistries(HashMap<Integer, ArrayList<ICard>> registries) {
        ProcessRound pr = new ProcessRound();
        pr.registries = registries;
        server.sendToAllTCP(pr);
    }
    
    private class HostListener extends Listener {
        public void connected (Connection connection) {
            host.remotePlayerConnected(connection.getID());
            System.out.println("Client connected, ID: " + connection.getID() + ", IP: " + connection.getRemoteAddressTCP().toString());
        }

        public void disconnected (Connection connection) {
            System.out.println("Client disconnected, ID: " + connection.getID());
        }

        public void received (Connection connection, Object object) {
            if (object instanceof RegistryDiscard) {
                host.ready(connection.getID()+1, ((RegistryDiscard)object).registry, ((RegistryDiscard)object).discard);
            }
        }
    }
}
