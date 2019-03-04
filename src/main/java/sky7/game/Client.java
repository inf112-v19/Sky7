package sky7.game;

import java.util.ArrayList;

import sky7.board.Board;
import sky7.card.ICard;
import sky7.host.IHost;
import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.board.IBoardGenerator;
import sky7.card.IProgramCard;
import sky7.player.IPlayer;
import sky7.player.Player;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Client implements IClient {

    public static final int MAX_NUMBER_OF_REGISTRY = 6;
    private IBoard board; //TODO double check the code, might contain problems.
    private IHost host;
    private ArrayList<ICard> hand;
    private IPlayer player;
    private STATE state;


    public Client() {
        //board = new Board(10,8);
        board = new Board(10, 8);
        hand = new ArrayList<>(MAX_NUMBER_OF_REGISTRY);
        this.player = new Player();
        state = STATE.LOADING;
    }

    @Override
    public IBoard gameBoard() {
        return this.board;
    }

    @Override
    public void connect(IHost host) {
        this.host = host;

    }

    @Override
    public void chooseCards(ArrayList<ICard> draw) { // Same as get registery
        hand = draw;
    }

    @Override
    public void temp() {
        System.out.println("player 0 clicked ready");
        host.ready(0, hand, hand);
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
    public String getRegistry() {
        return Arrays.deepToString(player.getRegistry());
    }

    @Override
    public void newCards(String programCards) {
        state = STATE.CHOOSING_CARDS;
        player.setHand(convertStringToProgramCards(programCards));
    }

    @Override
    public STATE getState() {
        return state;
    }

    @Override
    public IProgramCard[] getHand() {
        return player.getHand(); //TODO
    }

    @Override
    public void setCard(IProgramCard chosenCard, int positionInRegistry) {
        //choosingCards[positionInRegistry] = chosenCard;
    }

    @Override
    public void lockRegistry() {
        //TODO to check, what if the player did not choose 6 cards?
        //player.setRegistry(choosingCards);
    }

    /**
     * @param programCardsString a string representation of programcards
     * @return a list of IProgramCards
     */
    private IProgramCard[] convertStringToProgramCards(String programCardsString) {
        return new IProgramCard[0];//TODO
    }
}
