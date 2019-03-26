package sky7.board.cellContents.Active;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IActive;
import sky7.board.cellContents.DIRECTION;

public class Laser implements IActive {
    private final boolean start; // its a start og a laser
    private DIRECTION direction;
    private final int numberOfLasers;
    private Texture texture;
    private final static int PRIORITY = 4;

    public Laser(boolean start, DIRECTION direction, int numberOfLasers) {
        this.start = start;
        this.direction = direction;
        this.numberOfLasers = numberOfLasers;
    }

    @Override
    public Texture getTexture() {
        if(texture == null){
            if(start){
               texture = getStartLaserTexture();
            }else {
                texture = getLaserTexture();
            }
        }
        return texture;
    }

    /**
     * Start of this laser. Check in what direction it goes and how many lasers.
     * @return the texture of this start laser in right direction.
     */
    public Texture getStartLaserTexture() {
        switch (direction) {
            case NORTH:
                if (numberOfLasers == 1) {
                    texture = new Texture("assets/laser/LaserStartU.png");
                } else if (numberOfLasers == 2) {
                    texture = new Texture("assets/laser/LaserDStartU.png");
                } else {
                    throw new IllegalArgumentException("the number og laser has invalid value. Should be 1 or 2, was  " + numberOfLasers);
                }
                break;
            case SOUTH:
                if (numberOfLasers == 1) {
                    texture = new Texture("assets/laser/LaserStartD.png");
                }else if (numberOfLasers == 2) {
                    texture = new Texture("assets/laser/LaserDStartD.png");
                }else {
                    throw new IllegalArgumentException("the number og laser has invalid value. Should be 1 or 2, was  " + numberOfLasers);
                }
                break;
            case WEST:
                if (numberOfLasers == 1) {
                    texture = new Texture("assets/laser/LaserStartR.png");
                } else if (numberOfLasers == 2) {
                    texture = new Texture("assets/laser/LaserDStartR.png");
                } else {
                    throw new IllegalArgumentException("the number og laser has invalid value. Should be 1 or 2, was  " + numberOfLasers);
                }
                break;
            case EAST:
                if (numberOfLasers == 1) {
                    texture = new Texture("assets/laser/LaserStartL.png");
                }else if (numberOfLasers == 2) {
                    texture = new Texture("assets/laser/LaserDStartL.png");
                }else {
                    throw new IllegalArgumentException("the number og laser has invalid value. Should be 1 or 2, was  " + numberOfLasers);
                }
                break;
            default:
                throw new IllegalStateException("The directoion arguement of the laser is not in a valid state");
        }
        return texture;
    }

    /**
     * check in what direction this laser goes and if it has  one or two lasers.
     * @return the texture of this laser in its direction and correct number of lasers.
     */
        public Texture getLaserTexture() {
            if( direction == DIRECTION.SOUTH || direction == DIRECTION.NORTH){
                if(numberOfLasers == 1){
                    texture = new Texture("assets/laser/LaserVert.png");
                }else if (numberOfLasers == 2){
                    texture = new Texture("assets/laser/LaserDVert.png");
                }else {
                    throw  new IllegalArgumentException("The number og laser has invalid value. Should be 1 or 2, was  " + numberOfLasers);
                }
            }else if ( direction == DIRECTION.EAST || direction == DIRECTION.WEST) {
                if(numberOfLasers == 1){
                    texture = new Texture("assets/laser/LaserHor.png");
                }else if(numberOfLasers == 2) {
                    texture = new Texture("assets/laser/LaserDHor.png");
                }else {
                    throw  new IllegalArgumentException("The number og laser has invalid value. Should be 1 or 2, was  " + numberOfLasers);
                }
            }else {
                throw  new IllegalArgumentException("The number og laser has invalid value. Should be 1 or 2, was  " + numberOfLasers);
            }
            return texture;
        }

    @Override
    public int drawPriority() { return PRIORITY; }

    @Override
    public int compareTo(ICell other) { return Integer.compare(this.drawPriority(), other.drawPriority()); }

    public DIRECTION getDirection(){ return direction; }
}
