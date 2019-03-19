package sky7.BoardTests;

import org.junit.Test;
import sky7.board.Board;
import sky7.board.BoardGenerator;
import sky7.board.ICell;
import sky7.board.cellContents.Inactive.FloorTile;

import java.io.FileNotFoundException;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

public class BoardTests {

    @Test
    public void generateBoardTest(){
        BoardGenerator bg = new BoardGenerator();
        try{
            Board board = bg.getBoardFromFile("src/test/assets/heightWithTest.json");
            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    TreeSet<ICell> treeSet = board.getTileTexture(x,y);
                    assertEquals(treeSet.first().getClass(), FloorTile.class);
                }
            }
            TreeSet<ICell> treeSet = board.getTileTexture(1,1);
            assertEquals(treeSet.first().getClass(), FloorTile.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
