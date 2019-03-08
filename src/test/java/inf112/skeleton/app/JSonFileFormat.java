package inf112.skeleton.app;

public class JSonFileFormat {
    private String name;
    private String length;
    private String players;
    private String width;
    private String height;
    private String grid;






    public JSonFileFormat(String name, String length, String players, String width, String height, String grid){
        this.name = name;
        this.length = length;
        this.players = players;
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    public String getLength() {
        return length;
    }

    public String getName() {
        return name;
    }
    public String getGrid() {
        return grid;
    }

    public String getHeight() {
        return height;
    }

    public String getPlayers() {
        return players;
    }

    public String getWidth() {
        return width;
    }
}
