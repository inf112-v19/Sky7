package sky7.game;

import sky7.board.IBoard;

import java.io.FileNotFoundException;

public interface IClient {

    /**
     * Return the board that the current game is being played on.
     *
     * @return A board
     */
    IBoard gameBoard();

    void generateBoard() throws FileNotFoundException;
}
