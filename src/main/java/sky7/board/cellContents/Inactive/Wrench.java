package sky7.board.cellContents.Inactive;

import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Wrench implements IInactive {
    private int type;
    private int priority;

    public Wrench(int type) {

        this.type = type;
    }

    @Override
    public String getTexture() {
        return null;
    }

    @Override
    public int drawPriority() {
        return priority;
    }

    @Override
    public int compareTo(ICell o) {
        return 0;
    }
}
