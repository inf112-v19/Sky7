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
                    case NORTH: texture = new Texture("assets/laser/3.png"); break;
                    case SOUTH: texture = new Texture("assets/laser/1.png"); break;
                    case EAST: texture = new Texture("assets/laser/4.png"); break;
                    case WEST: texture = new Texture("assets/laser/2.png"); break;
                }
            }else {
                switch (direction){
                    case NORTH: texture = new Texture("assets/laser/8.png"); break;
                    case SOUTH: texture = new Texture("assets/laser/6.png"); break;
                    case EAST: texture = new Texture("assets/laser/9.png"); break;
                    case WEST: texture = new Texture("assets/laser/7.png"); break;
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
}
