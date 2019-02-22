package sky7.board.cellContents.Active;

import sky7.board.ICell;
import sky7.board.cellContents.IActive;

public class CogWheel implements IActive {

    private int rotDirection;
    private int priority;
    public CogWheel(int rotDirection) {
        this.rotDirection = rotDirection;
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
