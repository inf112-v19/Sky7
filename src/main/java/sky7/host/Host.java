package sky7.host;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.card.IDeck;
import sky7.card.ProgramDeck;
import sky7.Client.Client;
import sky7.Client.IClient;
import sky7.game.Game;

/**
 * A Class that
 */
public class Host implements IHost {


    // FIELD VARIABLES --------------
    private String boardName = "assets/Boards/mvp1Board.json";
    private IClient[] players;
    private int nPlayers = 0, readyPlayers = 0, nRemotePlayers = 0;
    private IDeck pDeck;
    private IBoard board;
    private HashMap<Integer, ArrayList<ICard>> playerRegs; // player registries
    private boolean[] remotePlayers;
    private int[] dockPos; // number in pos x is player x's dock position
    private Queue<Integer> availableDockPos;
    
    private BoardGenerator bg;
    private boolean terminated = false;
    private HOST_STATE nextState = HOST_STATE.BEGIN;
    private HOST_STATE currentState = HOST_STATE.BEGIN;
    private int roundNr = 0;
    private Game game;
    private boolean processingFinished = false;
    private int winner = -1;
    private HostNetHandler netHandler;
    private int MAX_N_PLAYERS = 8; // TODO: set according to board size

    // host must know where each player can respawn.

    // CONSTRUCTORS -------------

    /**
     * @param cli a Client i.e. player
     */
    public Host(IClient cli) {
        this();
        players[0] = cli;
        new Thread() {
            public void run() {
                try {
                    netHandler = new HostNetHandler((IHost)Host.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        nPlayers++;
        cli.connect(this, 0, boardName);
        board.placeRobot(0, 5, 5);
        cli.placeRobot(0, 5, 5);
    }

    public Host() {

        players = new Client[MAX_N_PLAYERS];
        playerRegs = new HashMap<>();
        remotePlayers = new boolean[8];
        dockPos = new int[8];
        shuffleDockPositions(MAX_N_PLAYERS); // TODO argument should be same as number of dock positions on chosen board
        //pQueue = new LinkedList<>();
        pDeck = new ProgramDeck();
        bg = new BoardGenerator();
//        queue = new TreeSet<>();
        try {
            board = bg.getBoardFromFile(boardName);
            game = new Game(this, board);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * @param players an array of players
     */
    public Host(Client[] players) {
        this();
        this.players = players;
    }


    // USEFUL METHODS -----------------

    /**
     * Begin the game.
     */
    public void Begin() {
        //TODO check if ready to begin.
        // Check if clients are ready.
        // Check other conditions if necessary
        netHandler.distributeBoard(boardName);
        run3();
    }

    @Override
    public boolean addPlayer(Client player) {
        players[nPlayers++] = player;
        player.connect(this, 0, boardName);
        //TODO add a new player if the board allows it. There is a limit on how many players can play on a board.
        return false;
    }


    private synchronized void run3() {
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

    private void runBEGIN() {
        currentState = HOST_STATE.BEGIN;
        // TODO can add beginning menu here
        nextState = HOST_STATE.DEAL_CARDS;
    }

    private void runDEAL_CARDS() {
        currentState = HOST_STATE.DEAL_CARDS;
        System.out.println("Start of round");
        returnCardsToDeck();
        readyPlayers = 0;
        giveOutCards();
        nextState = HOST_STATE.WAIT_FOR_PLAYERS;
    }

    private void runWAIT_FOR_PLAYERS() {
        currentState = HOST_STATE.WAIT_FOR_PLAYERS;
        if (readyPlayers < nPlayers) {
            waitForPlayersToBeReady();
        } else nextState = HOST_STATE.DISTRIBUTE_REGISTRY;
    }


    private void runDISTRIBUTE_REGISTRY() {
        currentState = HOST_STATE.DISTRIBUTE_REGISTRY;
        for (int i = 0; i < nPlayers; i++) {
            players[i].render(playerRegs);
        }
        netHandler.distributeRegistries(playerRegs);
        nextState = HOST_STATE.BEGIN_PROCESSING;
    }

    private void runBEGIN_PROCESSING() {
        if (currentState == HOST_STATE.DISTRIBUTE_REGISTRY) {
            processingFinished = false;
            game.process(playerRegs);
        }
        currentState = HOST_STATE.BEGIN_PROCESSING;
        nextState = processingFinished ? (winner != -1 ? HOST_STATE.FINISHED : HOST_STATE.DEAL_CARDS) : HOST_STATE.BEGIN_PROCESSING;
    }

    private void runWAIT_FOR_PROCESSING() {
        try {
            System.out.println("Host is waiting for gameProcessing to finish");
            this.wait();
        } catch (InterruptedException e) {
            System.out.println("host was interrupted");
        }
    }

    private void runFINISHED() {
        // TODO if there is something to do when game is finished
        terminated = true;
    }


    /**
     * returns the cards both chosen and not chosen by a player.
     */
    private void returnCardsToDeck() {
        // return registry cards to deck - need to implement locked cards later
        if (!playerRegs.isEmpty())
            for (int j = 0; j < nPlayers; j++) {
                pDeck.returnCards(playerRegs.remove(j));
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
        players[0].chooseCards(pDeck.draw(9));
        System.out.println("Cards given to player " + 0);
        
        for (int i=1; i<MAX_N_PLAYERS ; i++) {
            if (remotePlayers[i]) {
                netHandler.dealCards(i, pDeck.draw(9));
                System.out.println("Cards given to player " + i);
            }
        }
    }

    @Override
    public synchronized void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard) {
        if (registry.size() < 5)
            throw new IllegalArgumentException("Player " + pN + " attempting to play fewer than 5 cards.");
        playerRegs.put(pN, registry);
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
    
    /**
     * Create a queue of all possible dock positions in a random order.
     * 
     * @param numberOfDockPositions
     */
    private void shuffleDockPositions(int numberOfDockPositions) {
        availableDockPos = new ArrayDeque<>();
        List<Integer> temp = new ArrayList<>();
        for (int i=0; i<numberOfDockPositions ; i++) {
            temp.add(i);
        }
        Collections.shuffle(temp);
        availableDockPos.addAll(temp);
    }

    // NET ------------------------
    
    @Override
    public int remotePlayerConnected() {
        for (int i=1; i<MAX_N_PLAYERS  ; i++) {
            if (!remotePlayers[i]) {
                remotePlayers[i] = true;
                nRemotePlayers++;
                return i;
            }
        }
        throw new IllegalStateException("Could not find legal playerID for newly connected player");
    }
    
    @Override
    public void remotePlayerDisconnected(int playerID) {
        remotePlayers[playerID] = false;
        nRemotePlayers--;
    }

    // GETTERS ---------------------

    public HOST_STATE getCurrentState() {
        return currentState;
    }

    @Override
    public int getRoundNr() {
        return roundNr;
    }

    public IDeck getpDeck() {
        return pDeck;
    }

    public IBoard getBoard() {
        return board;
    }

    public IClient[] getPlayers() {
        return players;
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


    // TODO TO DELETE
    /*//TreeSet<PlayerCard> queue;
    private List<Integer> pQueue;
    private int phaseNr = 0;
    private static final int FAZES_PER_ROUND = 5;
    // an older certainly stable run
    private synchronized void run2() {
        int currentPlayer = 0;

        while (!terminated) {

            System.out.println("Start of round");

            giveOutCards();

            waitForPlayersToBeReady();

            // 5 phases
            for (int faze = 0; faze < FAZES_PER_ROUND; faze++) {
                System.out.println("phase " + faze);

                //findPlayerSequence(faze);

                //runPhase(faze);

                pQueue.clear();

                activateBoardElements();
                activateLasers();

            }

            returnCardsToDeck();

            readyPlayers = 0;
        }
    }

    /**
     * Runs the game until someone has won, or the game is terminated.

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
                case PLAY_NEXT_ROUND:
                    runPLAY_NEXT_ROUND();
                    break;
                case PLAY_NEXT_PHASE:
                    runPLAY_NEXT_PHASE();
                    break;
            }
        }
    }

    private void runPLAY_NEXT_PHASE() {
        currentState = HOST_STATE.PLAY_NEXT_PHASE;
        if (phaseNr < 5) {
            simulateFaze();
            nextState = HOST_STATE.PLAY_NEXT_PHASE;
        } else {
            phaseNr = 0;
            nextState = HOST_STATE.PLAY_NEXT_ROUND;
        }
    }

    private void runPLAY_NEXT_ROUND() {
        if (currentState == HOST_STATE.WAIT_FOR_PLAYERS) {
            nextState = HOST_STATE.PLAY_NEXT_PHASE;
        } else if (currentState == HOST_STATE.PLAY_NEXT_PHASE) {
            nextState = HOST_STATE.DEAL_CARDS;
        }
        roundNr++;
        currentState = HOST_STATE.PLAY_NEXT_ROUND;
    }
       /**
     * Simulates a phase in the game.

    private void simulateFaze() {
        System.out.println("phase " + phaseNr);
        findPlayerSequence();
        runPhase();
        pQueue.clear();
        activateBoardElements();
        activateLasers();
        phaseNr++;
    }


    /**
     * For a single phase, queues the players in sequence by the priority of the cards they chose.

    private void findPlayerSequence() {
        //need to compare the leftmost card of each registry and store which order the players will move in (queue)
        // then pop one from each reg, and repeat
        for (int i = 0; i < nPlayers; i++) {
            //TODO
            ProgramCard thisPlayersCard = (ProgramCard) playerRegs.get(i).get(phaseNr);
            for (int j = 0; j < pQueue.size(); j++) {
                ProgramCard thatPlayersCard = (ProgramCard) playerRegs.get(pQueue.get(j)).get(phaseNr);
                if (thisPlayersCard.priorityN() > thatPlayersCard.priorityN())
                    pQueue.add(j, i);
            }
            pQueue.add(i);
        }
    }

       /**
     * Play a single card, meaning simulate moves, from each player.

    private void runPhase() {
        // execute 1 card from each player in order of descending card priority number
        int currentPlayer;
        for (int j = 0; j < nPlayers; j++) {
            currentPlayer = pQueue.get(j);
            activateCard(currentPlayer, (ProgramCard) playerRegs.get(currentPlayer).get(phaseNr));

            // wait after each step so that players can see what is going on
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

        /**
     * @param currentPlayer
     * @param card

    private void activateCard(int currentPlayer, ProgramCard card) {

        System.out.println("Activating card " + card.GetSpriteRef() + " for player " + currentPlayer);

        // TODO This is not implemented in the clients as of yet.
        // call all clients to perform the same action on their board
        for (int i = 0; i < nPlayers; i++) {
            players[i].activateCard(currentPlayer, card);
        }

        if (card.moveType()) {
            board.moveRobot(currentPlayer, card.move());
        } else {
            board.rotateRobot(currentPlayer, card.rotate());
        }
    }
        /**
     * Activates the lasers on the board, not the robot-lasers.

    private void activateLasers() {
        // TODO
    }

       /**
     * Activates elements that belong to the board, like laser, cogwheels.

    private void activateBoardElements() {
        board.moveConveyors();
        board.rotateCogs();

        for (int i = 0; i < nPlayers; i++) {
            players[i].activateBoardElements();
        }

        // wait after each step so that players can see what is going on
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */
}
