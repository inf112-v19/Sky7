package sky7.board.cellContents.Inactive;

import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class StartPosition implements IInactive{
    private int number;
    private int priority;

    public StartPosition(int number){

        this.number = number;
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
