package sky7.game;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import sky7.net.KryoRegister;
import sky7.net.packets.Hand;

public class ClientNetHandler {

    Client client;
    IGameClient gameClient;
    
    public ClientNetHandler(IGameClient gameClient, String host) throws IOException {
        this.gameClient = gameClient;
        client = new Client();
        new KryoRegister(client.getKryo());
        client.start();
        client.addListener(new ClientListener());
        client.connect(6000, host, 27273);
        
    }

    private class ClientListener extends Listener {
        public void received (Connection connection, Object object) {
            if (object instanceof Hand) {
                gameClient.chooseCards(((Hand) object).cards);
            }
        }
    }
    
}
