package sky7.host;

import com.badlogic.gdx.math.Vector2;
import sky7.Client.IClient;
import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.board.cellContents.Inactive.StartPosition;
import sky7.card.ICard;
import sky7.card.IDeck;
import sky7.card.ProgramDeck;
import sky7.game.Game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A Class that hosts a roboRally game.
 */
public class Host implements IHost {

    @Override
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    // FIELD VARIABLES --------------
    private String boardName = "assets/Boards/DizzyDash.json";

    // TODO MAX_N_PLAYERS should be set based on board.
    private int MAX_N_PLAYERS = 8, nPlayers = 0, readyPlayers = 0, nRemotePlayers = 0, winner = -1;
    private int nFlagsOnBoard = 4; // TODO should be set based on loaded board
    private boolean terminated = false, processingFinished = false;
    private HOST_STATE nextState = HOST_STATE.BEGIN;
    private HOST_STATE currentState = HOST_STATE.BEGIN;

    private HashMap<Integer, ArrayList<ICard>> playersRegistries; // player registries
    private boolean[] remotePlayers;
    private int[] lockedRegSlots, robotDamage, lifeTokens, visitedFlags;
    private boolean[] powerDown, gameOver;
    private HostNetHandler netHandler;
    private BoardGenerator bg;
    private IClient localClient;
    private IBoard board;
    private IDeck pDeck;
    private Game game;

    // TODO host must know where each player can respawn.(?) Or should that be handled locally in game engine?

    // CONSTRUCTORS -------------

    /**
     * @param cli a Client i.e. player
     */
    public Host(IClient cli) {
        this();
        localClient = cli;
        localClient.connect(this, nPlayers++, boardName);


    }

    public Host() {
        initializeFieldVariables();

        try {
            netHandler = new HostNetHandler((IHost) Host.this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            board = bg.getBoardFromFile(boardName);
            game = new Game(this, board);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        nFlagsOnBoard = board.getFlags().size();
    }


    // PUBLIC METHODS ------------------

    @Override
    public void Begin() {
        // TODO Check other conditions if necessary
        netHandler.distributeBoard(boardName);
        placeRobots();
        run();
    }

    @Override
    public synchronized void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard, boolean powerDown) {
        if (registry.size() < 5)
            throw new IllegalArgumentException("Player " + pN + " attempting to play fewer than 5 cards.");
        playersRegistries.put(pN, registry);
        pDeck.returnCards(discard);
        if (powerDown) this.powerDown[pN] = true;
        readyPlayers++;
        notify();
    }

    @Override
    public synchronized void terminate() {
        terminated = true;
        currentState = HOST_STATE.TERMINATED;
        nextState = HOST_STATE.BEGIN;
        notify();
    }

    @Override
    public synchronized void setWinner(int winner) {
        processingFinished = true;
        this.winner = winner;
        notifyAll();
    }

    @Override
    public boolean applyDamage(int playerID, int damage) {
        robotDamage[playerID] += damage;
        if (robotDamage[playerID] > 4) lockedRegSlots[playerID] = robotDamage[playerID] - 4;
        if (robotDamage[playerID] >= 10) return true;
        return false;
    }

    @Override
    public void repairDamage(int playerID, int damage) {
        if (robotDamage[playerID] > 0) {
            robotDamage[playerID] -= damage;
        }
        //TODO andre årsaker som gjør at player ikke skal få mer health?
    }


    // PRIVATE METHODS -----------------

    /**
     *
     */
    private void initializeFieldVariables() {
        pDeck = new ProgramDeck();
        bg = new BoardGenerator();
        playersRegistries = new HashMap<>();
        remotePlayers = new boolean[8];
        lockedRegSlots = new int[8];
        robotDamage = new int[8];
        lifeTokens = new int[8];
        visitedFlags = new int[8];
        powerDown = new boolean[8];
        gameOver = new boolean[8];
    }

    /**
     *
     */
    private void placeRobots() {
        List<StartPosition> startCells = board.getStartCells();
        List<Vector2> startPositions = board.getStartPositions();

        for (int i = 0; i < nPlayers; i++) {
            for (int j = 0; j < startCells.size(); j++) {
                if (startCells.get(j).getNumber() == i + 1) {
                    // add to hosts board.
                    board.placeRobotAtStart(i, startPositions.get(j));
                    //board.placeRobot(i, (int) startPositions.get(j).x, (int) startPositions.get(j).y);

                    // add to localClient
                    //localClient.placeRobot(i, (int) startPositions.get(j).x, (int) startPositions.get(j).y);
                    localClient.placeRobotAtStart(i, startPositions.get(j));

                    // add to remote clients.
                    netHandler.placeRobotAtStart(i, startPositions.get(j));
                    //netHandler.placeRobot(i, (int) startPositions.get(j).x, (int) startPositions.get(j).y);
                    break;
                }
            }
        }

    }

    /**
     * run the game state by state.
     */
    private synchronized void run() {
        while (!terminated) {
            System.out.println(nextState);
            switch (nextState) {
                case BEGIN:
                    runBEGIN();
                    break;
                case DEAL_CARDS:
                    runDEAL_CARDS();
                    break;
                case WAIT_FOR_PLAYERS:
                    runWAIT_FOR_PLAYERS();
                    break;
                case DISTRIBUTE_REGISTRY:
                    runDISTRIBUTE_REGISTRY();
                    break;
                case BEGIN_PROCESSING:
                    runBEGIN_PROCESSING();
                    break;
                case WAITING_FOR_PROCESSING:
                    runWAIT_FOR_PROCESSING();
                    break;
                case FINISHED:
                    runFINISHED();
                    break;
                default:
                    throw new IllegalStateException("Host could not find next state.");
            }
        }
    }


    /**
     *
     */
    private void runBEGIN() {
        currentState = HOST_STATE.BEGIN;
        nextState = HOST_STATE.DEAL_CARDS;
    }

    /**
     *
     */
    private void runDEAL_CARDS() {
        currentState = HOST_STATE.DEAL_CARDS;
        System.out.println("Start of round");
        readyPlayers = 0;
        giveOutCards();
        nextState = HOST_STATE.WAIT_FOR_PLAYERS;
    }

    /**
     *
     */
    private void runWAIT_FOR_PLAYERS() {
        currentState = HOST_STATE.WAIT_FOR_PLAYERS;
        if (readyPlayers < nPlayers) {
            waitForPlayersToBeReady();
        } else nextState = HOST_STATE.DISTRIBUTE_REGISTRY;
    }

    /**
     *
     */
    private void runDISTRIBUTE_REGISTRY() {
        currentState = HOST_STATE.DISTRIBUTE_REGISTRY;
        netHandler.distributeRegistries(playersRegistries, powerDown);
        localClient.render(playersRegistries, powerDown);
        nextState = HOST_STATE.BEGIN_PROCESSING;
    }

    /**
     *
     */
    private void runBEGIN_PROCESSING() {
        if (currentState == HOST_STATE.DISTRIBUTE_REGISTRY) {
            processingFinished = false;
            game.process(playersRegistries, powerDown);
        }
        currentState = HOST_STATE.BEGIN_PROCESSING;
        nextState = processingFinished ? (winner != -1 ? HOST_STATE.FINISHED : HOST_STATE.DEAL_CARDS) : HOST_STATE.BEGIN_PROCESSING;
        returnCardsToDeck();
    }

    /**
     *
     */
    private void runWAIT_FOR_PROCESSING() {
        try {
            System.out.println("Host is waiting for gameProcessing to finish");
            this.wait();
        } catch (InterruptedException e) {
            System.out.println("host was interrupted");
        }
    }

    /**
     *
     */
    private void runFINISHED() {
        // TODO if there is something to do when game is finished
        terminated = true;
    }


    /**
     * returns the cards both chosen and not chosen by a player.
     */
    private void returnCardsToDeck() {
        // return registry cards to deck - need to implement locked cards later
        if (!playersRegistries.isEmpty())
            returnCardsNotLocked(0);
        for (int i = 1; i < 8; i++) {
            if (remotePlayers[i]) returnCardsNotLocked(i);
        }
    }

    /**
     * @param playerID
     */
    private void returnCardsNotLocked(int playerID) {
        if (!playersRegistries.containsKey(playerID)) return;
        if (lockedRegSlots[playerID] == 0) pDeck.returnCards(playersRegistries.remove(playerID));
        else {
            ArrayList<ICard> reg = playersRegistries.remove(playerID);
            for (int i = 4; i >= lockedRegSlots[playerID]; i--) {
                reg.remove(i);
            }
            pDeck.returnCards(reg);
        }
    }

    /**
     * waits for players to be ready.
     */
    private synchronized void waitForPlayersToBeReady() {
        // wait for all players to be ready
        try {
            System.out.println("Host is waiting for players to click ready");
            this.wait();
        } catch (InterruptedException e) {
            System.out.println("host was interrupted");
        }

    }

    /**
     * gives out 9 card to each player, at the start of a round.
     */
    private void giveOutCards() {
        // give 9 cards to each player
        // TODO: handle situation where host should hand out less than 9 cards to damaged robots
        if (!powerDown[0]) {
            System.out.println("Handing out " + (9 - robotDamage[0]) + " cards to player " + 0);
            localClient.chooseCards(pDeck.draw(9 - robotDamage[0]));
        } else {
            localClient.chooseCards(new ArrayList<ICard>());
            readyPlayers++;
        }

        for (int i = 1; i < remotePlayers.length; i++) {
            if (remotePlayers[i]) {
                if (!gameOver[i]) {
                    if (!powerDown[i]) {
                        netHandler.dealCards(i, pDeck.draw(Math.max(0, 9 - robotDamage[i])));
                        System.out.println("Handing out " + (Math.max(0, 9 - robotDamage[i])) + " cards to player " + i);
                    } else {
                        netHandler.dealCards(i, new ArrayList<ICard>());
                        readyPlayers++;
                    }
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            if (powerDown[i]) powerDown[i] = false;
        }
    }


    // NET ------------------------

    @Override
    public int remotePlayerConnected() {
        for (int i = 1; i < MAX_N_PLAYERS; i++) {
            if (!remotePlayers[i]) {
                remotePlayers[i] = true;
                nRemotePlayers++;
                nPlayers++;
                return i;
            }
        }
        throw new IllegalStateException("Could not find legal playerID for newly connected player");
    }

    @Override
    public void remotePlayerDisconnected(int playerID) {
        remotePlayers[playerID] = false;
        nRemotePlayers--;
        nPlayers--;
    }

    // GETTERS ---------------------

    @Override
    public HOST_STATE getCurrentState() {
        return currentState;
    }

    @Override
    public IDeck getpDeck() {
        return pDeck;
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public int getnPlayers() {
        return nRemotePlayers;
    }

    @Override
    public int getReadyPlayers() {
        return readyPlayers;
    }

    @Override
    public String getBoardName() {
        return boardName;
    }

    @Override
    public synchronized void finishedProcessing(IBoard board) {
        processingFinished = true;
        this.board = board;
        notify();
    }

    @Override
    public void robotVisitedFlag(int playerID, int flagNumber) {
        if (visitedFlags[playerID] == flagNumber - 1) visitedFlags[playerID]++;

        if (visitedFlags[playerID] == nFlagsOnBoard) {
            System.out.println("Player " + playerID + " has won the game!");
            // TODO victory stuff(?)
        }
    }

    @Override
    public void powerDownRepair(boolean[] currentPD) {
        for (int i = 0; i < MAX_N_PLAYERS; i++) {
            if (currentPD[i]) {
                robotDamage[i] = 0;
                lockedRegSlots[i] = 0;
            }
        }
    }

    @Override
    public boolean loseLifeToken(int playerID) {
        // TODO what happens at lose of life-token
        // Check if game over
        // if so, do not ask for registry
        // send game update until game is won or everybody loses.
        // host must continue to host until match is won or everybody loses.

        if(!gameOver[playerID]){
            gameOver[playerID] = --lifeTokens[playerID]<=0;
            if(gameOver[playerID]) --nPlayers;
        }
        if(nPlayers == 0){
            // TODO what happens if nobody wins.
        }
        return gameOver[playerID];
    }

}
