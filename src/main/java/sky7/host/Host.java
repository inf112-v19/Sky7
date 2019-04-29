package sky7.host;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.board.cellContents.Inactive.StartPosition;
import sky7.card.ICard;
import sky7.card.IDeck;
import sky7.card.ProgramDeck;
import sky7.Client.IClient;
import sky7.game.Game;

/**
 * A Class that hosts a roboRally game.
 */
public class Host implements IHost {


    // FIELD VARIABLES --------------
    private String boardName = "assets/Boards/mvp1Board.json";
    // TODO MAX_N_PLAYERS should be set based on board.
    private int MAX_N_PLAYERS = 8, nPlayers = 0, readyPlayers = 0, nRemotePlayers = 0, winner = -1;
    private boolean terminated = false, processingFinished = false;
    private HOST_STATE nextState = HOST_STATE.BEGIN;
    private HOST_STATE currentState = HOST_STATE.BEGIN;

    private HashMap<Integer, ArrayList<ICard>> playersRegistries; // player registries
    private Queue<Integer> availableDockPos;
    private boolean[] remotePlayers;
    private int[] lockedRegSlots, robotDamage;
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
        shuffleDockPositions(MAX_N_PLAYERS);
        new Thread(() -> {
            try {
                netHandler = new HostNetHandler((IHost) Host.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            board = bg.getBoardFromFile(boardName);
            game = new Game(this, board);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // PUBLIC METHODS ------------------

    @Override
    public void Begin() {
        //TODO check if ready to begin.
        // Check if clients are ready.
        // Check other conditions if necessary
        netHandler.distributeBoard(boardName);
        placeRobots();
        run();
    }

    @Override
    public synchronized void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard) {
        if (registry.size() < 5)
            throw new IllegalArgumentException("Player " + pN + " attempting to play fewer than 5 cards.");
        playersRegistries.put(pN, registry);
        pDeck.returnCards(discard);
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
        notify();
    }

    @Override
    public void applyDamage(int playerID, int damage) {
        robotDamage[playerID] += damage;
        if (robotDamage[playerID] >= 10) // TODO respawn or lose game
            if (robotDamage[playerID] > 4) lockedRegSlots[playerID] = robotDamage[playerID] - 4;
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
                    board.placeRobot(i, (int) startPositions.get(j).x, (int) startPositions.get(j).y);
                    // add to localClient
                    localClient.placeRobot(i, (int) startPositions.get(j).x, (int) startPositions.get(j).y);
                    // add to remote clients.
                    netHandler.placeRobot(i, (int) startPositions.get(j).x, (int) startPositions.get(j).y);
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
        netHandler.distributeRegistries(playersRegistries);
        localClient.render(playersRegistries);
        nextState = HOST_STATE.BEGIN_PROCESSING;
    }

    /**
     *
     */
    private void runBEGIN_PROCESSING() {
        if (currentState == HOST_STATE.DISTRIBUTE_REGISTRY) {
            processingFinished = false;
            game.process(playersRegistries);
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
     *
     * @param playerID
     */
    private void returnCardsNotLocked(int playerID) {
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
        System.out.println("Handing out " + (9 - robotDamage[0]) + " cards to player " + 0);
        localClient.chooseCards(pDeck.draw(9 - robotDamage[0]));

        for (int i = 1; i < remotePlayers.length; i++) {
            if (remotePlayers[i]) {
                netHandler.dealCards(i, pDeck.draw(9 - robotDamage[i]));
                System.out.println("Handing out " + (9 - robotDamage[i]) + " cards to player " + i);
            }
        }
    }



    /**
     * Create a queue of all possible dock positions in a random order.
     *
     * @param numberOfDockPositions
     */
    private void shuffleDockPositions(int numberOfDockPositions) {
        availableDockPos = new ArrayDeque<>();
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < numberOfDockPositions; i++) {
            temp.add(i);
        }
        Collections.shuffle(temp);
        availableDockPos.addAll(temp);
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

}
