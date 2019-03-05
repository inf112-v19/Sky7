package sky7.game;

import java.util.ArrayList;

import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.host.IHost;
import sky7.card.IProgramCard;
import sky7.player.IPlayer;

import java.io.FileNotFoundException;

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
     * check if card is of type move or rotate, if move then move robot, else rotate robot
     * called from host
     *
     * @param playerNr id of the current player
     * @param card the card that should be played
     */
    void activateCard (int playerNr, IProgramCard card);

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


}
