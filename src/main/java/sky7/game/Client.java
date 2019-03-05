package sky7.game;

import java.util.ArrayList;

import sky7.board.Board;
import sky7.card.ICard;
import sky7.host.IHost;
import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.board.IBoardGenerator;
import sky7.player.IPlayer;
import sky7.player.Player;

import java.io.FileNotFoundException;

public class Client implements IClient {

    public static final int MAX_NUMBER_OF_REGISTRY = 6;
    private IBoard board; //TODO double check the code, might contain problems.
    private IHost host;
    private IPlayer player;
    private STATE state;


    public Client() {
        //board = new Board(10,8);
        board = new Board(10, 8);
        this.player = new Player();
        state = STATE.LOADING;
    }

    @Override
    public IBoard gameBoard() {
        return this.board;
    }

    @Override
    public void connect(IHost host, int playerNumber) {
        this.host = host;
        player.setPlayerNumber(playerNumber);

    }

    @Override
    public void chooseCards(ArrayList<ICard> hand) { // Same as get registery
        player.setHand(hand);
        state = STATE.CHOOSING_CARDS;
    }


    public void generateBoard() throws FileNotFoundException {
        IBoardGenerator generator = new BoardGenerator();
        board = generator.getBoardFromFile("assets/Boards/emptyBoard.json");
        state = STATE.MOVING_ROBOT;
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }


    @Override
    public STATE getState() {
        return state;
    }

    @Override
    public ArrayList<ICard> getHand() {
        return player.getHand(); //TODO
    }

    @Override
    public void setCard(ICard chosenCard, int positionInRegistry) {
        player.setCard(chosenCard,positionInRegistry);
    }

    @Override
    public void lockRegistry() {
        //TODO to check, what if the player did not choose 6 cards?
        //player.setRegistry(choosingCards)

        state = STATE.READY;
        host.ready(player.getPlayerNumber(),player.getRegistry(),player.getDiscard());
    }

    /**
     * @param programCardsString a string representation of programcards
     * @return a list of IProgramCards
     */
    private ArrayList<ICard> convertStringToProgramCards(String programCardsString) {
        return null ;//TODO
    }
}
