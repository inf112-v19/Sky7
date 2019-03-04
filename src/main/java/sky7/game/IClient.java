package sky7.game;

import java.util.ArrayList;
import java.util.Stack;

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

    void connect(IHost host);

    /**
     * A method called by host to give the player program cards
     *
     * @param draw
     */
    void chooseCards(ArrayList<ICard> draw);

    void temp();

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
     * @return clients player registry
     */
    String getRegistry();

    /**
     * register cards, (called by host)
     */
    void newCards(String programCards);

    /**
     * @return the state of this client.
     */
    STATE getState();

    /**
     * @return the cards that the player can choose from. (called by gui)
     */
    IProgramCard[] getHand();

    /**
     * @param chosenCard         the cards chosen by the player. (called by gui)
     * @param positionInRegistry the position of the card chosen in the registry.
     */
    void setCard(IProgramCard chosenCard, int positionInRegistry);

    /**
     * locked the registry, such that the player cannot choose cards anymore for the current round.
     */
    void lockRegistry();
}
