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
     * @return this deck
     */
    IDeck getpDeck();

    /**
     * @return this board
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

    void setBoardName(String boardName);

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
     * @param board this board
     */
    void finishedProcessing(IBoard board);

    /**
     * @param winner id of winning player
     */
    void setWinner(int winner);

    /**
     * Increase robot health if standing on wrench.
     *
     * @param playerID player to be repair
     * @param health amount to heal
     */
    void repairDamage(int playerID, int health);

    /**
     * @return playerID nr to be given to the newly connected player
     */
    int remotePlayerConnected();

    /**
     *
     * @param playerID players id
     */
    void remotePlayerDisconnected(int playerID);


    /**
     * Decrease robot health with damage amount.
     *
     * @param playerID robot to be damaged
     * @param damage amount of damage
     * @return true if damage killed robot, false otherwise.
     */
    boolean applyDamage(int playerID, int damage);

    /**
     * Informs Host that a robot has visited a flag
     * 
     * @param playerID robot that visited a flag
     * @param flagNumber integer representing the flag
     */
    void robotVisitedFlag(int playerID, int flagNumber);

    /**
     *  Call the Host to repair the robots in power down state
     */
    void powerDownRepair(boolean [] currentPD);

    /**
     * Check if robot loose a life token
     *
     * @param playerID robot to check
     * @return true if robot lose life token, false otherwise.
     */
    boolean loseLifeToken(int playerID);
}
