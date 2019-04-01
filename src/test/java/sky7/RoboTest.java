package sky7;

import org.junit.Test;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.robots.RobotTile;

import static org.junit.Assert.assertEquals;

public class RoboTest {

    @Test
    public void roboRotateTest(){
        RobotTile robo = new RobotTile(1);
        DIRECTION dir = robo.getOrientation(); // should start North
        assertEquals(dir, DIRECTION.NORTH);


        robo.rotate180();
        assertEquals(robo.getOrientation(), DIRECTION.SOUTH);

        robo.rotateCCW();
        assertEquals(robo.getOrientation(), DIRECTION.EAST);

        robo.rotateCW();
        assertEquals(robo.getOrientation(), DIRECTION.SOUTH);

        robo.rotateCW();
        assertEquals(robo.getOrientation(), DIRECTION.WEST);

        robo.rotateCW();
        assertEquals(robo.getOrientation(), DIRECTION.NORTH);

    }

}
