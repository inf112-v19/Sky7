package sky7.client;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import sky7.card.ICard;
import sky7.card.ProgramCard;
import sky7.card.ProgramDeck;
import sky7.game.Client;
import sky7.game.IClient;
import sky7.host.Host;
import sky7.host.IHost;

public class ClientTest {

    static List<IClient> clients;
    static final int N_CLIENTS = 8;
    ProgramDeck deck;
    
    public static void main(String[] args) {
        
    }
    
    @BeforeClass
    public static void testSetup() {
        clients = new ArrayList<IClient>();
        for (int i=0; i<N_CLIENTS ; i++) {
            clients.add(new Client());
        }
        
        IHost host = new Host();
        
        
        for (int i=0; i<N_CLIENTS ; i++) {
            clients.get(i).connect(host, i, "assets/Boards/emptyBoard.json");
            
            assertEquals(clients.get(i).getPlayer().getPlayerNumber(), i);
        }
        
        setupBoard();
    }

    public static void setupBoard() {
        try {
            clients.get(0).generateBoard();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testHand() {
        deck = new ProgramDeck();
        ArrayList<ICard> given = deck.draw(9);  
        clients.get(0).chooseCards(given);
        
        assertEquals(given, clients.get(0).getHand());
        
    }
    
    @Test
    public void testRobot() {
        // at the moment, client places robot at 5,5 in generateBoard() method, otherwise we would have to place it now
        clients.get(0).activateCard(0, new ProgramCard(0,2,0));
        
        
    }
}
