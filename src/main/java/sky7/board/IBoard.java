package sky7.board;

import com.badlogic.gdx.math.Vector2;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.Active.Pusher;
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
     * @param playerNr integer representing the number of the player
     * @param x in x pos
     * @param y in y pos
     */
    void placeRobot(int playerNr, int x, int y);

    /**
     * move robot with playerNumber in this direction
     *
     * @param playerNumber integer representing robots number
     * @param direction in what direction robot should be moved to
     */
    void moveRobot(Integer playerNumber, DIRECTION direction);

    /**
     * remove this cell in this pos from this board
     *
     * @param cell cell to be removed
     * @param pos position of the cell to be removed
     */
    void removeCell(ICell cell, Vector2 pos);

    /**
     * Rotate a robot
     * 
     * @param currentPlayer
     * @param rotate -1 = CCW, 1 = CW, 2 = turn 180
     */
    void rotateRobot(int currentPlayer, int rotate);

    void moveConveyors();

    /**
     * return a list of the position of all pushers in this board
     *
     * @return pushers positions.
     */
    List<Vector2> getPusherPos();

    /**
     * return a list of all the pushers on this board
     *
     * @return all the pushers
     */
    List<Pusher> getPushers();

    /**
     * return the cell in that position
     *
     * @param positionOfCell position to find the cell
     * @return the cell in that position
     */
    TreeSet<ICell> getCell(Vector2 positionOfCell);

    /**
     * return a list of the position of all robots in this board
     *
     * @return robots positions.
     */
    Vector2[] getRobotPos();

    /**
     * return a list of all robots on this board
     *
     * @return all robots
     */
    RobotTile[] getRobots();

    /**
     * move robot //TODO fjerne denne da den bare blir brukt i testene
     *
     * @param player the robot to move
     * @param move move this many steps
     */
    void moveRobot(int player, int move);

    /**
     * check if board contains this position
     *
     * @param pos the position to check.
     * @return true if the position is within board
     */
    boolean containsPosition(Vector2 pos);

    /**
     * return the destination after going this amount of steps in that direction from that position
     *
     * @param from start position
     * @param direction in what direction
     * @param steps how far
     * @return the new position
     */
    Vector2 getDestination(Vector2 from, DIRECTION direction, int steps);

    /**
     * dont show the robot in this board
     *
     * @param player robot to hide
     */
    void hideRobot(int player);

    /**
     *
     * @return a list of all lasers in this board
     */
    List<Laser> getLasers();

    /**
     *
     * @return a list of all position of all lasers in this board
     */
    List<Vector2> getLaserPos();

    /**
     * add a this cell in pos position
     *
     * @param cell cell to add
     * @param pos in what positon
     */
    void addCell(ICell cell, Vector2 pos);

    /**
     *
     * @return list of  where all start points are
     */
    List<Vector2> getStartPositions();

    /**
     *
     * @return list of all start position in this board
     */
    List<StartPosition> getStartCells();

    /**
     *
     * @return a list of all the flags
     */
    List<Flag> getFlags();

    /**
     *
     * @return a list of all position to all flags
     */
    List<Vector2> getFlagPositions();

    /**
     *
     * @return a list with all wrenches
     */
    List<Wrench> getWrenches();

    /**
     *
     * @return a list of position to all the wrenches
     */
    List<Vector2> getWrenchPositions();
}
