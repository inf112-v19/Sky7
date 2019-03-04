package sky7.board;

import com.badlogic.gdx.graphics.Texture;

import java.util.TreeSet;

public interface IBoard {


    /**
     * Returns a the TreeSet with cells to be displayed at a given position of a tile on the board.
     *
     * @param x horizontal position of a tile
     * @param y vertical position of a tile
     * @return the name or value of an image to display of a cell at a given tile.
     */
    TreeSet<ICell> getTileTexture(int x, int y);

    /**
     * The width of the board by the number of tiles
     *
     * @return the width of the board by the number of tiles on the board.
     */
    int getWidth();

    /**
     * The height of the board by the number of tiles
     *
     * @return  The height of the board by the number of tiles on the board.
     */
    int getHeight();

    void placeRobot(int playerNr, int x, int y);

    void moveRobot(int currentPlayer, int move);

}
