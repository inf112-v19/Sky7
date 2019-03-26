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
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.robots.RobotTile;

public class Board implements IBoard {
    private TreeSet<ICell>[][] grid;
    private int width, height, nPlayers;
    private Vector2[] robotPos;
    private RobotTile[] robots;
    private List<CogWheel> cogs;
    private List<Vector2> cogPos;
    private List<IConveyorBelt> convs;
    private List<Vector2> convPos;
    private List<Laser> lasers;
    private List<Vector2> laserPos;

     public Board(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new TreeSet[width][height];
        
        // fill grid with floor tiles
        for (int i=0; i<width ; i++) {
            for (int j=0; j<height ; j++) {
                grid[i][j] = new TreeSet<ICell>();
                grid[i][j].add(new FloorTile()); //= new FloorTile();
            }
        }
        
        //add 1 robot
        grid[5][4].add(new RobotTile(0));
        nPlayers = 1;
        
    }

    public Board(TreeSet<ICell>[][] grid,int height, int width){
        this.grid = grid;
        this.width = width;
        this.height = height;
        this.nPlayers = 1;
        this.robotPos = new Vector2[8];
        this.robots = new RobotTile[8];
        this.cogs = new ArrayList<>();
        this.cogPos = new ArrayList<>();
        this.convs = new ArrayList<>();
        this.convPos = new ArrayList<>();
        this.lasers = new ArrayList<>();
        this.laserPos = new ArrayList<>();
        
        // find and store locations of cogwheels, conveyor belts
        for (int i=0; i<grid.length; i++) {
            for (int j=0; j<grid[0].length; j++) {
                for (ICell item : grid[i][j]) {
                    if (item instanceof CogWheel) { 
                        cogPos.add(new Vector2(i,j));
                        cogs.add((CogWheel)item);
                    }
                    if (item instanceof IConveyorBelt) {
                        convPos.add(new Vector2(i,j));
                        convs.add((IConveyorBelt)item);
                    }
                    if (item instanceof Laser) {
                        laserPos.add(new Vector2(i,j));
                        lasers.add((Laser)item);
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
        robotPos[playerNr] = new Vector2(x,y);
        robots[playerNr] = new RobotTile(playerNr);
        grid[x][y].add(robots[playerNr]);
    }

    @Override
    public void moveRobot(int player, int move) {
        
        int possibleMove = 0;
        DIRECTION dir = robots[player].getOrientation();
        if (move < 0) {
            dir = dir.inverse(dir);
            move = 1;
        }
        
        // check how far in the given direction it is possible to move (up to the move value)
        for (int i=1; i<=move; i++) {
            possibleMove = (isMovePossible(player, i, dir)) ? i : possibleMove;
            if (possibleMove < i) break;
        }
        
        
        if (possibleMove > 0) {
            
            Vector2 target = new Vector2(0,0);
            
            switch (dir) {
            case NORTH:
                target = new Vector2(robotPos[player].x, robotPos[player].y+possibleMove);
                break;
            case EAST:
                target = new Vector2(robotPos[player].x+possibleMove, robotPos[player].y);
                break;
            case SOUTH:
                target = new Vector2(robotPos[player].x, robotPos[player].y-possibleMove);
                break;
            case WEST:
                target = new Vector2(robotPos[player].x-possibleMove, robotPos[player].y);
                break;
            default:
                throw new IllegalStateException("Found no orientation for robot " + player);
            }
            
            updateRobotPos(player, target);
        }
    }
    
    private boolean isMovePossible(int player, int move, DIRECTION dir) {
        
        // return false if there's a wall in tile which the robot is in (same direction as robot is going to move)
        for (ICell item : grid[(int) robotPos[player].x][(int) robotPos[player].y]) {
            if (item instanceof Wall) {
                if (((Wall)item).getDirection() == dir) return false;
            }
        }
        
        Vector2 target = new Vector2(0,0);
        
        switch (dir) {
        case NORTH:
            target = new Vector2(robotPos[player].x, robotPos[player].y+move);
            break;
        case EAST:
            target = new Vector2(robotPos[player].x+move, robotPos[player].y);
            break;
        case SOUTH:
            target = new Vector2(robotPos[player].x, robotPos[player].y-move);
            break;
        case WEST:
            target = new Vector2(robotPos[player].x-move, robotPos[player].y);
            break;
        default:
            throw new IllegalStateException("Found no orientation for robot " + player);
        }
        
        // check that new pos is within grid
        if (target.x < 0 || target.y < 0) return false;
        if (target.x >= grid.length || target.y >= grid[0].length) return false;
        
        // check what is in the target cell
        for(ICell item : grid[(int) target.x][(int) target.y]) {
            
            // if it's a wall facing the robot, return false
            if (item instanceof Wall && ((Wall) item).getDirection() == dir.inverse(dir)) return false;
            
            if (item instanceof RobotTile) {
                
                int blockingRobot = -1;
                
                // get the playerNr of the blocking robot
                for (int i=0; i<nPlayers; i++) {
                    if (i != player && robotPos[i] == target) {
                        blockingRobot = i;
                    }
                }
                
                // recursively check if the robot can be pushed in the current direction
                boolean canMove = isMovePossible(blockingRobot, 1, dir);
                if (canMove) updateRobotPos(blockingRobot, target);
                return canMove;
            }
        }
        
        return true;
    }

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
        
        System.out.println("Attempting to rotate " + rotate);
        
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
    }

    @Override
    public void rotateCogs() {
        for (int i=0; i<cogPos.size(); i++) {
            for (int j=0; j<nPlayers; j++) {
                if (cogPos.get(i).equals(robotPos[i])) 
                    rotateRobot(i, cogs.get(i).getRotation());
            }
        }
    }

    @Override
    public void moveConveyors() {
        // TODO Auto-generated method stub
    }


}
