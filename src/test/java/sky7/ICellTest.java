package sky7;

import org.junit.Test;
import sky7.board.ICell;
import sky7.board.cellContents.Active.Belt;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.*;
import sky7.board.cellContents.robots.RobotTile;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ICellTest {

    @Test
    public void bottomTilesShouldHaveSamePriority(){
        ArrayList<ICell> list = new ArrayList<>();
        list.add(new Belt(1, 1));
        list.add(new FloorTile());
        list.add(new Hole());
        final int priority = 1;
        for (int i = 0; i < list.size(); i++) {
            assertEquals(priority, list.get(i).drawPriority());
        }

    }

    @Test
    public void middleTilesShouldHaveSamePriority(){
        ArrayList<ICell> list = new ArrayList<>();
        list.add(new Flag(1));
        list.add(new Laser(true, 1, 1));
        list.add(new Wrench(1));;
        final int middel = 3;
        for (int i = 0; i < list.size(); i++) {
            assertEquals(middel, list.get(i).drawPriority());
        }
    }

    @Test
    public void topLevelShouldHaveSamePriority() {
        ArrayList<ICell> list = new ArrayList<>();
        list.add(new RobotTile(1));

        final int top = 5;
        for (int i = 0; i < list.size(); i++) {
            assertEquals(top, list.get(i).drawPriority());
        }
    }

    @Test
    public void topMiddleLevelShouldHaveSamePriority(){
        ArrayList<ICell> list = new ArrayList<>();
        list.add(new Wall(DIRECTION.EAST));

        final int inBetween = 4;
        for (int i = 0; i < list.size(); i++) {
            assertEquals(inBetween,list.get(i).drawPriority());
        }
    }


}
