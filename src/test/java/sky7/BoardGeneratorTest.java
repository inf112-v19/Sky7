package sky7;

import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
//import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverAnonymous;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

public class BoardGeneratorTest {
    private Random r = new Random();
    @Rule
    public ExpectedException thrown= ExpectedException.none();


    /**
     * Generates a random board with one element in each slot,
     * and checks if the boardgenerator can read it
     *
     * First it generates valid baordGenerator values,
     * then it writes a random amount of it to a json file
     *
     * Then a IBoardGenerator tries to read it
     */
    @Test //TODO: check that the test only check things that are implemented
    public void checkThatRandomValidBoardCanBeRead(){
        try {
            generateLongStringWithRandomObjects();
            IBoardGenerator generator = new BoardGenerator();
            Board board = generator.getBoardFromFile("src/test/assets/checkForRandomInput.json");
            assertTrue(true); // if it gets here everthing okey
        }catch (FileNotFoundException e) {
            fail("didn't pass");
            e.fillInStackTrace();
        }
    }


    @Test
    public void invalidBoardStateShouldCrashCastError(){
        ArrayList<String> validInput = getListOfValidCombinations();
        String[] allChars= {"b","c","f","g","h","l","r","s","t","w","N","E","W","S","C","A"};

        int randomNum = r.nextInt(500) + 3000;
        BoardGenerator bg = new BoardGenerator();
        for (int i = 0; i < randomNum; i++) {

            int firstPos = r.nextInt(allChars.length);
            int secondPos = r.nextInt(allChars.length);

            while (validInput.contains(allChars[firstPos] + allChars[secondPos])) {
                firstPos = r.nextInt(allChars.length);
                secondPos = r.nextInt(allChars.length);
            }


            JSonFileFormat format = new JSonFileFormat("Name", "random", "5-8", ("1"), ("1"), (allChars[firstPos] + allChars[secondPos]));
            Gson j = new Gson();
            String jsonFile = j.toJson(format);
            try {
                PrintWriter wr = new PrintWriter("src/test/assets/checkInvalidInputTest.json");
                wr.println(jsonFile);
                wr.close();
                try{
                    bg.getBoardFromFile("src/test/assets/checkInvalidInputTest.json");
                    fail(allChars[firstPos] + allChars[secondPos] + " passed the generator, it should not");
                } catch (IllegalArgumentException e){
                }
            }catch (FileNotFoundException e){
                e.fillInStackTrace();
            }
        }





    }

    private ArrayList<String> getListOfValidCombinations(){
        ArrayList<String> validInput = new ArrayList<>();
        String[] direction = {"N", "S", "E", "W"};
        String[] clockWise = {"C", "A"};
        String[] flagNumbers = {"1", "2", "3", "4"};
        String[] nrOfLasers = {"1", "2"};
        String[] playerNr = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] startPlace = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] wrenchtool = {"1","2"};

        validInput.add("f");
        addOneOf("w", direction, validInput);
        addOneOf("b", direction, validInput);
        addOneOf("c", clockWise, validInput);
        addOneOf("g", flagNumbers, validInput);
        addOneOf("t", wrenchtool, validInput);
        validInput.add("h");

        addOneOf("s", startPlace, validInput);
        return  validInput;
    }


    private void generateLongStringWithRandomObjects(){
        ArrayList<String> listOfAll = getListOfValidCombinations();

        int height = r.nextInt(500) + 550;
        int width = r.nextInt(500) + 550;

        StringBuilder string =  new StringBuilder();
        for (int i = 0; i < height*width; i++) {
            int randomNr = r.nextInt(listOfAll.size());
            string.append(listOfAll.get(randomNr) + " ");
        }

        JSonFileFormat format = new JSonFileFormat("Name", "random", "5-8", ("" +width), (""+height), string.toString());
        Gson j = new Gson();
        String jsonFile = j.toJson(format);
        try {
            PrintWriter wr = new PrintWriter("src/test/assets/checkForRandomInput.json");
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
