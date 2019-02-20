package sky7.board;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import sky7.board.cellContents.Inactive.FloorTile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.IllegalFormatConversionException;
import java.util.TreeSet;

public class BoardGenerator implements IBoardGenerator {
    private static HashMap<String, String> json = new HashMap<>();
    private static HashMap<Character, Object> codes = new HashMap<>();

    public BoardGenerator() {
    }

    public static void main(String[] args) throws FileNotFoundException {
        getJson();
        String cells = json.get("grid");

        System.out.println(ICell.class);

    }


    @Override
    public Board getBoardFromFile(String filePath) throws FileNotFoundException {

        getJson();

        TreeSet<ICell>[][] grid = new TreeSet[Integer.parseInt(json.get("height"))][Integer.parseInt(json.get("width"))];
        String[] cells = json.get("grid").split(" ");
        int i = 0;
        for (int j = 0; j < cells.length; j++) {
            if (j % 12 == 0) {
                i++;
            }
            String cell = cells[j];
            char typeOfCell = cell.charAt(0);

            TreeSet<ICell> layers = new TreeSet<ICell>();
            switch (typeOfCell) { //TODO throw argument exception if the file is formatted incorrectly.
                case 'f':
                    layers.add(new FloorTile());
                    grid[i][j] = layers;
                case 'w': //TODO fill in wall (direction, can be multiple walls)
                    break;
                case 'b': //TODO fill in belt (direction and type)
                    break;
                case 'c': //TODO fill in cogwheel (direction with clock and against clock)
                    break;
                case 'g': //TODO fill in flag (number)
                    break;
                case 't': //TODO fill in wrench (2 types)
                    break;
                case 'h': //TODO fill in hole, essentially ignore
                    break;
                case 'l': //TODO fill in laser (start position, direction, number of lasers)
                    break;
                case 's': //TODO fill in start cell for robot (number)
                    break;
                    default: throw new IllegalArgumentException("There is a mistake in the format of the file");

            }
            grid[i][j] = layers;

        }


        //TODO parse the coded board to ICell


        Board board = new Board(grid);


        return null;
    }

    private static void getJson() throws FileNotFoundException {

        JsonReader boardReader = new JsonReader(new FileReader("assets/Boards/board2.json"));
        JsonReader boardFormatReader = new JsonReader(new FileReader("assets/Boards/boardFormat.json"));
        Gson j = new Gson();

        json = j.fromJson(boardReader, HashMap.class);
        codes = j.fromJson(boardFormatReader, HashMap.class);


        //System.out.println(json.get("name"));


    }
}
