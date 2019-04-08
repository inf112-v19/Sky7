package sky7.BoardTests;

import org.junit.Test;
import sky7.board.Board;
import sky7.board.BoardGenerator;
import sky7.board.ICell;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.robots.RobotTile;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class BoardTests {

    @Test
    public void generateBoardTest() {
        BoardGenerator bg = new BoardGenerator();
        try {
            Board board = bg.getBoardFromFile("src/test/assets/heightWithTest.json");
            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    TreeSet<ICell> treeSet = board.getTileTexture(x, y);
                    assertEquals(treeSet.first().getClass(), FloorTile.class);
                }
            }
            TreeSet<ICell> treeSet = board.getTileTexture(1, 1);
            assertEquals(treeSet.first().getClass(), FloorTile.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void roboMovingRoboTest() {
        TreeSet<ICell>[][] set = new TreeSet[10][10];
        for (int y = 0; y < set.length; y++) {
            for (int x = 0; x < set[y].length; x++) {
                set[x][y] = new TreeSet<ICell>();
                set[x][y].add(new FloorTile());
            }
        }

        Board board = new Board(set, 10, 10);
        board.placeRobot(0, 5, 5);


        assertTrue(isRoboHere(board, 5, 5));

        board.moveRobot(0,-1);
        board.moveRobot(0, -1);
        board.rotateRobot(0, 2 );   // facing SOUTH

                // (5,3)
        board.rotateRobot(0, 1);    // (Facing West)
        board.moveRobot(0, 1);             // (4,3)

        assertFalse(isRoboHere(board, 5, 5)); //RoboMoved
        assertTrue(isRoboHere(board, 4, 3)); // To (4,3)



    }

    private boolean isRoboHere(Board board, int x, int y) {
        if (x < 0 || x >= board.getWidth() || y < 0 || y >= board.getHeight()) {
            return false;
        }
        boolean found = false;
        TreeSet<ICell> tiles = board.getTileTexture(x, y);
        for (ICell cell : tiles) {
            if (cell instanceof RobotTile) {
                found = true;
            }
        }

        return found;
    }


    @Test
    public void pushRobo() {
        TreeSet<ICell>[][] set = new TreeSet[10][10];
        for (int y = 0; y < set.length; y++) {
            for (int x = 0; x < set[y].length; x++) {
                set[x][y] = new TreeSet<ICell>();
                set[x][y].add(new FloorTile());
            }
        }

        Board board = new Board(set, 10, 10);
        board.placeRobot(0, 5, 5);
        board.placeRobot(1, 5, 6);
        board.moveRobot(0, 1);

        assertTrue(isRoboHere(board, 5, 7)); // pushed one up

        board.rotateRobot(1, 2);
        board.moveRobot(1, 2); // moved them back to (5,5) and (5,4)

        assertTrue(isRoboHere(board, 5, 5));
        assertTrue(isRoboHere(board, 5, 4));

        assertFalse(isRoboHere(board, 5, 6)); // NOT WHERE THEY WHERE ANYMORE
        assertFalse(isRoboHere(board, 5, 7));// NOT WHERE THEY WHERE ANYMORE

        board.placeRobot(2, 5, 6); // testing TRippel push
        board.moveRobot(0, 1);

        assertTrue(isRoboHere(board, 5, 5));
        assertTrue(isRoboHere(board, 5, 6));
        assertTrue(isRoboHere(board, 5, 7));


        // TESTING PUSHING WEST AND EAST

        board.placeRobot(3, 4, 6);
        board.placeRobot(4, 3, 6);
        board.placeRobot(5, 2, 6);
        board.placeRobot(6, 1, 6);

        board.rotateRobot(1, 1);
        board.moveRobot(1, 1);

        assertTrue(isRoboHere(board, 0, 6));
        assertTrue(isRoboHere(board, 4, 6));

        board.moveRobot(1, 1);
        assertTrue(isRoboHere(board, 4, 6));
        board.moveRobot(1, 2);
        assertTrue(isRoboHere(board, 4, 6));

    }

    @Test
    public void cogs() {
        TreeSet<ICell>[][] set = new TreeSet[10][10];
        for (int y = 0; y < set.length; y++) {
            for (int x = 0; x < set[y].length; x++) {
                set[x][y] = new TreeSet<ICell>();
                set[x][y].add(new FloorTile());
            }
        }

        set[4][4].add(new CogWheel(1));
        Board board = new Board(set, 10, 10);
        board.placeRobot(0, 4, 3);
        board.rotateCogs();
        board.moveRobot(0, 1);
        assertTrue(isRoboHere(board, 4, 4));
        board.rotateCogs();
        board.moveRobot(0, 1);
        assertTrue(isRoboHere(board, 5, 4));
        board.rotateCogs();
        board.rotateCogs();
        board.moveRobot(0, 2);
        assertTrue(isRoboHere(board, 7, 4));

        board.placeRobot(1, 4, 4);
        board.rotateCogs();
        board.rotateCogs();
        board.rotateCogs();
        board.moveRobot(1, 2);
        assertTrue(isRoboHere(board, 2, 4));
    }

    @Test
    public void isMovePossibalTest() {
        TreeSet<ICell>[][] set = new TreeSet[10][10];
        for (int y = 0; y < set.length; y++) {
            for (int x = 0; x < set[y].length; x++) {
                set[x][y] = new TreeSet<ICell>();
                set[x][y].add(new FloorTile());
            }
        }


        set[5][2].add(new Wall(DIRECTION.EAST));
        set[4][6].add(new Wall(DIRECTION.WEST));
        Board board = new Board(set, set[0].length, set.length);
        int currX = 5;
        int currY = 5;
        board.placeRobot(0, currX, currY);
        DIRECTION roboDir = DIRECTION.NORTH;
        Random r = new Random();
        int nrFail = 0;
        for (int i = 0; i < 100000; i++) {
            int oneInFour = r.nextInt(4);
            switch (oneInFour) {
                case 0:
                    break; // Don't trun;
                case 1:
                    board.rotateRobot(0, 1);
                    roboDir = turnRight(roboDir);
                    break;
                case 2:
                    board.rotateRobot(0, 2);
                    roboDir = roboDir.reverse();
                    break;
                case 3:
                    board.rotateRobot(0, -1);
                    roboDir = turnLeft(roboDir);
                    break;

            }


            boolean canGo = false;
            int newX = currX;
            int newY = currY;
            switch (roboDir) {
                case NORTH:
                    canGo = checkCanGo(board, roboDir, currX, currY + 1);
                    newY++;
                    ;
                    break;
                case WEST:
                    canGo = checkCanGo(board, roboDir, currX - 1, currY);
                    newX--;
                    break;
                case SOUTH:
                    canGo = checkCanGo(board, roboDir, currX, currY - 1);
                    newY--;
                    break;
                case EAST:
                    canGo = checkCanGo(board, roboDir, currX + 1, currY);
                    newX++;
                    break;
            }

            boolean canLeave = canLeave(board, roboDir, currX, currY);
            board.moveRobot(0, 1);
            canGo = canGo && canLeave;

            if (canGo) {
                assertTrue(isRoboHere(board, newX, newY));
                assertFalse(isRoboHere(board, currX, currY));
                currX = newX;
                currY = newY;
            } else {
                nrFail++;
                assertFalse(isRoboHere(board, newX, newY));
                assertTrue(isRoboHere(board, currX, currY));
            }
        }
        System.out.println(nrFail);
    }

    private boolean canLeave(Board board, DIRECTION dir, int x, int y) {
        boolean canGo = true;
        TreeSet<ICell> tiles = board.getTileTexture(x, y);
        for (ICell cell : tiles) {
            if (cell instanceof Wall) {
                Wall wall = (Wall) cell;
                if (dir == wall.getDirection()) {
                    canGo = false;
                }
            }
        }
        return canGo;
    }

    private boolean checkCanGo(Board board, DIRECTION dir, int x, int y) {
        if (x < 0 || x >= board.getWidth() || y < 0 || y >= board.getHeight()) {
            return false;
        }
        boolean canGo = true;
        TreeSet<ICell> tiles = board.getTileTexture(x, y);
        for (ICell cell : tiles) {
            if (cell instanceof Wall) {
                Wall wall = (Wall) cell;
                if (dir.reverse() == wall.getDirection()) {
                    canGo = false;
                }
            }
        }
        return canGo;
    }

    private DIRECTION turnRight(DIRECTION dir) {
        switch (dir) {
            case NORTH:
                return DIRECTION.EAST;
            case EAST:
                return DIRECTION.SOUTH;
            case SOUTH:
                return DIRECTION.WEST;
            case WEST:
                return DIRECTION.NORTH;
            default:
                throw new IllegalArgumentException("Dir: " + dir + "is not one of the four DIRECTIONs");
        }
    }

    private DIRECTION turnLeft(DIRECTION dir) {
        switch (dir) {
            case NORTH:
                return DIRECTION.WEST;
            case WEST:
                return DIRECTION.SOUTH;
            case SOUTH:
                return DIRECTION.EAST;
            case EAST:
                return DIRECTION.NORTH;
            default:
                throw new IllegalArgumentException("Dir: " + dir + "is not one of the four DIRECTIONs");
        }
    }

}
