package sky7.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector2;

import sky7.board.cellContents.Active.Belt;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.Active.IConveyorBelt;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.Active.Pusher;
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
    private List<Belt> convs;
    private List<Vector2> convPos;
    private List<Laser> lasers;
    private List<Vector2> laserPos;
    private ArrayList<Vector2> holePos;
    private ArrayList<Hole> holes;
    private List<Vector2> pusherPos;
    private List<Pusher> pushers;

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
        this.pushers = new ArrayList<>();
        this.pusherPos = new ArrayList<>();

        // find and store locations of cogwheels, conveyor belts
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (ICell item : grid[i][j]) {
                    if (item instanceof CogWheel) {
                        cogPos.add(new Vector2(i, j));
                        cogs.add((CogWheel) item);
                    }
                    if (item instanceof Belt) {
                        convPos.add(new Vector2(i, j));
                        convs.add((Belt) item);
                    }
                    if (item instanceof Laser) {
                        laserPos.add(new Vector2(i, j));
                        lasers.add((Laser) item);
                    }
                    if (item instanceof Hole) {
                        holePos.add(new Vector2(i, j));
                        holes.add((Hole) item);
                    }
                    if (item instanceof Pusher) {
                        pusherPos.add(new Vector2(i, j));
                        pushers.add((Pusher) item);
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
        return (pos.x >= 0 && pos.y >= 0) && (pos.x < grid.length && pos.y < grid[0].length);
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
        List<Vector2> positions = new ArrayList<>();
        List<RobotTile> robosWantsToMove = new ArrayList<>();
        List<Belt> convsToBeMoved = new ArrayList<>();

        // find all robots on converyor belts, and their position
        for (int i = 0; i < convs.size(); i++) {
            for (int j = 0; j < robots.length; j++) {
                if(convPos.get(i).equals(robotPos[j])){
                    positions.add(convPos.get(i)); // or robotPos[j]
                    robosWantsToMove.add(robots[j]);
                    convsToBeMoved.add(convs.get(i));
                }
            }
        }

        // remove robots (and belts) if the robot can not be moved
        for (int i = 0; i < robosWantsToMove.size(); i++) {
            DIRECTION dir = robosWantsToMove.get(i).getOrientation();
            int x = (int) positions.get(i).x;
            int y = (int) positions.get(i).y;

            DIRECTION to = convsToBeMoved.get(i).getDirectionTo();


            if(!canConvoPush(x,y, to)){
                System.out.println(robosWantsToMove.get(i).getId() + " can't move");
                robosWantsToMove.remove(i);
                positions.remove(i);
                convsToBeMoved.remove(i);
                i--;
            }
        }

        // TODO: move the robots.
        for (int i = 0; i < robosWantsToMove.size(); i++) {
            RobotTile robo = robosWantsToMove.get(i);
            Belt belt = convsToBeMoved.get(i);
            Vector2 vec = positions.get(i);

            int[] coords = DIRECTION.getNewPosMoveDir((int)vec.x,(int)vec.y, belt.getDirectionTo());
            int newx = coords[0];
            int newy = coords[1];

            TreeSet<ICell> newCells = grid[newx][newy];
            // if there is no belt, just keep going in same dir;
            DIRECTION dir = belt.getDirectionTo();
            for (ICell cell: newCells) {
                if(cell instanceof Belt){
                    Belt newBelt = (Belt) cell;
                    if(newBelt.getDirectionFrom().reverse() == belt.getDirectionTo() ||
                            (newBelt.getDirectionFromAlt() != null && newBelt.getDirectionFromAlt().reverse() == belt.getDirectionTo())){
                        dir = newBelt.getDirectionTo();
                    }
                }
            }

            int rotate = belt.getDirectionTo().directionToRotation(dir);
            int roboNr = robo.getId();


            // TODO: make it handle out of bonds
            moveRobot(roboNr, belt.getDirectionTo());
            if(rotate != 0) {
                rotateRobot(roboNr, rotate);
            }
        }
    }

    private boolean canConvoPush(int x, int y, DIRECTION to) {
        System.out.println("------------ checking if the convo can be pused--------");

        //checking if we can leave current location
        TreeSet<ICell> localCells = getTileTexture(x,y);
        for(ICell cell : localCells){
            if(cell instanceof Wall){
                Wall wall = (Wall) cell;
                if(wall.getDirection() == to){
                    System.out.println(" Early wall return");
                    return false;
                }
            }
        }

        int newX = x;
        int newY = y;
        switch (to){
            case EAST: newX--; break;
            case WEST:newX++; break;
            case NORTH: newY++; break;
            case SOUTH:newY--; break;

        }

        if(!containsPosition(new Vector2(newX, newY))){
            System.out.println("early out of bonds return");
            return true;
        }



        // checking if we can enter the new place
        TreeSet<ICell> nextCells = getTileTexture(newX,newY);
        boolean canMove = true;

        boolean foundBelt = false;
        Belt belt = null;

        boolean foundRobo = false;
        RobotTile robo = null;


        for (ICell cell : nextCells) {
            if(cell instanceof Wall){
                Wall wall = (Wall) cell;
                // we are trying to enter, but a wall is blocking
                if(wall.getDirection().reverse() == to){
                    System.out.println("hitiing wall fail");
                    return false;
                }
            } else if(cell instanceof Belt){
                Belt newBelt = (Belt)cell;
                if(newBelt.getDirectionFrom().reverse() == to || (newBelt.getDirectionFromAlt() != null && newBelt.getDirectionFromAlt().reverse() == to)){
                    boolean doubleRobo =  tCrossCheck(newX,newY, newBelt);
                    if(doubleRobo){
                        System.out.println("early T return");
                        return false;
                    }
                    belt = newBelt;
                    foundBelt = true;
                }

            } else if(cell instanceof RobotTile){
                foundRobo = true;
                robo = (RobotTile) cell;
            }
        }

        if(foundRobo && !foundBelt){
            System.out.println("early robo no belt return");
            return false;

        } else if(foundRobo && foundBelt){
            DIRECTION newTo = belt.getDirectionTo();
            System.out.println("check if robo can be pushed on wards");
            return canConvoPush(newX,newY, newTo);
        }

        System.out.println("final return just true");
        return true;

    }

    /**
     * Check if a given belt has two ingoing paths, and if there are two ingoing paths
     * if there are coming in two robots in to the new point.
     *
     * @param beltX
     * @param beltY
     * @param checkBelt
     * @return true if two robos would collide if both moved, false else.
     */
    private boolean tCrossCheck(int beltX, int beltY, Belt checkBelt) {
        DIRECTION from1 = checkBelt.getDirectionFrom();
        DIRECTION from2 = checkBelt.getDirectionFromAlt();
        if(from2 == null){
            System.out.println("early here");
            return false;
        }

        int[] fstPair = DIRECTION.getNewPosMoveDir(beltX, beltY, from1);
        int fstX = fstPair[0];
        int fstY = fstPair[1];

        int[] sndPair = DIRECTION.getNewPosMoveDir(beltX, beltY, from2);
        int sndX = sndPair[0];
        int sndY = sndPair[1];

        //is there a belt and a rbot moving in to this belt on point (fstX, fstY)

        boolean roboFrom1 = isThereABeltAndRobotOnEntryOne(fstX,fstY,from1);
        boolean roboFrom2 = isThereABeltAndRobotOnEntryOne(sndX,sndY,from2);

        return roboFrom1&&roboFrom2;

    }

    private boolean isThereABeltAndRobotOnEntryOne(int x, int y, DIRECTION from1) {
        if(!containsPosition(new Vector2(x, y))){
            return false;
        }
        TreeSet<ICell> cells = grid[x][y];
        boolean foundRobo = false;
        boolean beltCorrectWay = false;
        for(ICell cell: cells){
            if(cell instanceof RobotTile){
                foundRobo = true;
            } else if(cell instanceof Belt){
                Belt newBelt = (Belt) cell;
                if(newBelt.getDirectionTo() == from1.reverse()){
                    beltCorrectWay = true;
                }

            }
        }

        return foundRobo && beltCorrectWay;
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
