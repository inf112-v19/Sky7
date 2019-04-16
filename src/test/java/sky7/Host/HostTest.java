package sky7.Host;


import org.junit.Before;
import org.junit.Test;
import sky7.card.ICard;
import sky7.Client.Client;
import sky7.host.HOST_STATE;
import sky7.host.Host;

import java.util.List;


public class HostTest {


    static final int NR_OF_CLIENTS = 8;
    static private Client[] clients;
    static private Host host;

    @Before
    public void setUp() {
        host = new Host();
        clients = new Client[NR_OF_CLIENTS];
        for (int i = 0; i < NR_OF_CLIENTS; i++) {
            clients[i] = new Client();
        }
        //TODO a Host needs a client

    }


    // HELPER METHODS
    private void runHost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HostTest.host.Begin();
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void prepareClient(int client) {
        clients[client].connect(host, client, host.getBoardName());
        List<ICard> hand = clients[0].getHand().subList(0, 5);
        for (int i = 0; i < hand.size(); i++) {
            clients[client].setCard(hand.get(i), i);
        }
        clients[client].lockRegistry();
    }



    // TESTS

    // Test states during run

    // STATE : BEGIN
    // TODO Test that adding the clients multiple times should not change the number of clients.
    // TODO Test number of clients connected to a host.
    // TODO Test the id of a client when the client is connected to a host
    // TODO Test that pDeck is full after construction, before playing run.
    @Test
    public void hostStateShouldBeBeginAtFirst() {
        assert (host.getCurrentState() == HOST_STATE.BEGIN);
    }


    //STATE : DEAL CARDS
    // TODO #1.1 of run: test that 9 cards sendt to client are do not contain duplicates.
    // TODO #1.2 of run: test that the set cards returned from client are subset of the cards sendt from Host.
    @Test
    public void hostShouldDeal9Cards() {
        for (int i = 0; i < 10; i++) {
            host = new Host();
            host.addPlayer(clients[0]);
            int originalSizeOfDeck = host.getpDeck().nRemainingCards();

            runHost();
            int sizeOfDeckAfterCardsDelt = host.getpDeck().nRemainingCards();

            host.terminate();
            assert (originalSizeOfDeck - 9 == sizeOfDeckAfterCardsDelt);
        }

    }


    // STATE : WAIT_FOR_CLIENTS
    @Test
    public void hostShouldWaitFor1Client() {
        host.addPlayer(clients[0]);
        runHost();
        assert (host.getCurrentState() == HOST_STATE.WAIT_FOR_PLAYERS);
        host.terminate();
    }


    @Test
    public void hostShouldWaitFor2Clients() {
        for (int i = 0; i < 10; i++) {
            host = new Host();
            host.addPlayer(clients[0]);
            host.addPlayer(clients[1]);

            runHost();

            prepareClient(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assert host.getCurrentState() == HOST_STATE.WAIT_FOR_PLAYERS;
            host.terminate();
        }
    }

    @Test
    public void hostShouldWaitForNClients() {

        for (int i = 1; i < NR_OF_CLIENTS; i++) {
            host = new Host();
            for (int j = 0; j < i; j++) {
                host.addPlayer(clients[j]);
            }

            runHost();

            for (int j = 0; j < i; j++) {
                prepareClient(j);
                assert host.getCurrentState() == HOST_STATE.WAIT_FOR_PLAYERS : "nr of players = " + j;
            }

            host.terminate();
        }
    }

    // STATE : PLAY_NEXT_ROUND
    // TODO #3 of run: test if host returns that cards return back to the deck
    // TODO #5 of run: test if host runs multiple rounds.

    // STATE : PLAY_NEXT_FAZE
    // TODO #4 of run: test if host runs 5 fazes.


    // STATE : TERMINATE
    @Test
    public void terminateMethodShouldTerminate() {
        host.terminate();
        assert (host.getCurrentState() == HOST_STATE.TERMINATED);
    }

    // METHODS
    // TODO Test activateCard
    // TODO Test findPlayerSequence
    // TODO Test activateBoardElements
    // TODO Test activateLaser
    // TODO Test ready
}
