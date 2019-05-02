package sky7.host;

import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.card.IDeck;

import java.util.ArrayList;

public interface IHost {

    // GETTERS -------------------

    /**
     * @return The state of game during play.
     */
    HOST_STATE getCurrentState();

    /**
     * @return
     */
    IDeck getpDeck();

    /**
     * @return
     */
    IBoard getBoard();

    /**
     * @return the number of player registered for the game served by host.
     */
    int getnPlayers();

    /**
     * @return the number of players ready to play.
     */
    int getReadyPlayers();

    /**
     * @return the board name that the host is serving the game on
     */
    String getBoardName();


    // OTHER METHODS -------------------

    /**
     * Terminate the game served by the host.
     */
    void terminate();

    /**
     * Begin the game.
     */
    void Begin();

    /**
     * Method to be called by players, to inform host of which cards are chosen to play, and in what order
     *
     * @param pN       player number
     * @param registry the chosen cards to play
     * @param discard  the discarded cards
     * @param powerDown true if the player will power down next round
     */
    void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard, boolean powerDown);


    /**
     * @param board
     */
    void finishedProcessing(IBoard board);

    /**
     * @param winner
     */
    void setWinner(int winner);

    /**
     *
     * @param playerID
     * @param damage
     */
    void repairDamage(int playerID, int damage);

    /**
     * @return playerID nr to be given to the newly connected player
     */
    int remotePlayerConnected();

    /**
     *
     * @param playerID
     */
    void remotePlayerDisconnected(int playerID);


    /**
     *
     * @param playerID
     * @param damage
     */
    void applyDamage(int playerID, int damage);

    /**
     * Informs Host that a robot has visited a flag
     * 
     * @param playerID
     * @param flagNumber
     */
    void robotVisitedFlag(int playerID, int flagNumber);

    /**
     *  Call the Host to repair the robots in power down state
     */
    void powerDownRepair(boolean [] currentPD);
}
