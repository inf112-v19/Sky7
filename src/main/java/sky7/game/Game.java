package sky7.game;

import sky7.board.Board;
import sky7.board.IBoard;

public class Game implements IGame {

    private IBoard board;
    
    public Game() {
        board = new Board();
        
        
    }

}
