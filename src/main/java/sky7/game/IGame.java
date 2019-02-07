package sky7.game;

import sky7.board.IBoard;

public interface IGame {

    /**
     * Return the board that the current game is being played on.
     *
     * @return A board
     */
    IBoard gameBoard();
}
