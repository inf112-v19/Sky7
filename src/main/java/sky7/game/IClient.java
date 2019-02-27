package sky7.game;

import java.util.Stack;

import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.host.IHost;

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
    void getCards(Stack<ICard> draw);
}
