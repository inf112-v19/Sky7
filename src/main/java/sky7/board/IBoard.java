package sky7.board;

import com.badlogic.gdx.math.Vector2;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.Flag;
import sky7.board.cellContents.Inactive.StartPosition;
import sky7.board.cellContents.Inactive.Wrench;
import sky7.board.cellContents.robots.RobotTile;

import java.util.List;
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

    /**
     * Place a robot on the board
     * 
     * @param playerNr
     * @param x
     * @param y
     */
    void placeRobot(int playerNr, int x, int y);

    /*/**
     * Attempt to move a robot forward or backward
     * 
     * @param currentPlayer the player/robot number to move
     * @param move should be between -1 and 3
     */
    //void moveRobot(int currentPlayer, int move);

    void moveRobot(Integer id, DIRECTION direction);

    void removeCell(ICell cell, Vector2 pos);

    /**
     * Rotate a robot
     * 
     * @param currentPlayer
     * @param rotate -1 = CCW, 1 = CW, 2 = turn 180
     */
    void rotateRobot(int currentPlayer, int rotate);

    void moveConveyors();

    TreeSet<ICell> getCell(Vector2 a);

    Vector2[] getRobotPos();

    RobotTile[] getRobots();

    void moveRobot(int player, int move);

    boolean containsPosition(Vector2 pos);

    Vector2 getDestination(Vector2 from, DIRECTION direction, int i);

    void hideRobot(int player);

    List<Laser> getLasers();

    List<Vector2> getLaserPos();

    void addCell(ICell cell, Vector2 pos);

    List<Vector2> getStartPositions();

    List<StartPosition> getStartCells();

    List<Flag> getFlags();

    List<Vector2> getFlagPositions();

    List<Wrench> getWrenches();

    List<Vector2> getWrenchPositions();
}
