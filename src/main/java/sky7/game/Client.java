package sky7.game;

import sky7.board.Board;
import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.board.IBoardGenerator;

import java.io.FileNotFoundException;

public class Client implements IClient {

    private IBoard board;

    public Client(){
        //board = new Board(10,8);
    }

    @Override
    public IBoard gameBoard() {
        return this.board;
    }

    @Override
    public void generateBoard() throws FileNotFoundException {
        IBoardGenerator generator = new BoardGenerator();
        board = generator.getBoardFromFile("assets/Boards/emptyBoard.json");
    }
}
