package sky7.board.cellContents.Inactive;

import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class FloorTile implements IInactive {
    String textureRef = "floor";
    int priority = 1;

    @Override
    public String getTexture() {
        return textureRef;
    }

    @Override
    public int drawPriority() {
        return this.priority;
    }

    @Override
    public int compareTo(ICell other) {
        return Integer.compare(this.drawPriority(), other.drawPriority());
    }

}
