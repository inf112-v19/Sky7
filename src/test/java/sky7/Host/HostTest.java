package sky7.Host;


import org.junit.Before;
import org.junit.Test;
import sky7.game.Client;
import sky7.host.Host;

public class HostTest {
    static final int NR_OF_CLIENTS = 10;
    static Client[] clients;
    static Host host;

    @Before
    public static void setUp() {
        //TODO a Host needs a client
        clients = new Client[NR_OF_CLIENTS];
        host = new Host();

    }

    // TestRun. Test stages of run using a fake client.
    // TODO #1 of run: test that pDeck is 9 cards less when cards are dealt.

    @Test
    public void hostShouldDeal9Cards() {
        Client testClient1 = clients[1];

        testClient1.connect(host,1,"assets/Boards/emptyBoard.json");

        int originalSizeOfDeck; //TODO add getters in host and getSize in IDeck

    }
    // TODO #2 of run: test if host waits for 0 , 1, 2 and n clients.
    // TODO #3 of run: test if host returns that cards return back to the deck
    // TODO #4 of run: test if host runs 5 fazes.
    // TODO #5 of run: test if host runs multiple rounds.

    // TODO Test number of clients connected to a host
    // TODO Test the id of a client when the client is connected to a host
    // TODO Test that pDeck is full after construction, before playing run.


    // TODO Test activateCard
    // TODO Test findPlayerSequence
    // TODO Test activateBoardElements
    // TODO Test activateLaser
    // TODO Test ready
}
