package sky7.BoardTests;

import static org.junit.Assert.*;

import java.util.TreeSet;

import org.junit.Test;

import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.Active.Pusher;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.Inactive.Wrench;
import sky7.board.cellContents.robots.RobotTile;

public class DrawPriorityTest {

    @Test
    public void orderTest() {
        TreeSet<ICell> tile = new TreeSet<>();
        
        // insert ICell objects in the tile set in an order not consistent with draw priority layers
        // see DrawPriority.pdf in Documents
        tile.add(new Laser(true, null, 1));
        tile.add(new Wall(DIRECTION.NORTH));
        tile.add(new Wrench(1));
        tile.add(new Pusher(null, false));
        tile.add(new Wall(DIRECTION.EAST));
        tile.add(new RobotTile(1));
        tile.add(new FloorTile());
        
        // If any of the inserted elements have the same drawPriority, one of them should disappear
        assertTrue(tile.size() == 7);
        
        ICell[] tileArray = tile.toArray(new ICell[7]);
        
        // Assert that the ICell objects are now arranged in accordance with the draw priority layers
        assertTrue(tileArray[0] instanceof FloorTile);
        assertTrue(tileArray[1] instanceof Wrench);
        assertTrue(tileArray[2] instanceof Pusher);
        assertTrue(tileArray[3] instanceof Laser);
        assertTrue(tileArray[4] instanceof Wall);
        assertTrue(tileArray[5] instanceof Wall);
        assertTrue(tileArray[6] instanceof RobotTile);
        
    }

}
