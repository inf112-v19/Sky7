package sky7.board.cellContents;

public enum DIRECTION {
    NORTH,
    SOUTH,
    EAST,
    WEST;


    public DIRECTION inverse(DIRECTION dir){
        DIRECTION newDir;
        switch (dir){
            case NORTH: newDir = SOUTH; break;
            case WEST: newDir = EAST; break;
            case SOUTH: newDir = NORTH; break;
            case EAST: newDir =  WEST; break;
            default: throw new IllegalArgumentException("The direction you are trying to invers are not one of the standar four");
        }
         return newDir;
    }
}
