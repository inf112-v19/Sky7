package sky7.board.cellContents.Active;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IActive;

import java.util.HashSet;


public class Pusher implements IActive {
    private final DIRECTION direction;
    private final HashSet whenActive; // when the pusher activates
    private Texture texture;
    private final int PRIORITY =4;

    public Pusher(DIRECTION direction, HashSet whenActive ){
        this.direction = direction;
        this.whenActive = whenActive;
    }

    /**
     * Check if the pusher activates in the current phase.
     * @param phase the current phase
     * @return true if the pusher activates in phase, false otherwise.
     */
    boolean doActivate(int phase){
        return whenActive.contains(phase);
    }

    @Override
    public Texture getTexture() {//TODO change image 8.png , this is the same as 1.png
        if(texture == null){
            if(whenActive.contains(2) && whenActive.contains(4)){
                switch (direction){
                    case NORTH: texture = new Texture("assets/pusher/3.png"); break;
                    case SOUTH: texture = new Texture("assets/pusher/1.png"); break;
                    case EAST: texture = new Texture("assets/pusher/4.png"); break;
                    case WEST: texture = new Texture("assets/pusher/2.png"); break;
                    default: throw new IllegalStateException("The direction argument of the pusher is not in a valid state");

                }
            }else {
                switch (direction){
                    case NORTH: texture = new Texture("assets/pusher/8.png"); break;
                    case SOUTH: texture = new Texture("assets/pusher/6.png"); break;
                    case EAST: texture = new Texture("assets/pusher/9.png"); break;
                    case WEST: texture = new Texture("assets/pusher/7.png"); break;
                    default: throw new IllegalStateException("The direction argument of the pusher is not in a valid state");
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
    public int compareTo(ICell other) { return Integer.compare(this.drawPriority(), other.drawPriority()); }

    /**
     * return the direction of this pusher
     * @return
     */
    public DIRECTION getDirection(){
        return direction;
    }
}
