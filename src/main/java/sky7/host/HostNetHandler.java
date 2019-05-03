package sky7.host;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import sky7.card.ICard;
import sky7.net.KryoRegister;
import sky7.net.packets.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HostNetHandler {
    
    Server server;
    IHost host;
    HashMap<Integer, Integer> playerToConnection;
    HashMap<Integer, Integer> connectionToPlayer;
    
    public HostNetHandler(IHost host) throws IOException {
        playerToConnection = new HashMap<>();
        connectionToPlayer = new HashMap<>();
        this.host = host;
        server = new Server();
        new KryoRegister(server.getKryo());
        server.addListener(new HostListener());
        server.start();
        server.bind(27273);
    }
    
    public int getNumberOfConnectedPlayers() {
        return server.getConnections().length;
    }

    /**
     * Deal cards to playerID
     *
     * @param playerID robot to get cards
     * @param cards cards to be dealt
     */
    public void dealCards(int playerID, ArrayList<ICard> cards) {
        Hand h = new Hand();
        h.cards = cards;
        server.sendToTCP(playerToConnection.get(playerID), h);
    }

    /**
     * Distribute registries
     *
     * @param registries to be distribute
     * @param powerDown list of every power down
     */
    public void distributeRegistries(HashMap<Integer, ArrayList<ICard>> registries, boolean[] powerDown) {
        ProcessRound pr = new ProcessRound();
        pr.registries = registries;
        pr.powerDown = powerDown;
        server.sendToAllTCP(pr);
    }

    /**
     * Distribute board
     *
     * @param boardName string representing the name of the board
     */
    public void distributeBoard(String boardName) {
        Begin b = new Begin();
        b.boardName = boardName;
        server.sendToAllTCP(b);
    }
    
    public void placeRobot(int playerID, int xPos, int yPos) {
        PlaceRobot pr = new PlaceRobot();
        pr.playerID = playerID;
        pr.xPos = xPos;
        pr.yPos = yPos;
        server.sendToAllTCP(pr);
    }

    /**
     * place playerID in their start position.
     *
     * @param playerID robot to be placed
     * @param startPosition position to be placed at
     */
    public void placeRobotAtStart(int playerID, Vector2 startPosition) {
        PlaceRobotAtStart pr = new PlaceRobotAtStart();
        pr.playerID = playerID;
        pr.startPosition = startPosition;
        server.sendToAllTCP(pr);
    }

    private class HostListener extends Listener {
        public void connected (Connection connection) {
            int newPlayerID = host.remotePlayerConnected();
            System.out.println("Client connected, ID: " + connection.getID() + ", IP: " + connection.getRemoteAddressTCP().toString());
            playerToConnection.put(newPlayerID, connection.getID());
            connectionToPlayer.put(connection.getID(), newPlayerID);
            ClientConnectionAccepted cca = new ClientConnectionAccepted();
            cca.playerID = newPlayerID;
            server.sendToTCP(connection.getID(), cca);
            
            //inform all connected clients that number of players increased
            NumberOfPlayers nop = new NumberOfPlayers();
            nop.nPlayers = server.getConnections().length+1;
            server.sendToAllTCP(nop);
            connection.setKeepAliveTCP(5000);
        }
        
        public void disconnected (Connection connection) {
            System.out.println("Client disconnected, ID: " + connectionToPlayer.get(connection.getID()));
            host.remotePlayerDisconnected(connectionToPlayer.get(connection.getID()));
        }

        public void received (Connection connection, Object object) {
            if (object instanceof RegistryDiscard) {
                RegistryDiscard rd = (RegistryDiscard)object;
                host.ready(connectionToPlayer.get(connection.getID()), 
                        rd.registry, 
                        rd.discard,
                        rd.powerDown);
            }
        }
    }
}
