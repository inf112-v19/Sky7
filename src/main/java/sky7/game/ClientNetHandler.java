package sky7.game;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import sky7.net.KryoRegister;

public class ClientNetHandler {

    Client client;
    
    public ClientNetHandler(String host, int tcpPort) throws IOException {
        client = new Client();
        new KryoRegister(client.getKryo());
        client.start();
        client.addListener(new ClientListener());
        client.connect(6000, host, tcpPort);
        
    }

    private class ClientListener extends Listener {
        public void received (Connection connection, Object object) {
            
        }
    }
    
}
