package sky7.game;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import sky7.Client.IClient;
import sky7.net.KryoRegister;
import sky7.net.packets.ClientConnectionAccepted;
import sky7.net.packets.Hand;

public class ClientNetHandler {

    IClient client;
    com.esotericsoftware.kryonet.Client netClient;
    
    public ClientNetHandler(IClient client, String host) throws IOException {
        this.client = client;
        netClient = new com.esotericsoftware.kryonet.Client();
        new KryoRegister(netClient.getKryo());
        netClient.start();
        netClient.addListener(new ClientListener());
        netClient.connect(6000, host, 27273);
        
    }

    private class ClientListener extends Listener {
        public void received (Connection connection, Object object) {
            if (object instanceof Hand) {
                client.chooseCards(((Hand) object).cards);
            } else if (object instanceof ClientConnectionAccepted) {
                client.connect(((ClientConnectionAccepted)object).playerID, ((ClientConnectionAccepted)object).boardName);
            }
        }
    }
    
}
