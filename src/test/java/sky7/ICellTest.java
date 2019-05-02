package sky7;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Active.Belt;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.Inactive.Flag;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Inactive.Hole;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.Inactive.Wrench;
import sky7.board.cellContents.robots.RobotTile;

public class ICellTest {

    @Test
    public void basicTilesShouldHaveSamePriority(){
        ArrayList<ICell> list = new ArrayList<>();
        list.add(new FloorTile());
        final int priority = 1;
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).drawPriority(), priority);
        }
    }
    @Test
    public void lowTilesShouldHaveSamePriority(){
        ArrayList<ICell> list = new ArrayList<>();
        list.add(new Wrench(1));
        list.add(new StartPosition(1));
        list.add(new Hole());
        final int priority = 2;
        for (int i = 0; i < list.size(); i++) {
            assertEquals(priority, list.get(i).drawPriority());
        }

    }

    @Test
    public void middleTilesShouldHaveSamePriority(){
        ArrayList<ICell> list = new ArrayList<>();
        list.add(new CogWheel(1));
        list.add(new Pusher(DIRECTION.NORTH,true));
        list.add(new Belt(DIRECTION.EAST, DIRECTION.WEST,1));
        final int middel = 3;
        for (int i = 0; i < list.size(); i++) {
            assertEquals(middel, list.get(i).drawPriority());
        }
    }

    @Test
    public void laserLayerShouldHaveCorrectPriority(){
        Laser start = new Laser(true, DIRECTION.EAST, 1);
        Laser middel = new Laser(false, DIRECTION.SOUTH, 2);
        assertEquals(start.drawPriority(), 5);
        assertEquals(middel.drawPriority(), 4);
    }

    @Test
    public void wallLayersShouldHaveCorrectPriority(){
        Wall wallN = new Wall(DIRECTION.NORTH);
        assertEquals(wallN.drawPriority(), 6);
        Wall wallE = new Wall(DIRECTION.EAST);
        assertEquals(wallE.drawPriority(), 7);
        Wall wallS = new Wall(DIRECTION.SOUTH);
        assertEquals(wallS.drawPriority(), 8);
        Wall wallW = new Wall(DIRECTION.WEST);
        assertEquals(wallW.drawPriority(), 9);

    }

    @Test
    public void roboTileShouldHaveCorrectPriority(){
        RobotTile robo = new RobotTile(1);
        assertEquals(robo.drawPriority(), 10);
    }



}
