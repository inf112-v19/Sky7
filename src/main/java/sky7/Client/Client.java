package sky7.Client;

import com.badlogic.gdx.math.Vector2;
import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.board.IBoardGenerator;
import sky7.card.ICard;
import sky7.game.Game;
import sky7.host.IHost;
import sky7.player.IPlayer;
import sky7.player.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Client implements IClient {

    private IBoard board; //TODO double check the code, might contain problems.
    private IHost host;
    private IPlayer player;
    private STATE state;
    private int[] robotDamage = new int[8];
    private String boardName;
    private Game game;
    private boolean localClient; // True if this user is also running Host, false if remotely connected to Host.
    private boolean readyToRender = false, selfPowerDown = false, finishedProcessing = true;
    private ClientNetHandler netHandler;
    private int nPlayers;
    private boolean gameOver = false;


    public Client() {
        //board = new Board(10,8);
        this.player = new Player();
        state = STATE.LOADING;
        localClient = true;
    }

    @Override
    public IBoard gameBoard() {
        return this.board;
    }

    @Override
    public void join(String hostName) {
        localClient = false;
        try {
            netHandler = new ClientNetHandler(this, hostName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void connect(IHost host, int playerNumber) {
        connect(host, playerNumber, ""); //TODO add a default board for the game or something.
        player.setPlayerNumber(playerNumber);
    }

    @Override
    public void connect(IHost host, int playerNumber, String boardName) {
        this.host = host;
        player.setPlayerNumber(playerNumber);
        this.boardName = boardName;
        generateBoard();
    }
    
    @Override
    public void connect(int playerNumber) {
        player.setPlayerNumber(playerNumber);
    }

    @Override
    public void chooseCards(ArrayList<ICard> hand) {
//        player.clearRegistry();
        player.setHand(hand);
        state = STATE.CHOOSING_CARDS;
    }

    /**
     * generate a board
     */
    public void generateBoard() {
        IBoardGenerator generator = new BoardGenerator();
        try {
            board = generator.getBoardFromFile(boardName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        state = STATE.MOVING_ROBOT;
        game = new Game(this, board);
        readyToRender = true;
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }


    /**
     * update board to a different board
     * @param board updated board
     */
    public void updateBoard(IBoard board){
        this.board = board;
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
        player.setCard(chosenCard, positionInRegistry);
    }

    @Override
    public void lockRegistry() {
        //TODO to check, what if the player did not choose 6 cards?
        //player.setRegistry(choosingCards)

        state = STATE.READY;
        if (localClient) {
            host.ready(player.getPlayerNumber(), player.getChosenRegistry(), player.getDiscard(), selfPowerDown);
        } else {
            netHandler.ready(player.getChosenRegistry(), player.getDiscard(), selfPowerDown);
        }
        
        selfPowerDown = false;
    }

    @Override
    public void placeRobot(int playerNr, int xPos, int yPos) {
        board.placeRobot(playerNr, xPos, yPos);
    }

    @Override
    public void activateBoardElements() {
        //board.moveConveyors();
        //board.rotateCogs();

    }

    @Override
    public void activateLasers() {
        //TODO should call board.activateLasers


    }

    @Override
    public void finishedProcessing(IBoard board) {
    	finishedProcessing = true;
        player.clearRegistry();
    	player.clearRegistry();
    }

    @Override
    public void render(HashMap<Integer, ArrayList<ICard>> cards, boolean[] powerDown) {
    	finishedProcessing = false;
    	new Thread(() -> { game.process(cards, powerDown.clone()); }).start();
    }

    public int getID() {
        return player.getPlayerNumber();
    }

    @Override
    public int getNPlayers() {
        return this.nPlayers;
    }
    
    @Override
    public void updateNPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    @Override
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
    
    @Override
    public boolean readyToRender() {
        return this.readyToRender;
    }

    @Override
    public boolean applyDamage(int playerID, int damage) {
        if (playerID == this.player.getPlayerNumber()) player.applyDamage(damage);
        
        robotDamage[playerID] += damage;
        return false;
    }
    @Override
    public void repairDamage(int playerID, int health) {
        if(playerID == this.player.getPlayerNumber()){
            player.repairDamage(health);
        }

        robotDamage[playerID] -= health;
    }

    @Override
    public void powerDown() {
        selfPowerDown ^= true; // flip boolean value
    }

    @Override
    public void powerDownRepair(boolean[] currentPD) {
        if (currentPD[player.getPlayerNumber()]) {
            player.updateDamage(0);
        }
        
        for (int i=0; i<8; i++) {
            if (currentPD[i]) robotDamage[i] = 0;
        }
        
        finishedProcessing(null);
    }
    
    @Override
    public boolean isFinishedProcessing() {
        if (finishedProcessing) {
            finishedProcessing = false;
            return true;
        } else return false;
    }

    @Override
    public void placeRobotAtStart(int playerNr, Vector2 startPosition) {
        board.placeRobotAtStart(playerNr,startPosition);
    }

    @Override
    public boolean loseLifeToken(int playerID) {
        if(playerID == player.getPlayerNumber()){
            if(!gameOver){
                gameOver = player.loseLifeToken();
            }
        }
        return gameOver;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }
}
