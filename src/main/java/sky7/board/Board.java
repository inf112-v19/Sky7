package sky7.board;

import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.robots.RobotTile;

public class Board implements IBoard {
    private ICell[][] grid;
    private int width, height;
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new ICell[width][height];
        
        // fill grid with floor tiles
        for (int i=0; i<width ; i++) {
            for (int j=0; j<height ; j++) {
                grid[i][j] = new FloorTile();
            }
        }
        
        //add 1 robot
        ICell robot = new RobotTile();
        grid[5][4] = robot;
        
    }
    @Override
    public String getTileTexture(int x, int y) {
        return grid[x][y].getTexture();
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
    

}
