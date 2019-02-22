package sky7.board.cellContents.Inactive;

import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Wall implements IInactive {
    private int direction;
    private int priority;

    public Wall(int direction) { //TODO trenger vi få inn type?
        this.direction = direction;

    }

    @Override
    public String getTexture() {
        return null;
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
