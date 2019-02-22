package sky7.game;

import sky7.board.Board;
import sky7.board.IBoard;

public class Client implements IClient {

    private IBoard board;
    
    public Client() {
        board = new Board(10,8);
    }

    @Override
    public IBoard gameBoard() {
        return this.board;
    }
}
