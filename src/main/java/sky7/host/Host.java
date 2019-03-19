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

    private String boardName = "assets/Boards/mvp1Board.json";
    IClient[] players;
    int nPlayers = 1, readyPlayers = 0;
    IDeck pDeck;
    IBoard board;
    HashMap<Integer, ArrayList<ICard>> playerRegs;
//    TreeSet<PlayerCard> queue;
    List<Integer> pQueue;
    BoardGenerator bg;


    public Host(IClient cli) {
        players = new Client[8];
        players[0] = cli;
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
        
        cli.connect((IHost)this, 0, boardName); //TODO do this for each client and give each client a unique ID.
        
        board.placeRobot(0, 5, 5);
        
        run();
    }
    
    public Host() {
        
    }

    private synchronized void run() {
        int currentPlayer = 0;
        
        while(true) {
            
            System.out.println("Start of round");
            
            // give 9 cards to each player
            for (int i=0; i<nPlayers; i++) {
                players[i].chooseCards(pDeck.draw(9));
                System.out.println("Cards given to player " + i);
            }
            
            // wait for all players to be ready
            while(readyPlayers < nPlayers) {
                try {
                    System.out.println("Host is waiting for players to click ready");
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("host was interrupted");
                }
            }
            
            // 5 phases
            for (int i=0; i<5; i++) {
                System.out.println("phase " + i);
                //need to compare the leftmost card of each registry and store which order the players will move in (queue)
                // then pop one from each reg, and repeat
                findPlayerSequence(i);
                
                // execute 1 card from each player in order of descending card priority number
                for (int j=0; j<nPlayers ; j++) {
                    currentPlayer = pQueue.get(j);
                    activateCard(currentPlayer, (ProgramCard)playerRegs.get(currentPlayer).get(i));
                    
                    // wait after each step so that players can see what is going on
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                }
                pQueue.clear();
            }
            
            activateBoardElements();
            activateLasers();
            
            // return registry cards to deck - need to implement locked cards later
            for (int j=nPlayers-1; j>=0; j--) {
                pDeck.returnCards(playerRegs.remove(j));
            }
            
            readyPlayers = 0;
        }
    }

    private void activateCard(int currentPlayer, ProgramCard card) {
        
        System.out.println("Activating card " + card.GetSpriteRef() + " for player " + currentPlayer);
        
        // call all clients to perform the same action on their board
        for (int i=0; i<nPlayers; i++) {
            players[i].activateCard(currentPlayer, card);
        }
        
        if (card.moveType()) {
            board.moveRobot(currentPlayer, card.move());
        } else {
            board.rotateRobot(currentPlayer, card.rotate());
        }
    }

    private void findPlayerSequence(int roundNr) {
        for (int i=0; i<nPlayers; i++) {
        ProgramCard thisPlayersCard = (ProgramCard)playerRegs.get(i).get(roundNr);
            for (int j=0; j<pQueue.size(); j++) {
                ProgramCard thatPlayersCard = (ProgramCard)playerRegs.get(pQueue.get(j)).get(roundNr);
                if (thisPlayersCard.priorityN() > thatPlayersCard.priorityN())
                    pQueue.add(j, i);
            }
            
            pQueue.add(i);
        }
    }

    private void activateBoardElements() {
        board.moveConveyors();
        board.rotateCogs();
        
        for (int i=0; i<nPlayers; i++) {
            players[i].activateBoardElements();
            // add method to activate conveyors
        }
    }
    
    private void activateLasers() {
        
    }

    @Override
    public synchronized void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard) {
        if (registry.size() < 5) throw new IllegalArgumentException("registry does not contain 5 cards");
        playerRegs.put(pN, registry);
        pDeck.returnCards(discard);
        readyPlayers++;
        notify();
    }
}
