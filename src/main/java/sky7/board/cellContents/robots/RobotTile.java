package sky7.board.cellContents.robots;

import sky7.board.ICell;
import sky7.board.cellContents.IMoving;

public class RobotTile implements IMoving {
    String textureRef = "robot";
    int priority = 5;
    
    @Override
    public String getTexture() {
        return textureRef;
    }

    @Override
    public int drawPriority() {
        return priority;
    }
    
    @Override
    public int compareTo(ICell other) {
        return Integer.compare(this.drawPriority(), other.drawPriority());
    }

}
