package sky7.host;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.card.IDeck;
import sky7.card.ProgramCard;
import sky7.card.ProgramDeck;
import sky7.game.Client;
import sky7.game.IClient;

public class Host implements IHost {

    private static final int FAZES_PER_ROUND = 5;
    private String boardName = "assets/Boards/mvp1Board.json";
    IClient[] players;
    int nPlayers = 1, readyPlayers = 0;
    IDeck pDeck;
    IBoard board;
    HashMap<Integer, ArrayList<ICard>> playerRegs; // player registries
    //    TreeSet<PlayerCard> queue;
    List<Integer> pQueue;
    BoardGenerator bg;
    private boolean terminated = false;


    public Host(IClient cli) { // a host that
        this();
        players = new Client[8];
        players[0] = cli;
        cli.connect((IHost) this, 0, boardName); //TODO do this for each client and give each client a unique ID.
        board.placeRobot(0, 5, 5);
        run();
    }

    public Host() {
        playerRegs = new HashMap<Integer, ArrayList<ICard>>();
//        queue = new TreeSet<>();
        pQueue = new LinkedList<>();
        pDeck = new ProgramDeck();
        bg = new BoardGenerator();
        try {
            board = bg.getBoardFromFile(boardName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Host(Client[] players) {
        this();
        this.players = players;
    }



    public boolean addPlayer(Client player) {

        //add a new player if the board allows it. There is a limit on how many players can play on a board.
        return false;
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

    public int getnPlayers() {
        return nPlayers;
    }

    public int getReadyPlayers() {
        return readyPlayers;
    }

    public String getBoardName() {
        return boardName;
    }

    private synchronized void run() {
        int currentPlayer = 0;

        while (!terminated) {

            System.out.println("Start of round");

            giveOutCards();

            waitForPlayersToBeReady();

            // 5 phases
            for (int faze = 0; faze < FAZES_PER_ROUND; faze++) {
                System.out.println("phase " + faze);

                findPlayerSequence(faze);

                playACardFromEachPlayer(currentPlayer, faze);

                pQueue.clear();

                activateBoardElements();
                activateLasers();

            }

            returnCardsToDeck();

            readyPlayers = 0;
        }
    }

    private void playACardFromEachPlayer(int currentPlayer, int faze) {
        // execute 1 card from each player in order of descending card priority number
        for (int j = 0; j < nPlayers; j++) {
            currentPlayer = pQueue.get(j);
            activateCard(currentPlayer, (ProgramCard) playerRegs.get(currentPlayer).get(faze));

            // wait after each step so that players can see what is going on
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private void returnCardsToDeck() {
        // return registry cards to deck - need to implement locked cards later
        for (int j = 0; j < nPlayers; j++) {
            pDeck.returnCards(playerRegs.remove(j));
        }
    }

    private void waitForPlayersToBeReady() {
        // wait for all players to be ready
        while (readyPlayers < nPlayers) {
            try {
                System.out.println("Host is waiting for players to click ready");
                this.wait();
            } catch (InterruptedException e) {
                System.out.println("host was interrupted");
            }
        }
    }

    private void giveOutCards() {
        // give 9 cards to each player
        for (int i = 0; i < nPlayers; i++) {
            players[i].chooseCards(pDeck.draw(9));
            System.out.println("Cards given to player " + i);
        }
    }

    private void activateCard(int currentPlayer, ProgramCard card) {

        System.out.println("Activating card " + card.GetSpriteRef() + " for player " + currentPlayer);

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

    private void findPlayerSequence(int roundNr) {
        //need to compare the leftmost card of each registry and store which order the players will move in (queue)
        // then pop one from each reg, and repeat
        for (int i = 0; i < nPlayers; i++) {
            ProgramCard thisPlayersCard = (ProgramCard) playerRegs.get(i).get(roundNr);
            for (int j = 0; j < pQueue.size(); j++) {
                ProgramCard thatPlayersCard = (ProgramCard) playerRegs.get(pQueue.get(j)).get(roundNr);
                if (thisPlayersCard.priorityN() > thatPlayersCard.priorityN())
                    pQueue.add(j, i);
                    continue;
                }
            }

            pQueue.add(i);
        }
    }

    private void activateBoardElements() {
        board.moveConveyors();
        board.rotateCogs();
        
        for (int i=0; i<nPlayers; i++) {
            players[i].activateBoardElements();
        }
    }

    private void activateLasers() {

    }

    @Override
    public synchronized void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard) {
        if (registry.size() < 5) throw new IllegalArgumentException("Player " + pN + " attempting to play fewer than 5 cards.");
        playerRegs.put(pN, registry);
        pDeck.returnCards(discard);
        readyPlayers++;
        notify();
    }

    public synchronized void terminate(){
        terminated = true;
        notify();
    }
}
