package sky7.board.cellContents;

import com.badlogic.gdx.math.Vector2;

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
                throw new IllegalArgumentException("The direction you are trying to inters are not one of the standard four");
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
                throw new IllegalArgumentException("The direction you are trying to get symbol from are not one of the standard four");
        }

    }

    /**
     * Given x and y returns the next (x,y) after moving in dir.
     *
     * @param x
     * @param y
     * @param dir
     * @return int[x, y]
     */

    // TODO: checking if can use game.getDestination() instead.
    /*public static Vector2 getNewPosMoveDir(int x, int y, DIRECTION dir){
        int newX = x;
        int newY = y;

        switch (dir){
            case NORTH: newY++; break;
            case SOUTH: newY--; break;
            case EAST: newX++; break;
            case WEST: newX--; break;
        }

        return new Vector2(newX, newY);
    }*/


    /**
     * Returns the rotation needed to go form THIS to the given direction.
     * @param dir
     * @return rotation needed to reach new direction.
     */
    public int directionToRotation(DIRECTION dir){
        if(this == dir){
            return 0;
        }
        switch (this){
            case NORTH:
                switch (dir){
                    case EAST: return 1;
                    case SOUTH: return 2;
                    case WEST: return -1;
                }
                break;
            case EAST: switch (dir){
                case SOUTH: return 1;
                case WEST: return 2;
                case NORTH: return -1;
            }
                break;
            case SOUTH: switch (dir){
                case WEST: return 1;
                case NORTH: return 2;
                case EAST: return -1;
            }
                break;
            case WEST: switch (dir){
                case NORTH: return 1;
                case EAST: return 2;
                case SOUTH: return -1;
            }
                break;
        }

        return 0;
    }
}
