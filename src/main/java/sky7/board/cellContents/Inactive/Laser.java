package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Laser implements IInactive {
    private final boolean start;
    private final int direction;
    private final int numberOfLasers;
    private int priority;

    public Laser(boolean start, int direction, int numberOfLasers) {

        this.start = start;
        this.direction = direction;
        this.numberOfLasers = numberOfLasers;
    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public int drawPriority() {
        return 0;
    }

    @Override
    public int compareTo(ICell o) {
        return 0;
    }
}
