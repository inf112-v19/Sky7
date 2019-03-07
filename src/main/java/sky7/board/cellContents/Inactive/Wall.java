package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IInactive;

public class Wall implements IInactive {
    private DIRECTION direction;
    private Texture texture;
    private int priority = 3;

    public Wall(DIRECTION direction) {
        this.direction = direction;
        switch (direction) {
            case NORTH:
                texture = new Texture("wall/LaserStopT.png"); //TODO add wall images.
            case EAST:
                texture = new Texture("wall/LaserStopR.png"); //TODO add wall images.
            case SOUTH:
                texture = new Texture("wall/LaserStopB.png"); //TODO add wall images.
            case WEST:
                texture = new Texture("wall/LaserStopL.png"); //TODO add wall images.

        }

    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public int drawPriority() {
        return priority;
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