package sky7.host;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

import sky7.board.Board;
import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.card.IDeck;
import sky7.card.ProgramCard;
import sky7.card.ProgramDeck;
import sky7.game.Client;
import sky7.game.IClient;

public class Host implements IHost {
    
    IClient[] players;
    int nPlayers = 1, readyPlayers = 0;
    IDeck pDeck;
    IBoard board;
    List<Stack<ICard>> playerRegs;
    TreeSet<PlayerCard> queue;
    

    public Host(IClient cli) {
        players = new Client[8];
        players[0] = cli;
        board = new Board(10, 8);
        playerRegs = new ArrayList<Stack<ICard>>();
        playerRegs.add(new Stack<ICard>());
        queue = new TreeSet<>();
        pDeck = new ProgramDeck();
        
        cli.connect((IHost)this);
        
        run();
    }

    @Override
    public synchronized void run() {
        
        while(true) {
            
            System.out.println("Start of round");
            
            // give 9 cards to each player
            for (int i=0; i<nPlayers; i++) {
                players[i].chooseCards(pDeck.draw(9));
            }
            
            // wait for all players to be ready
            while(readyPlayers < nPlayers) {
                try {
                    System.out.println("host sleeping");
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("host was notified");
                }
            }
            
            // 5 phases
            for (int i=0; i<5; i++) {
                System.out.println("phase " + i);
                //need to compare the leftmost card of each registry and store which order the players will move in (queue)
                // then pop one from each reg, and repeat
                queue.add(new PlayerCard(0));
                
                
                for (int j=1; j<nPlayers; j++) {
                    queue.add(new PlayerCard(j));
                }
                
                for (int j=0; j<nPlayers ; j++) {
                    move(queue.pollLast());
                }
                
                boardElementsMove();
                lasersFire();
            }
            
            readyPlayers = 0;
        }
    }

    private void lasersFire() {
        // TODO Auto-generated method stub
        
    }

    private void boardElementsMove() {
        // TODO Auto-generated method stub
        
    }

    private void move(PlayerCard current) {
//        board.movePlayer(current.playerNr, current.card);
    }

    @Override
    public synchronized void ready(int pN, ArrayList<ICard> registry, ArrayList<ICard> discard) {
        playerRegs.get(pN).addAll(registry);
        pDeck.returnCards(discard);
        readyPlayers++;
        notify();
    }
    
    private class PlayerCard implements Comparable<PlayerCard> {
        
        int playerNr;
        ICard card;
        
        public PlayerCard(int playerNr) {
            this.playerNr = playerNr;
            this.card = playerRegs.get(playerNr).pop();
        }
        
        @Override
        public int compareTo(PlayerCard other) {
            return ((ProgramCard)card).compareTo(((ProgramCard)other.card));
        }
        
    }
}
