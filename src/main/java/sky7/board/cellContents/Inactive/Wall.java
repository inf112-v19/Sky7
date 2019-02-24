package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Wall implements IInactive {
    private int direction;
    private int priority;
    private Texture texture;

    public Wall(int direction) {
        this.direction = direction;
        texture = new Texture(""); //TODO add wall images.

    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public int drawPriority() {
        return this.priority;
    }

    @Override
    public int compareTo(ICell o) {
        return 0;
    }
}
