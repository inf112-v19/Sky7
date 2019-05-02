package sky7.Client;

import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.host.IHost;
import sky7.player.IPlayer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IClient {

    /**
     * Return the board that the current game is being played on.
     *
     * @return A board
     */
    IBoard gameBoard();


    /**
     * @param host the host that this client is playing at.
     * @param playerNumber the number of the player on this client.
     */
    void connect(IHost host, int playerNumber);

    void connect(IHost host, int playerNumber, String boardName);

    void connect(int playerNumber);

    /**
     * A method called by host to give the player program cards
     *
     * @param draw
     */
    void chooseCards(ArrayList<ICard> draw);

    /**
     * Generate a board (TODO by reading in a JSON file)
     *
     * @throws FileNotFoundException Throws a exception if file not found
     */
    void generateBoard() throws FileNotFoundException;

    /**
     * @return the player to this client
     */
    IPlayer getPlayer();

    /**
     * @return the state of this client.
     */
    STATE getState();

    /**
     * @return the cards that the player can choose from. (called by gui)
     */
    ArrayList<ICard> getHand();

    /**
     * @param chosenCard         the cards chosen by the player. (called by gui)
     * @param positionInRegistry the position of the card chosen in the registry.
     */
    void setCard(ICard chosenCard, int positionInRegistry);

    /**
     * locked the registry, such that the player cannot choose cards anymore for the current round.
     */
    void lockRegistry();

    /**
     * Calls board.placeRobot
     * called from host
     *
     * @param playerNr
     * @param xPos
     * @param yPos
     */
    void placeRobot (int playerNr, int xPos, int yPos);

    /**
     * activate the board elements by calling board method
     * called from host
     */
    void activateBoardElements ();

    /**
     * activate the lasers by calling board method.
     * called from host
     */
    void activateLasers();


    void finishedProcessing(IBoard board);

    void render(HashMap<Integer,ArrayList<ICard>> cards, boolean[] powerDown);

    void join(String hostName);

    int getNPlayers();

    void updateNPlayers(int nPlayers);

    void setBoardName(String boardName);

    boolean readyToRender();

    /**
     * Check if playerId is this player, if so then subtract damage from players health.
     * @param playerID the player to apply health to
     * @param damage integer representing damage
     */
    void applyDamage(int playerID, int damage);

    /**
     *
     * @param board
     */
    void updateBoard(IBoard board);

    /**
     * Check if playerId is this player, if so then add health to players health.
     * @param playerID the player to apply health to
     * @param health integer representing health
     */
    void repairDamage(int playerID, int health);

    void powerDown();

    /**
     *  Call client to repair robots in power down state
     */
    void powerDownRepair(boolean[] currentPD);
}
