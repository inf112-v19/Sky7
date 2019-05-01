package sky7.Client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import sky7.card.ICard;
import sky7.net.KryoRegister;
import sky7.net.packets.*;

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
    
    public void ready(ArrayList<ICard> registry, ArrayList<ICard> discard, boolean powerDown) {
        RegistryDiscard rd = new RegistryDiscard();
        rd.registry = registry;
        rd.discard = discard;
        rd.powerDown = powerDown;
        netClient.sendTCP(rd);
    }

    private class ClientListener extends Listener {
        public void received (Connection connection, Object object) {
            if (object instanceof Hand) {
                client.chooseCards(((Hand) object).cards);
                
            } else if (object instanceof ClientConnectionAccepted) {
                client.connect(((ClientConnectionAccepted)object).playerID);
                
            } else if (object instanceof ProcessRound) {
                client.render(((ProcessRound)object).registries);
                
            } else if (object instanceof PlaceRobot) {
                PlaceRobot pr = (PlaceRobot)object;
                client.placeRobot(pr.playerID, pr.xPos, pr.yPos);
                
            } else if (object instanceof NumberOfPlayers) {
                client.updateNPlayers(((NumberOfPlayers)object).nPlayers);
                
            } else if (object instanceof Begin) {
                client.setBoardName(((Begin)object).boardName);
                try {
                    client.generateBoard();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
