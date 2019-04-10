package sky7.board.cellContents;

public enum DIRECTION {
    NORTH(0,1),
    SOUTH(0,-1),
    EAST(1,0),
    WEST(0,1);
    private int x;
    private int y;

    DIRECTION(int x,int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public DIRECTION reverse() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case WEST:
                return EAST;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            default:
                throw new IllegalArgumentException("The direction you are trying to invers are not one of the standar four");
        }
    }

    public char symbol() {
        switch (this) {
            case NORTH:
                return 'N';
            case WEST:
                return 'W';
            case SOUTH:
                return 'S';
            case EAST:
                return 'E';
            default:
                throw new IllegalArgumentException("The direction you are trying to invers are not one of the standar four");
        }

    }
}
