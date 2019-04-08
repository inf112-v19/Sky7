package sky7.host;

import java.util.ArrayList;
import java.util.Stack;

import sky7.card.ICard;
import sky7.game.GameClient;

public interface IHost {


    /**
     * Terminate the game served by the host.
     */
    void terminate();

    /**
     * @return the number of rounds played.
     */
    int getRoundNr();

    /**
     * @return the number of player registered for the game served by host.
     */
    int getnPlayers();


    /**
     * @param player the player that want to enter the game
     * @return true if player can be added to the game hosted by host
     */
    boolean addPlayer(GameClient player);

    /**
     * Method to be called by players, to inform host of which cards are chosen to play, and in what order
     *
     * @param pN       player number
     * @param registry the chosen cards to play
     * @param discard  the discarded cards
     */
    void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard);

    /**
     * @return the number of players ready to play.
     */
    int getReadyPlayers();

    /**
     * @return the board name that the host is serving the game on
     */
    String getBoardName();
}
