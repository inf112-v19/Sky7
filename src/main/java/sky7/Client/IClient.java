package sky7.Client;

import com.badlogic.gdx.math.Vector2;
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
     * @param host         the host that this client is playing at.
     * @param playerNumber the number of the player on this client.
     */
    void connect(IHost host, int playerNumber);

    /**
     * @param host the host that this client is playing at.
     * @param playerNumber the number of the player on this client.
     * @param boardName the name of the board
     */
    void connect(IHost host, int playerNumber, String boardName);

    /**
     * @param playerNumber the number of the player on this client.
     */
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
    void placeRobot(int playerNr, int xPos, int yPos);

    /**
     * when process is done.
     *
     * @param board this board
     */
    void finishedProcessing(IBoard board);

    void render(HashMap<Integer, ArrayList<ICard>> cards, boolean[] powerDown);

    /**
     * join the game
     * @param hostName string representing the name of the host
     */
    void join(String hostName);

    /**
     * @return integer representing the number of players.
     */
    int getNPlayers();

    /**
     * change NPlayers
     *
     * @param nPlayers new number of players
     */
    void updateNPlayers(int nPlayers);

    /**
     * put a name of the board.
     *
     * @param boardName new name of the board
     */
    void setBoardName(String boardName);

    /**
     * @return true when ready, false otherwise.
     */
    boolean readyToRender();

    /**
     * Check if playerId is this player, if so then subtract damage from players health.
     *
     * @param playerID the player to apply health to
     * @param damage   integer representing damage
     * @return true if the damage killed the robot.
     */
    boolean applyDamage(int playerID, int damage);

    /**
     * @param board board to update to
     */
    void updateBoard(IBoard board);

    /**
     * Check if playerId is this player, if so then add health to players health.
     *
     * @param playerID the player to apply health to
     * @param health   integer representing health
     */
    void repairDamage(int playerID, int health);

    /**
     * Player dont get new cards and can not play the next round. Repair all damage.
     */
    void powerDown();

    /**
     * Call client to repair robots in power down state
     */
    void powerDownRepair(boolean[] currentPD);

    /**
     * @return true if finish processing, false otherwise
     */
    boolean isFinishedProcessing();

    /**
     * If a robot go outside the board or lose a life token, this method should put the robot at startposition
     *
     * @param playerNr player to be put at start position
     * @param startPosition position to be put
     */
    void placeRobotAtStart(int playerNr, Vector2 startPosition);

    /**
     * @param playerID player to check
     * @return true if playerID lose a life token, false otherwise.
     */
    boolean loseLifeToken(int playerID);

    /**
     * @return true if game is over, false otherwise.
     */
    boolean isGameOver();
}
