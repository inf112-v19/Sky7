package sky7.board;
    
import java.util.Arrays;
import java.util.TreeSet;

import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.robots.RobotTile;

public class Board implements IBoard {
    private TreeSet<ICell>[][] grid;
    private int width, height;

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
        grid[5][4].add(new RobotTile());
        
    }

    public Board(TreeSet<ICell>[][] grid,int height, int width){
        this.grid = grid;
        this.width = width;
        this.height = height;

    }
    @Override
    public String[] getTileTexture(int x, int y) {
        String[] tiles = new String[grid[x][y].size()];
        int i = 0;
        for (ICell tile : grid[x][y]) {
            tiles[i++] = tile.getTexture();
        }
        
        return tiles;
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
}
