package inf112.skeleton.app;

import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverAnonymous;
import org.junit.Test;
import sky7.board.Board;
import sky7.board.BoardGenerator;
import sky7.board.IBoard;
import sky7.board.IBoardGenerator;
import sky7.board.cellContents.Inactive.FloorTile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.*;

public class IBoardGeneratorTest {
    private Random r = new Random();

    /**
     * Generates a random board with one element in each slot,
     * and checks if the boardgenerator can read it
     *
     * First it generates valid baordGenerator values,
     * then it writes a random amount of it to a json file
     *
     * Then a IBoardGenerator tries to read it
     */
    //@Test TODO: check that the test only check things that are implemented
    public void checkThatRandomBoardCanBeRead(){
        try {
            generateLongStringWithRandomObjects();
            IBoardGenerator generator = new BoardGenerator();
            Board board = generator.getBoardFromFile("src/test/assets/checkForRadnomeInput.json");
            System.out.println(board.getWidth());
            System.out.println(board.getHeight());
            assertTrue(true); // if it gets here everthing okey
        }catch (FileNotFoundException e) {
            fail("did'nt pass");
            e.fillInStackTrace();
        }
    }


    private void generateLongStringWithRandomObjects(){
        ArrayList<String> listOfAll = new ArrayList<>();
        String[] direction = {"N", "S", "E", "W"};
        String[] clockWise = {"C", "A"};
        String[] flagNumbers = {"1", "2", "3", "4"};
        String[] nrOfLasers = {"1", "2"};
        String[] playerNr = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] startPlace = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] wrenchtool = {"1","2"};

        addOneOf("b", direction, listOfAll);
        addOneOf("c", clockWise, listOfAll);
        listOfAll.add("f");
        addOneOf("g", flagNumbers, listOfAll);
        listOfAll.add("h");
        addOneOf("lN", nrOfLasers, listOfAll);
        addOneOf("lW", nrOfLasers, listOfAll);
        addOneOf("lS", nrOfLasers, listOfAll);
        addOneOf("lE", nrOfLasers, listOfAll);
        addOneOf("r", playerNr, listOfAll);
        addOneOf("s", startPlace, listOfAll);
        addOneOf("t", wrenchtool, listOfAll);
        addOneOf("w", direction, listOfAll);


        int height = r.nextInt(100) +1;
        int width = r.nextInt(100) +1;

        String string = "";
        for (int i = 0; i < height*width; i++) {
            int randomNr = r.nextInt(listOfAll.size());
            string += listOfAll.get(randomNr) + " ";
        }

        JSonFileFormat format = new JSonFileFormat("Name", "random", "5-8", ("" +width), (""+height), string);
        Gson j = new Gson();
        String jsonFile = j.toJson(format);
        try {
            PrintWriter wr = new PrintWriter("src/test/assets/checkForRadnomeInput.json");
            wr.println(jsonFile);
            wr.close();
        }catch (FileNotFoundException e){
            e.fillInStackTrace();
        }


    }

    private void addOneOf(String in, String[] chooseOne, ArrayList<String> addTolist){
        for (int i = 0; i < chooseOne.length; i++) {
            addTolist.add(in + chooseOne[i]);
        }
    }
    @Test
    public void checkFloorFilledBoardisFloorFilled(){
        IBoardGenerator generator = new BoardGenerator();
        Board board;
        try {
            board = generator.getBoardFromFile("src/test/assets/emptyBoardtest.json");

            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    assertTrue(board.getTileTexture(x,y).first() instanceof FloorTile);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * For this test to work well you need to change the valuse in heightWithTest.json,
     * working on a fix making this happen automaticly
     */

    @Test
    public void heightAndWidthTest(){

        try {
            final int height = 232;
            final int width = 43;
            IBoardGenerator generator = new BoardGenerator();
            Board board = generator.getBoardFromFile("src/test/assets/heightWithTest.json");
            assertEquals(height, board.getHeight());
            assertEquals(width, board.getWidth());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Can be used to generate long string to be read by IBoardGenerator
     *
     * Prints out a string containing width*height number of "f "s
     *
     */

    //@Test remove "//" to get the string
    public void getStringForJSonFile(){
        final int height = 232;
        final int width = 43;
        String fs= "";
        for (int i = 0; i < height*width; i++) {
            fs += "f ";
        }
        System.out.println(fs);
    }
}
