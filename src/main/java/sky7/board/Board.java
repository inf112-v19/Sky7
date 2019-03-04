package sky7.board;
    
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector2;

import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.robots.RobotTile;

public class Board implements IBoard {
    private TreeSet<ICell>[][] grid;
    private int width, height;
    private HashMap<Integer, Vector2> robotPos;

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
        
    }

    public Board(TreeSet<ICell>[][] grid,int height, int width){
        this.grid = grid;
        this.width = width;
        this.height = height;
        this.robotPos = new HashMap<>();

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
        robotPos.put(playerNr, new Vector2(x,y));
        grid[x][y].add(new RobotTile(playerNr));
    }

    @Override
    public void moveRobot(int player, int move) {
        Vector2 pos = robotPos.get(player);
        int possibleMove = 0;
        if ((possibleMove = isMovePossible(player, move)) > 0) {
            
        }
    }

    private int isMovePossible(int player, int move) {
        
        
        
        return 0;
    }


}
