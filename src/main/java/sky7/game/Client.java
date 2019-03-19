package sky7.game;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.board.IBoardGenerator;
import sky7.card.ICard;
import sky7.card.IProgramCard;
import sky7.host.IHost;
import sky7.player.IPlayer;
import sky7.player.Player;

public class Client implements IClient {

    private IBoard board; //TODO double check the code, might contain problems.
    private IHost host;
    private IPlayer player;
    private STATE state;
    private String boardName;


    public Client() {
        //board = new Board(10,8);
        this.player = new Player();
        state = STATE.LOADING;
    }

    @Override
    public IBoard gameBoard() {
        return this.board;
    }

    @Override
    public void connect(IHost host, int playerNumber) {
        connect(host,playerNumber,""); //TODO add a default board for the game or something.
    }

    @Override
    public void connect(IHost host, int playerNumber, String boardName) {
        this.host = host;
        player.setPlayerNumber(playerNumber);
        this.boardName = boardName;
        
    }

    @Override
    public void chooseCards(ArrayList<ICard> hand) {
        player.clearRegistry();
        player.setHand(hand);
        state = STATE.CHOOSING_CARDS;
    }


    public void generateBoard() throws FileNotFoundException {
        IBoardGenerator generator = new BoardGenerator();
        board = generator.getBoardFromFile(boardName);
        state = STATE.MOVING_ROBOT;
        board.placeRobot(0, 5, 5);
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
        return player.getHand();
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

    @Override
    public void placeRobot(int playerNr, int xPos, int yPos) {
        board.placeRobot(playerNr, xPos, yPos);

    }

    @Override
    public void activateCard(int playerNr, IProgramCard card) {
        if( card.moveType() )
            board.moveRobot(playerNr, card.move());
        else
            board.rotateRobot(playerNr, card.rotate());

    }

    @Override
    public void activateBoardElements() {
        //board.moveConveyors();
        board.rotateCogs();

    }

    @Override
    public void activateLasers() {
        //TODO should call board.activateLasers

    }

    /**
     * @param programCardsString a string representation of programcards
     * @return a list of IProgramCards
     */
    private ArrayList<ICard> convertStringToProgramCards(String programCardsString) {
        return null ;//TODO
    }
}
