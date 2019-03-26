package sky7.board.cellContents.Active;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IActive;
import sky7.board.cellContents.DIRECTION;

public class Laser implements IActive {
    private final boolean start;
    private DIRECTION direction;
    //private final int direction; //
    private final int numberOfLasers;
    private Texture texture;
    private final static int PRIORITY = 3;

    public Laser(boolean start, DIRECTION direction, int numberOfLasers) {

        this.start = start;
        this.direction = direction;
        this.numberOfLasers = numberOfLasers;
    }

    @Override
    public Texture getTexture() {
        if(texture == null){
            if(start){
                switch (direction) {
                    case NORTH:
                        if (numberOfLasers == 1) {
                            texture = new Texture("assets/laser/LaserStartU.png");
                        } else {
                            texture = new Texture("assets/laser/LaserDStartU.png");
                        }break;
                    case SOUTH:
                        if (numberOfLasers == 1) {
                            texture = new Texture("assets/laser/LaserStartD.png");
                        } else {
                            texture = new Texture("assets/laser/LaserDStartD.png");
                        }break;
                    case WEST:
                        if (numberOfLasers == 1) {
                            texture = new Texture("assets/laser/LaserStartR.png");
                        } else {
                            texture = new Texture("assets/laser/LaserDStartR.png");
                        }break;
                    case EAST:
                        if (numberOfLasers == 1) {
                            texture = new Texture("assets/laser/LaserStartL.png");
                        } else {
                            texture = new Texture("assets/laser/LaserDStartL.png");
                        }break;
                }


            }else {
                if( direction == DIRECTION.SOUTH || direction == DIRECTION.NORTH){
                    if(numberOfLasers == 1){
                        texture = new Texture("assets/laser/LaserVert.png");
                    }else {
                        texture = new Texture("assets/laser/LaserDVert.png");
                    }
                }else {
                    if(numberOfLasers == 1){
                        texture = new Texture("assets/laser/LaserHor.png");
                    }else {
                        texture = new Texture("assets/laser/LaserDHor.png");
                    }
                }

            }
        }

        return texture;
    }

    @Override
    public int drawPriority() {
        return PRIORITY;
    }

    @Override
    public int compareTo(ICell other) {

        return Integer.compare(this.drawPriority(), other.drawPriority());
    }
}
