package sky7.board;

import com.badlogic.gdx.math.Vector2;


import sky7.board.cellContents.Active.Belt;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.Inactive.*;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.Active.Pusher;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.Flag;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Inactive.Hole;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.robots.RobotTile;

import java.util.*;

public class Board implements IBoard {
    private TreeSet<ICell>[][] grid;
    private int width, height, nPlayers, maxMove;
    private Vector2[] robotPos;
    private RobotTile[] robots;
    private Vector2[] deadRobotPos;
    private RobotTile[] deadRobots;
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
    private List<Vector2> startPositions;
    private List<StartPosition> startCells;
    private List<Flag> flags;
    private List<Vector2> flagPositions;
    private List<Wrench> wrenches;
    private List<Vector2> wrenchPositions;
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
        this.deadRobotPos = new Vector2[8];
        this.deadRobots = new RobotTile[8];
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
        this.startPositions = new ArrayList<>();
        this.startCells = new ArrayList<>();
        this.flags = new ArrayList<>();
        this.flagPositions = new ArrayList<>();
        this.wrenchPositions = new ArrayList<>();
        this.wrenches = new ArrayList<>();


        // find and store locations of interactive tiles
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
                    else if (item instanceof Laser) {
                        laserPos.add(new Vector2(i, j));
                        lasers.add((Laser) item);
                    }
                    else if (item instanceof Hole) {
                        holePos.add(new Vector2(i, j));
                        holes.add((Hole) item);
                    }
                    else if (item instanceof Pusher) {
                        pusherPos.add(new Vector2(i, j));
                        pushers.add((Pusher) item);
                    }
                    else if (item instanceof StartPosition) {
                        startPositions.add(new Vector2(i, j));
                        startCells.add((StartPosition) item);
                    }
                    else if (item instanceof Flag) {
                        flagPositions.add(new Vector2(i, j));
                        flags.add((Flag) item);
                    }
                    else if (item instanceof Wrench) {
                        wrenchPositions.add(new Vector2(i, j));
                        wrenches.add((Wrench) item);
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
        return new Vector2(pos.x + dir.getX() * distance, pos.y + dir.getY() * distance);
    }

    @Override
    public void hideRobot(int player) {
        Vector2 pos = robotPos[player];
        deadRobots[player] = robots[player];
        robots[player] = null;
        deadRobotPos[player] = robotPos[player];
        robotPos[player] = null;

        for (ICell item : grid[(int) pos.x][(int) pos.y]) {
            if (item instanceof RobotTile) {
                grid[(int) pos.x][(int) pos.y].remove(item);
                return;
            }
        }
    }

    @Override
    public List<Laser> getLasers() {
        return lasers;
    }

    @Override
    public List<Vector2> getLaserPos() {
        return laserPos;
    }

    @Override
    public void addCell(ICell cell, Vector2 pos) {
        grid[(int) pos.x][(int) pos.y].add(cell);
    }

    @Override
    public List<Vector2> getStartPositions() {
        return startPositions;
    }

    @Override
    public List<StartPosition> getStartCells() {
        return startCells;
    }

    @Override
    public List<Flag> getFlags() {
        return flags;
    }

    @Override
    public List<Vector2> getFlagPositions() {
        return flagPositions;
    }

    @Override
    public List<Wrench> getWrenches() {
        return wrenches;
    }

    @Override
    public List<Vector2> getWrenchPositions() {
        return wrenchPositions;
    }

    @Override
    public void removeCell(ICell cell, Vector2 pos) {
        if (grid[(int) pos.x][(int) pos.y].contains(cell)) {
            grid[(int) pos.x][(int) pos.y].remove(cell);
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

                // Checking that the nr is correct

                RobotTile robo = (RobotTile) item;
                if(robo.getId() == player) {
                    grid[(int) target.x][(int) target.y].add(item);
                    grid[(int) pos.x][(int) pos.y].remove(item);
                    robotPos[player] = target;
                    return;
                }
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
    public void moveConveyors(boolean singelAndDouble) {
        List<Vector2> positions = new ArrayList<>();
        List<RobotTile> robosWantsToMove = new ArrayList<>();
        List<Belt> convsToBeMoved = new ArrayList<>();

        // find all robots on converyor belts, and their position
        for (int i = 0; i < convs.size(); i++) {
            for (int j = 0; j < robots.length; j++) {
                if(convPos.get(i).equals(robotPos[j])){
                    if(singelAndDouble || convs.get(i).getType() == 0) {
                        positions.add(convPos.get(i)); // or robotPos[j]
                        robosWantsToMove.add(robots[j]);
                        convsToBeMoved.add(convs.get(i));
                    }

                }
            }
        }

        // remove robots (and belts and vectors) if the robot can not be moved
        for (int i = 0; i < robosWantsToMove.size(); i++) {
            DIRECTION to = convsToBeMoved.get(i).getDirectionTo();

            System.out.println("------------ Checking if robo nr " +robosWantsToMove.get(i).getId() + " can be moved--------");
            if(!canConvoPush(positions.get(i), to, !singelAndDouble)){
                System.out.println("------------ Robo nr" + robosWantsToMove.get(i).getId() + " can't move -----------");
                robosWantsToMove.remove(i);
                positions.remove(i);
                convsToBeMoved.remove(i);
                i--;
            } else {
                System.out.println("------------ Robo nr " + robosWantsToMove.get(i).getId() + " can be moved--------");
            }
        }



        // Moving the robots
        for (int i = 0; i < robosWantsToMove.size(); i++) {
            RobotTile robo = robosWantsToMove.get(i);
            Belt belt = convsToBeMoved.get(i);

            Vector2 coords = positions.get(i);
            activateBelt(robo,belt,coords);



        }
    }

    private void activateBelt(RobotTile robo, Belt belt, Vector2 coords) {
        int roboNr = robo.getId();
        System.out.println("----- acualy moving robo nr " + roboNr + "---------");

        Vector2 newCoords = getDestination(coords,belt.getDirectionTo(),1);
        int rotate = getRotateValue(belt, newCoords);
        // do the accual move
        updateRobotPos(roboNr, newCoords);
        if(rotate != 0) {
            rotateRobot(roboNr, rotate);
        }

        System.out.println("------------ Done moving robot nr: " + roboNr + "-----------");
    }

    private int getRotateValue(Belt belt, Vector2 coords) {
        TreeSet<ICell> newCells = getCell(coords);
        // if there is no belt, just keep going in same dir;
        DIRECTION dir = belt.getDirectionTo();

        // check if we have to rotate robo
        for (ICell cell: newCells) {
            if(cell instanceof Belt){
                Belt newBelt = (Belt) cell;
                if(newBelt.getDirectionFrom().reverse() == belt.getDirectionTo() ||
                        (newBelt.getDirectionFromAlt() != null && newBelt.getDirectionFromAlt().reverse() == belt.getDirectionTo())){
                    dir = newBelt.getDirectionTo();
                }
            }
        }

        return belt.getDirectionTo().directionToRotation(dir);
    }

    private boolean canConvoPush(Vector2 curCoords, DIRECTION to, boolean onlyExpress) {
        System.out.println("Recurtion trap");

        //checking if we can leave current location
        if(wallInCurrentTile(curCoords, to)){
            return false;
        }


        Vector2 newCoords = getDestination(curCoords, to,1);

        if(!containsPosition(newCoords)){
            return true; //the robot can be pushed of the map
        }

        if(moreThanOneRoboEnteringThisTile(newCoords, onlyExpress)){
            return false; //can't enter if two robots try to enter
        }




        // checking if we can enter the new place

        TreeSet<ICell> nextCells = getCell(newCoords);

        /*
         * The two boolean under is used to check if there is  a belt, and a robot in the tile we are trying to move to.
         * If there is, we have to check if the robot on the convo belt can be moved.
         * If there is robot, and no belt. Current robot can't move.
         *
         * If there is a wall on the way in to the next tile, current Robot can't move.
         *
         */

        // todo: might putting this in a method
        boolean foundBelt = false;
        Belt belt = null;

        boolean foundRobo = false;


        for (ICell cell : nextCells) {
            if(cell instanceof Wall){
                Wall wall = (Wall) cell;
                // we are trying to enter, but a wall is blocking
                if(wall.getDirection().reverse() == to){
                    return false;
                }
            } else if(cell instanceof Belt){
                Belt newBelt = (Belt)cell;

                // checking if the other belt is singel typed.
                if(onlyExpress && newBelt.getType()!=0){
                    continue;
                }
                /* if the belt is leaving somewhere else (then the checking robo is standing)
                 * then both can move, as long as the next Robo can move (check further down).
                 */
                if(newBelt.getDirectionTo().reverse() != to){
                    belt = newBelt;
                    foundBelt = true;
                }

            } else if(cell instanceof RobotTile){
                foundRobo = true;
            }
        }

        if(foundRobo && !foundBelt){
            return false;

        } else if(foundRobo && foundBelt){
            DIRECTION newTo = belt.getDirectionTo();
            return canConvoPush(newCoords, newTo, onlyExpress);
        }
        return true;

    }

    private boolean moreThanOneRoboEnteringThisTile(Vector2 coord, boolean onlyExpress) {
        int nrOfRobosGoingToCurrentTile = 0;
        for (DIRECTION dir : DIRECTION.values()) {

            Vector2 newCords = getDestination(coord,dir,1);
            if(roboEntriningFromMultipalDir(newCords, dir.reverse(), onlyExpress)){
                nrOfRobosGoingToCurrentTile++;
            }
        }

        return nrOfRobosGoingToCurrentTile > 1;
    }




    /**
     * Check if a given belt has two ingoing paths, and if there are two ingoing paths
     * if there are coming in two robots in to the new point.
     *
     * @param x
     * @param y
     * @param from1
     * @return true if two robos would collide if both moved, false else.
     */
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
    public List<Vector2> getPusherPos(){
        return pusherPos;



    }
    @Override
    public List<Pusher> getPushers(){
        return pushers;
    }




   /* @Override
    public Map<Integer, Flag> robotVisitFlag(){
        Map<Integer,Flag> robotWhoVisitedFlag = new HashMap<>();
        for(int i=0; i<flags.size(); i++){
            for(int j=0; j<robots.length; j++){
                if(flagsPos.get(i).equals(robotPos[j])){
                    int idOfRobot = robots[j].getId();
                    robotWhoVisitedFlag.put(idOfRobot,flags.get(i));
                }
            }
        }
        return robotWhoVisitedFlag;
    }*/

    private boolean roboEntriningFromMultipalDir(Vector2 coords, DIRECTION dir, boolean onlyExpress) {
        if(!containsPosition(coords)){
            return false;
        }
        TreeSet<ICell> cells = getCell(coords);
        boolean foundBeltLeavingInDir = false;
        boolean foundRobo = false;
        for(ICell cell : cells){
            if(cell instanceof Belt){
                Belt belt = (Belt) cell;
                if(onlyExpress && belt.getType() != 0){
                    continue;
                }
                if(belt.getDirectionTo() == dir){
                    foundBeltLeavingInDir = true;
                }
            } else if(cell instanceof RobotTile){
                foundRobo = true;
            }
        }

        return foundBeltLeavingInDir && foundRobo;
    }




    @Override
    public TreeSet<ICell> getCell(Vector2 a) {
        return grid[(int) a.x][(int) a.y];
    }

    @Override
    public RobotTile[] getRobots() {
        return robots;
    }
  /*  private boolean wallInCurrentTileMadeByMaren(Vector2 robotPos, DIRECTION dir) {
        for (ICell item : grid[(int) robotPos.x][(int) robotPos.y]) {
            if (item instanceof Wall) {
                return (((Wall) item).getDirection() == dir.reverse());
            }
        }
        return false;
    }*/


}
