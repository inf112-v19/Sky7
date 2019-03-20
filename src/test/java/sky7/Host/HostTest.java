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
    public void setUp() {
        //TODO a Host needs a client
        clients = new Client[NR_OF_CLIENTS];
        host = new Host();

    }
    // TODO Test number of clients connected to a host
    // TODO Test the id of a client when the client is connected to a host
    // TODO Test that pDeck is full after construction, before playing run.


    // TestRun. Test stages of run using a fake client.
    // TODO #1 of run: test that pDeck is 9 cards less when cards are dealt.
    // TODO #1.1 of run: test that 9 cards sendt to client are do not contain duplicates.
    // TODO #1.2 of run: test that the set cards returned from client are subset of the cards sendt from Host.

    @Test
    public void hostShouldDeal9Cards() {
        Client testClient1 = clients[1];

        testClient1 = new Client();
        testClient1.connect(host, 0, "assets/Boards/emptyBoard.json");
        int originalSizeOfDeck = host.getpDeck().getSize();
        host = new Host(testClient1);

        int sizeOfDeckafterCardsDelt = host.getpDeck().getSize();

        assert (originalSizeOfDeck - 9 == sizeOfDeckafterCardsDelt);
        host.ready(0,testClient1.getHand(),testClient1.getHand());


        //TODO add getters in host and getSize in IDeck

    }
    // TODO #2 of run: test if host waits for 0 , 1, 2 and n clients.
    // TODO #3 of run: test if host returns that cards return back to the deck
    // TODO #4 of run: test if host runs 5 fazes.
    // TODO #5 of run: test if host runs multiple rounds.


    // TODO Test activateCard
    // TODO Test findPlayerSequence
    // TODO Test activateBoardElements
    // TODO Test activateLaser
    // TODO Test ready
}
