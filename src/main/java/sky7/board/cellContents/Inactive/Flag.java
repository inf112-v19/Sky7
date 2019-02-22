package sky7.board.cellContents.Inactive;

import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Flag implements IInactive{
    private  int flagNumber;
    private int priority;

    public Flag(int flagNumber){
        this.flagNumber = flagNumber;
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
