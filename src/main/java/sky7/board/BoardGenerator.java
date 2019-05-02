package sky7.board;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.function.Supplier;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import sky7.board.cellContents.Active.Pusher;
import sky7.board.cellContents.Active.Belt;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.*;
import sky7.board.cellContents.Active.Laser;

/**
 * A Class for generating a board object parsed from a json file.
 */
public class BoardGenerator implements IBoardGenerator {
    private static HashMap<String, Supplier<ICell>> ICellFactory = new HashMap<>();

    public BoardGenerator() {
        fillSuppliers();
    }

    /**
     * @param syntax the string read from the json file.
     * @return a object with the specified values in the syntax
     */
    private static ICell createCell(String syntax) {
        Supplier<ICell> factory = ICellFactory.get(syntax);
        if (factory != null) {
            return factory.get();
        } else {
            System.err.println("createItem: Don't know how to create a '" + syntax + "'");
            return null;
        }
    }

    /**
     * Fills a Factory with supplier of all type of ICells.
     * A supplier returns a list of all the different permutation of an object.
     */
    private void fillSuppliers() {

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Flag.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : FloorTile.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Hole.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : CogWheel.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Wall.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Belt.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Laser.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : StartPosition.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Pusher.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }
        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Wrench.getSuppliers()) {
            ICellFactory.put(pair.getKey(), pair.getValue());
        }

    }

    @Override
    public Board getBoardFromFile(String jsonFilePath) throws FileNotFoundException {

        HashMap<String, String> startBoard = getJson("assets/Boards/startBoard2.json");
        TreeSet<ICell>[][] startGrid = fillGrid(startBoard);

        HashMap<String, String> mainBoard = getJson(jsonFilePath);
        TreeSet<ICell>[][] mainGrid = fillGrid(mainBoard);

        //mainGrid = rotate(mainGrid, 1);
        TreeSet<ICell>[][] finalGrid = combineGrids(startGrid, mainGrid, DIRECTION.SOUTH);
        finalGrid = rotate(finalGrid, 1);

        return new Board(finalGrid, finalGrid.length, finalGrid[0].length);
    }


    /**
     * Appends the start grid to the {@param dir} of the grid.
     *
     * @param startGrid
     * @param grid
     * @param dir
     * @return
     */
    private TreeSet<ICell>[][] combineGrids(TreeSet<ICell>[][] startGrid, TreeSet<ICell>[][] grid, DIRECTION dir) {
        TreeSet<ICell>[][] finalGrid;
        switch (dir) {
            case WEST:
                TreeSet<ICell>[][] rotatedWithClock = rotate(startGrid, 1);
                finalGrid = new TreeSet[grid.length][grid[0].length + rotatedWithClock[0].length];
                for (int i = 0; i < rotatedWithClock.length; i++) {
                    for (int j = 0; j < rotatedWithClock[0].length; j++) {
                        finalGrid[i][j] = rotatedWithClock[i][j];
                    }
                }
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[0].length; j++) {
                        finalGrid[i][j + rotatedWithClock[0].length] = grid[i][j];
                    }
                }
                break;
            case NORTH:
                finalGrid = new TreeSet[grid.length + startGrid.length][grid[0].length];
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[0].length; j++) {
                        finalGrid[i][j] = grid[i][j];
                    }
                }
                for (int i = 0; i < startGrid.length; i++) {
                    for (int j = 0; j < startGrid[0].length; j++) {
                        finalGrid[i + grid.length][j] = startGrid[i][j];
                    }
                }
                break;
            case EAST:
                TreeSet<ICell>[][] rotatedAgainstClock = rotate(startGrid, 3);
                finalGrid = new TreeSet[grid.length][grid[0].length + rotatedAgainstClock[0].length];
                for (int i = 0; i < rotatedAgainstClock.length; i++) {
                    for (int j = 0; j < rotatedAgainstClock[0].length; j++) {
                        finalGrid[i][j + grid[0].length] = rotatedAgainstClock[i][j];
                    }
                }
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[0].length; j++) {
                        finalGrid[i][j] = grid[i][j];
                    }
                }
                break;
            default:
                finalGrid = new TreeSet[grid.length + startGrid.length][grid[0].length];
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[0].length; j++) {
                        finalGrid[i][j] = grid[i][j];
                    }
                }
                for (int i = 0; i < startGrid.length; i++) {
                    for (int j = 0; j < startGrid[0].length; j++) {
                        finalGrid[i + grid.length][j] = startGrid[i][j];
                    }
                }
                break;
        }

        return finalGrid;
    }

    /**
     * Rotate a grid 90,180 or 270 degrees clockwise
     *
     * @param grid                       an array of arrays.
     * @param numberOfRotationsClockWise 1 for 90 deg with clock, 2 for 180 deg, 3 for 270 deg
     * @return
     */
    private TreeSet<ICell>[][] rotate(TreeSet<ICell>[][] grid, int numberOfRotationsClockWise) {
        TreeSet<ICell>[][] rotated;
        switch (numberOfRotationsClockWise) {
            case 1:
                rotated = new TreeSet[grid[0].length][grid.length];
                for (int width = 0; width < grid[0].length; width++) {
                    for (int height = 0; height < grid.length; height++) {
                        rotated[width][height] = grid[grid.length - height - 1][width];
                    }
                }
                return rotated;
            case 2:
                rotated = new TreeSet[grid.length][grid[0].length];
                for (int height = 0; height < grid.length; height++) {
                    for (int width = 0; width < grid[0].length; width++) {
                        rotated[height][width] = grid[grid.length - 1 - height][grid[0].length - 1 - width];
                    }
                }
                return rotated;
            case 3:
                rotated = new TreeSet[grid[0].length][grid.length];
                for (int width = 0; width < grid[0].length; width++) {
                    for (int height = 0; height < grid.length; height++) {
                        rotated[width][height] = grid[height][grid[0].length - 1 - width];
                    }

                }
                return rotated;
            default:
                return grid;
        }
    }

    /**
     * Fill the
     *
     * @param bJson
     */
    private TreeSet<ICell>[][] fillGrid(HashMap<String, String> bJson) {
        int height = Integer.parseInt(bJson.get("height"));
        int width = Integer.parseInt(bJson.get("width"));

        TreeSet<ICell>[][] grid = new TreeSet[height][width];
        String[] treeSet = bJson.get("grid").split(" ");

        int pos = 0;
        for (String layer : treeSet) {
            String[] cells = layer.split("_");
            TreeSet<ICell> tree = new TreeSet<>();

            for (String cell : cells) {
                ICell createdCell = createCell(cell);
                if (createdCell == null) {
                    throw new IllegalArgumentException("There is a mistake in the format of the file: " + cell);
                } else {
                    tree.add(createdCell);
                }
            }
            grid[pos / width][pos % width] = tree;
            pos++;
        }
        return grid;
    }

    /**
     * Converts the json file to a HashMap data structure.
     *
     * @param filePath a relative or absolute path to a json file
     * @throws FileNotFoundException if the json file is not found.
     */
    private static HashMap<String, String> getJson(String filePath) throws FileNotFoundException {
        JsonReader boardReader = new JsonReader(new FileReader(filePath));
        Gson j = new Gson();
        return j.fromJson(boardReader, HashMap.class);
    }
}
