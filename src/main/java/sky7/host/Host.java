package sky7.host;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import sky7.board.Board;
import sky7.board.IBoard;
import sky7.card.ICard;
import sky7.card.ProgramCard;
import sky7.card.IDeck;
import sky7.card.ProgramDeck;
import sky7.game.Client;
import sky7.game.IClient;

public class Host implements IHost {
    
    IClient[] players;
    int nPlayers = 1, readyPlayers = 0;
    IDeck pDeck;
    IBoard board;
    List<Stack<ICard>> playerRegs;
    

    public Host(IClient cli) {
        players = new Client[8];
        players[0] = cli;
        board = new Board(10, 8);
        playerRegs = new ArrayList<Stack<ICard>>();
        playerRegs.add(new Stack<ICard>());
        
        if (cli == null) throw new IllegalStateException("client is null");
        
        cli.connect((IHost)this);
        
        pDeck = new ProgramDeck();
        
        run();
    }

    @Override
    public synchronized void run() {
        
        while(true) {
            
            System.out.println("Start of round");
            
            for (int i=0; i<nPlayers; i++) {
                players[i].getCards(pDeck.draw(9));
            }
            
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
                
                //need to compare the leftmost card of each registry and store which order the players will move in (queue)
                // then pop one from each reg, and repeat
                
                int fastestPlayer = 0;
                for (int j=1; j<nPlayers; j++) {
                    if (((ProgramCard)playerRegs.get(j).peek()).compareTo(((ProgramCard)playerRegs.get(fastestPlayer).peek())) > 1) {
                        
                    }
                }
            }
            
        }
    }

    @Override
    public void ready(int pN, Stack<ICard> registry, Stack<ICard> discard) {
        playerRegs.get(pN).addAll(registry);
        pDeck.returnCards(discard);
        readyPlayers++;
    }
}
