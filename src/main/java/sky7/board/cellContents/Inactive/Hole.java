package sky7.board.cellContents.Inactive;

import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Hole implements IInactive{
    private int priority;

    public Hole(){

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
