package sky7.board.cellContents.Active;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IActive;

import java.util.HashSet;



public class Pusher implements IActive {
    private final DIRECTION direction;//what way it faces
    private final HashSet whenActive; // the pusher activates
    private Texture texture;
    private final int PRIORITY =3;

    public Pusher(DIRECTION direction, HashSet whenActive ){
        this.direction = direction;
        this.whenActive = whenActive;
    }

    boolean doActivate(int fase){
        return whenActive.contains(fase);
    }

    @Override
    public Texture getTexture() {//TODO endre bilde 8.png da dette er likt som 1.png
        if(texture == null){
            if(whenActive.contains(2) && whenActive.contains(4)){
                switch (direction){
                    case NORTH: texture = new Texture("assets/pusher/3.png"); break;
                    case SOUTH: texture = new Texture("assets/pusher/1.png"); break;
                    case EAST: texture = new Texture("assets/pusher/4.png"); break;
                    case WEST: texture = new Texture("assets/pusher/2.png"); break;
                }
            }else {
                switch (direction){
                    case NORTH: texture = new Texture("assets/pusher/8.png"); break;
                    case SOUTH: texture = new Texture("assets/pusher/6.png"); break;
                    case EAST: texture = new Texture("assets/pusher/9.png"); break;
                    case WEST: texture = new Texture("assets/pusher/7.png"); break;
                    default: throw new IllegalStateException("The directoion arguement of the pusher is not in a valid state");
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

    public DIRECTION getDirection(){
        return direction;
    }
}
