package sky7.board.cellContents;

public enum DIRECTION {
    NORTH(0,1),
    SOUTH(0,-1),
    EAST(1,0),
    WEST(-1,0);
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

    /**
     * reverse the direction.
     *
     * @return the reversed direction.
     */
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
                throw new IllegalArgumentException("The direction you are trying to inverse are not one of the standard four");
        }
    }

    /**
     * the character of this direction.
     *
     * @return a character representing this direction
     */
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
                throw new IllegalArgumentException("The direction you are trying to inverse are not one of the standard four");
        }

    }
}
