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
            e.printStackTrace();
        }
    }
    
    @Test
    public void testHand() {
        deck = new ProgramDeck();
        
        // simulate 100 rounds of drawing and returning cards
        for (int j=0; j<100 ; j++) {
            ArrayList<ICard> given = deck.draw(9);  
            clients.get(0).chooseCards(given);
            
            assertEquals(given, clients.get(0).getHand());
            
            // simulate player choosing the first 5 cards in hand
            for (int i=0; i<5 ; i++) {
                clients.get(0).setCard(given.get(i), i);
            }

            // assert that the stored registry is made up of the chosen cards each round
            ArrayList<ICard> reg = clients.get(0).getPlayer().getRegistry();
            assertEquals(reg.size(), 5);
            for (int i=0; i<5 ; i++) {
                assertTrue(((ProgramCard)reg.get(i)).equals(((ProgramCard)given.get(i))));
            }
            
            deck.returnCards(given);
        }
    }
}
