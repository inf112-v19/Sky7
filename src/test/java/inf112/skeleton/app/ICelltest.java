package inf112.skeleton.app;

import org.junit.Test;
import sky7.board.ICell;
import sky7.board.cellContents.Active.SingelConveyorBelt;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.robots.RobotTile;

import java.util.ArrayList;

import static org.junit.Assert.assertNotEquals;

public class ICelltest {

    @Test
    public void noICellSamePriority(){
        ArrayList<ICell> list = new ArrayList<>();
        RobotTile robo = new RobotTile();
        Wall wall = new Wall(DIRECTION.EAST);
        FloorTile floor = new FloorTile();
        // TODO: Texture don't seem to be working
        // SingelConveyorBelt belt = new SingelConveyorBelt(DIRECTION.EAST, DIRECTION.WEST, DIRECTION.NORTH, "dasfa");
        list.add(robo);
        list.add(wall);
        list.add(floor);
        //list.add(belt);
        for (int i = 0; i < list.size()-1; i++) {
            for (int j = i+1; j < list.size(); j++) {
                assertNotEquals(list.get(i).drawPriority(), list.get(j).drawPriority());
            }
        }
    }


}
