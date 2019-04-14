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
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Active.Belt;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.Inactive.Flag;
import sky7.board.cellContents.Inactive.FloorTile;
import sky7.board.cellContents.Inactive.Hole;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.Inactive.StartPosition;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.Inactive.Wrench;

public class BoardGenerator implements IBoardGenerator {
    private static HashMap<String, String> json = new HashMap<>();
    private static HashMap<Character, Object> codes = new HashMap<>();
    private static HashMap<String, Supplier<ICell>> ICellFactory = new HashMap<>();
    private static Board board;


    public BoardGenerator() {
        fillSuppliers();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BoardGenerator bg = new BoardGenerator();
        board = bg.getBoardFromFile("assets/Boards/emptyBoard.json");
        System.out.println(board);

    }

    private void addToFactory(String syntaks, Supplier<ICell> supplier) {
        ICellFactory.put(syntaks, supplier);
        AbstractMap.SimpleEntry<Integer,String> entry = new AbstractMap.SimpleEntry<>(1,"df");
    }

    private static ICell createCell(String syntaks) {
        // alternative/advanced method
        Supplier<ICell> factory = ICellFactory.get(syntaks);
        if (factory != null) {
            return factory.get();
        } else {
            System.err.println("createItem: Don't know how to create a '" + syntaks + "'");
            return null;
        }
    }

    private void fillSuppliers() {

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Flag.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : FloorTile.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Hole.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : CogWheel.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Wall.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Belt.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Laser.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : StartPosition.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
        }

        for (AbstractMap.SimpleEntry<String, Supplier<ICell>> pair : Pusher.getSuppliers()) {
            addToFactory(pair.getKey(), pair.getValue());
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
                    throw new IllegalArgumentException("There is a mistake in the format of the file: " + cell );
                } else {
                    tree.add(createdCell);
                }
            }
            grid[pos%width][pos/width] = tree;
            pos++;
        }

        return new Board(grid, height, width);
    }

    private static void getJson(String filePath) throws FileNotFoundException {

        JsonReader boardReader = new JsonReader(new FileReader(filePath));
        JsonReader boardFormatReader = new JsonReader(new FileReader("assets/Boards/boardFormat.json"));
        Gson j = new Gson();

        json = j.fromJson(boardReader, HashMap.class);
        //codes = j.fromJson(boardFormatReader, HashMap.class);
        
        //System.out.println(json.get("name"));


    }
}
