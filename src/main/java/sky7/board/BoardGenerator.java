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
import sky7.board.cellContents.Inactive.Flag;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Inactive.Hole;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.Inactive.StartPosition;
import sky7.board.cellContents.Inactive.Wall;

/**
 * A Class for generating a board object parsed from a json file.
 */
public class BoardGenerator implements IBoardGenerator {
    private static HashMap<String, String> json = new HashMap<>();
    private static HashMap<String, Supplier<ICell>> ICellFactory = new HashMap<>();

    public BoardGenerator() {
        fillSuppliers();
    }

    /**
     *
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

    }

    @Override
    public Board getBoardFromFile(String filePath) throws FileNotFoundException {

        getJson(filePath);
        int height = Integer.parseInt(json.get("height"));
        int width = Integer.parseInt(json.get("width"));
        TreeSet<ICell>[][] grid = new TreeSet[width][height];
        String[] treeSet = json.get("grid").split(" ");

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
            grid[pos % width][pos / width] = tree;
            pos++;
        }

        return new Board(grid, height, width);
    }

    /**
     * Converts the json file to a HashMap data structure.
     * @param filePath a relative or absolute path to a json file
     * @throws FileNotFoundException if the json file is not found.
     */
    private static void getJson(String filePath) throws FileNotFoundException {
        JsonReader boardReader = new JsonReader(new FileReader(filePath));
        Gson j = new Gson();
        json = j.fromJson(boardReader, HashMap.class);

    }
}
