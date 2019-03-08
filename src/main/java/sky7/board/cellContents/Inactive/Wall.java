package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IInactive;

public class Wall implements IInactive {
    private DIRECTION direction;
    private Texture texture;
    private final int PRIORITY = 4;

    public Wall(DIRECTION direction) {
        this.direction = direction;
    }

    @Override
    public Texture getTexture() {
        if(texture == null){
            switch (direction){
                case EAST: texture = new Texture("assets/wall/WallE.png"); break;

                case NORTH: texture = new Texture("assets/wall/WallN.png"); break;

                case WEST: texture = new Texture("assets/wall/WallW.png"); break;

                case SOUTH: texture = new Texture("assets/wall/WallS.png"); break;

                default: throw new IllegalStateException("The directoion arguement of the wall is not in a valid state");
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

    public DIRECTION getDirection() {
        return direction;
    }

    public boolean canGoHere(DIRECTION incomingDir) {
        return incomingDir.inverse(incomingDir) == direction;
    }
}