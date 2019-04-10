package sky7.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector2;

import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.Active.IConveyorBelt;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.Inactive.Hole;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.robots.RobotTile;

public class Board implements IBoard {
    private TreeSet<ICell>[][] grid;
    private int width, height, nPlayers, maxMove;
    private Vector2[] robotPos;
    private RobotTile[] robots;
    private List<CogWheel> cogs;
    private List<Vector2> cogPos;
    private List<IConveyorBelt> convs;
    private List<Vector2> convPos;
    private List<Laser> lasers;
    private List<Vector2> laserPos;
    private ArrayList<Vector2> holePos;
    private ArrayList<Hole> holes;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new TreeSet[width][height];

        // fill grid with floor tiles
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new TreeSet<ICell>();
                grid[i][j].add(new FloorTile()); //= new FloorTile();
            }
        }

        //add 1 robot
        grid[5][4].add(new RobotTile(0));
        nPlayers = 1;

    }

    @Override
    public Vector2[] getRobotPos() {
        return robotPos;
    }

    public Board(TreeSet<ICell>[][] grid, int height, int width) {
        this.grid = grid;
        this.width = width;
        this.height = height;
        this.nPlayers = 0;
        this.robotPos = new Vector2[8];
        this.robots = new RobotTile[8];
        this.cogs = new ArrayList<>();
        this.cogPos = new ArrayList<>();
        this.convs = new ArrayList<>();
        this.convPos = new ArrayList<>();
        this.lasers = new ArrayList<>();
        this.laserPos = new ArrayList<>();
        this.holes = new ArrayList<>();
        this.holePos = new ArrayList<>();

        // find and store locations of cogwheels, conveyor belts
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (ICell item : grid[i][j]) {
                    if (item instanceof CogWheel) {
                        cogPos.add(new Vector2(i, j));
                        cogs.add((CogWheel) item);
                    }
                    if (item instanceof IConveyorBelt) {
                        convPos.add(new Vector2(i, j));
                        convs.add((IConveyorBelt) item);
                    }
                    if (item instanceof Laser) {
                        laserPos.add(new Vector2(i, j));
                        lasers.add((Laser) item);
                    }
                    if (item instanceof Hole) {
                        holePos.add(new Vector2(i, j));
                        holes.add((Hole) item);
                    }
                }
            }
        }

    }

    @Override
    public TreeSet<ICell> getTileTexture(int x, int y) {
        return grid[x][y];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(grid);
    }

    @Override
    public void placeRobot(int playerNr, int x, int y) {
        System.out.println("Placing robot " + playerNr + " at " + x + ", " + y);
        robotPos[playerNr] = new Vector2(x, y);
        robots[playerNr] = new RobotTile(playerNr);
        grid[x][y].add(robots[playerNr]);
        nPlayers++;
    }

    @Override
    public void moveRobot(int player, int move) {

        int possibleMove = 0;
        DIRECTION dir = robots[player].getOrientation();
        if (move < 0) {
            dir = dir.reverse();
            move = 1;
        }

        // check how far in the given direction it is possible to move (up to the move value)
        maxMove = 0;
        for (int i = 1; i <= move; i++) {
            possibleMove = (isMovePossible(player, i, dir)) ? i : possibleMove;
            if (possibleMove < i || maxMove == i) break;
        }


        if (possibleMove > 0) {

            Vector2 target = getDestination(robotPos[player], dir, possibleMove);

            updateRobotPos(player, target);
        }
    }

    /**
     * Check whether a move is possible with the given parameters
     *
     * @param player the player/robot to be moved
     * @param move   the number of tiles to check ahead
     * @param dir    the direction to check for possible movement
     * @return true if it is possible (not blocked by walls, edge of map or immovable robots)
     */
    private boolean isMovePossible(int player, int move, DIRECTION dir) {

        if (wallInCurrentTile(robotPos[player], dir)) return false;

        Vector2 target = getDestination(robotPos[player], dir, move);
        if (!containsPosition(target)) return false;

        // set maxMove to move+1 indicating it is possible to move into the target cell and beyond until it is found not to be possible
        maxMove = move + 1;

        // check what is in the target cell
        for (ICell item : grid[(int) target.x][(int) target.y]) {

            // if its a wall in the opposite end of the cell, this cell is the farthest the robot can go, set max move
            // if it's a wall facing the robot, return false
            if (item instanceof Wall) {
                if (((Wall) item).getDirection() == dir) maxMove = move;
                if (((Wall) item).getDirection() == dir.reverse()) return false;
            }

            if (item instanceof RobotTile) {

                int blockingRobot = -1;

                // get the playerNr of the blocking robot
                for (int i = 0; i < nPlayers; i++) {
                    if (i != player && robotPos[i].equals(target)) {
                        blockingRobot = i;
                    }
                }

                // recursively check if the robot can be pushed in the current direction
                boolean canMove = isMovePossible(blockingRobot, 1, dir);
                if (canMove) {
                    Vector2 blockingRobotTarget = getDestination(robotPos[blockingRobot], dir, 1);
                    updateRobotPos(blockingRobot, blockingRobotTarget);
                }
                return canMove;
            }
        }

        return true;
    }

    /**
     * @param pos the position to check.
     * @return true if the position is within board
     */
    @Override
    public boolean containsPosition(Vector2 pos) {
        return (pos.x > 0 && pos.y > 0) && (pos.x < grid.length && pos.y < grid[0].length);
    }

    @Override
    public void moveRobot(Integer id, DIRECTION direction) {
        updateRobotPos(id, getDestination(robotPos[id], direction, 1));
    }

    /**
     * @param robotPos the position of the robot
     * @param dir      the direction to check for wall
     * @return true if there's a wall in the robot is in (same direction as robot is going to move)
     */
    private boolean wallInCurrentTile(Vector2 robotPos, DIRECTION dir) {
        for (ICell item : grid[(int) robotPos.x][(int) robotPos.y]) {
            if (item instanceof Wall) {
                return (((Wall) item).getDirection() == dir);
            }
        }
        return false;
    }

    /**
     * Get the coordinates of the tile in the given direction and distance from pos
     *
     * @param pos      the source from which to find the new coordinates
     * @param dir      the direction of travel
     * @param distance the number of tiles to traverse
     * @return the target vector (coordinates)
     */
    public Vector2 getDestination(Vector2 pos, DIRECTION dir, int distance) {
        return new Vector2(pos.x + dir.getX()*distance,pos.y + dir.getY()*distance);
        /*Vector2 target;
        switch (dir) {
            case NORTH:
                target = new Vector2(pos.x, pos.y + distance);
                break;
            case EAST:
                target = new Vector2(pos.x + distance, pos.y);
                break;
            case SOUTH:
                target = new Vector2(pos.x, pos.y - distance);
                break;
            case WEST:
                target = new Vector2(pos.x - distance, pos.y);
                break;
            default:
                throw new IllegalStateException("Could not calculate target position.");
        }
        return target;*/
    }

    @Override
    public void hideRobot(int player) {
        Vector2 pos = robotPos[player];
        for (ICell item : grid[(int) pos.x][(int) pos.y]) {
            if (item instanceof RobotTile) {
                grid[(int) pos.x][(int) pos.y].remove(item);
                return;
            }
        }
    }



    /**
     * Move a robot to a target vector
     *
     * @param player the player/robot to move
     * @param target the destination coordinates
     */
    private void updateRobotPos(int player, Vector2 target) {
        Vector2 pos = robotPos[player];
        for (ICell item : grid[(int) pos.x][(int) pos.y]) {
            if (item instanceof RobotTile) {
                grid[(int) target.x][(int) target.y].add(item);
                grid[(int) pos.x][(int) pos.y].remove(item);
                robotPos[player] = target;
                return;
            }
        }
    }

    @Override
    public void rotateRobot(int currentPlayer, int rotate) {

        System.out.println("Attempting to rotate player " + currentPlayer + " " + rotate);

        switch (rotate) {
            case -1:
                robots[currentPlayer].rotateCCW();
                break;
            case 1:
                robots[currentPlayer].rotateCW();
                break;
            case 2:
                robots[currentPlayer].rotate180();
                break;
            default:
                throw new IllegalStateException("Invalid rotation value.");
        }

        System.out.println("Robot " + currentPlayer + " is headed " + robots[currentPlayer].getOrientation());
    }

    @Override
    public void rotateCogs() {
        for (int i = 0; i < cogPos.size(); i++) {
            for (int j = 0; j < nPlayers; j++) {
                if (cogPos.get(i).equals(robotPos[j])) {
                    rotateRobot(j, cogs.get(i).getRotation());
                }
            }
        }
    }

    @Override
    public void moveConveyors() {
        // TODO Auto-generated method stub
    }

    @Override
    public TreeSet<ICell> getCell(Vector2 a) {
        return grid[(int) a.x][(int) a.y];
    }

    @Override
    public RobotTile[] getRobots() {
        return robots;
    }
}
