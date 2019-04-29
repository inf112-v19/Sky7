package sky7.host;

import java.util.ArrayList;

import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.Client.Client;
import sky7.card.IDeck;

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
     */
    void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard);


    /**
     * @param board
     */
    void finishedProcessing(IBoard board);

    /**
     * @param winner
     */
    void setWinner(int winner);

    /**
     * @return playerID nr to be given to the newly connected player
     */
    int remotePlayerConnected();

    /**
     * @param playerID
     */
    void remotePlayerDisconnected(int playerID);


    /**
     * @param playerID
     * @param damage
     */
    void applyDamage(int playerID, int damage);
}
