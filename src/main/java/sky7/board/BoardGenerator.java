package sky7.board;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import sky7.board.cellContents.Active.Belt;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.Inactive.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

public class BoardGenerator implements IBoardGenerator {
    private static HashMap<String, String> json = new HashMap<>();
    private static HashMap<Character, Object> codes = new HashMap<>();
    private static Board board;


    public BoardGenerator() {
    }

    public static void main(String[] args) throws FileNotFoundException {
        BoardGenerator bg = new BoardGenerator();
        board = bg.getBoardFromFile("assets/Boards/emptyBoard.json");
        System.out.println(board);

    }


    @Override
    public Board getBoardFromFile(String filePath) throws FileNotFoundException {

        getJson(filePath);

        int height = Integer.parseInt(json.get("height"));
        int width = Integer.parseInt(json.get("width"));
        TreeSet<ICell>[][] grid = new TreeSet[height][width];
        String[] cells = json.get("grid").split(" ");

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                String cell = cells[height*row+column];
                //TODO split cell in to partial codes
                String[] partsOfCell = cell.split("_");
                for (String part : partsOfCell) {

                    char typeOfCell = part.charAt(0);

                    TreeSet<ICell> layers = new TreeSet<ICell>();

                    switch (typeOfCell) { //TODO throw argument exception if the file is formatted incorrectly.
                        case 'f':
                            layers.add(new FloorTile());
                            break;
                        case 'w': //TODO fill in wall (direction, can be multiple walls)
                            for (int k = 1; k < part.length(); k++) {
                                char direction = part.charAt(k);
                                switch (direction) {
                                    case 'N': //TODO north
                                        layers.add(new Wall(1));
                                        break;
                                    case 'S': //TODO south
                                        layers.add(new Wall(2));
                                        break;
                                    case 'E': // TODO east
                                        layers.add(new Wall(3));
                                        break;
                                    case 'W': // TODO west
                                        layers.add(new Wall(4));
                                        break;
                                }
                            }
                            break;
                        case 'b': //TODO fill in belt (direction and type)
                            for (int k = 1; k < part.length(); k++) {
                                char direction = part.charAt(k);
                                switch (direction) {
                                    case 'N': //TODO north
                                        layers.add(new Belt(1, 0));
                                        break;
                                    case 'S': //TODO south
                                        layers.add(new Belt(2, 0));
                                        break;
                                    case 'E': // TODO east
                                        layers.add(new Belt(3, 0));
                                        break;
                                    case 'W': // TODO west
                                        layers.add(new Belt(4, 0));
                                        break;
                                }
                            }
                            break;
                        case 'c': //TODO fill in cogwheel (direction with clock and against clock)
                            for (int k = 1; k < part.length(); k++) {
                                char direction = part.charAt(k);
                                switch (direction) {
                                    case 'C': //TODO with clock
                                        layers.add(new CogWheel(1));
                                        break;
                                    case 'A': //TODO against clock
                                        layers.add(new CogWheel(2));
                                        break;
                                }
                            }
                            break;
                        case 'g': //fill in flag (number)
                            int flagNumber = Integer.parseInt(""+part.charAt(1));
                            layers.add(new Flag(flagNumber));
                            break;
                        case 't': // fill in wrench (2 types)
                            int typeOfWrench = Integer.parseInt(""+part.charAt(1));
                            layers.add(new Wrench(typeOfWrench));
                            break;
                        case 'h': // fill in hole, essentially ignore
                            layers.add(new Hole());
                            break;
                        case 'l': //TODO fill in laser (start position, direction, number of lasers)
                            layers.add(new Laser(false, 0, 1));
                            break;
                        case 's': //TODO fill in start cell for robot (number)
                            int startNumber = Integer.parseInt(""+part.charAt(1));
                            layers.add(new StartPosition(startNumber));
                            break;
                        default:
                            throw new IllegalArgumentException("There is a mistake in the format of the file");

                    }
                    grid[row][column] = layers;

                }
            }
        }


        return new Board(grid,height,width);
    }

    private static void getJson(String filePath) throws FileNotFoundException {

        JsonReader boardReader = new JsonReader(new FileReader(filePath));
        JsonReader boardFormatReader = new JsonReader(new FileReader("assets/Boards/boardFormat.json"));
        Gson j = new Gson();

        json = j.fromJson(boardReader, HashMap.class);
        codes = j.fromJson(boardFormatReader, HashMap.class);


        //System.out.println(json.get("name"));


    }
}
