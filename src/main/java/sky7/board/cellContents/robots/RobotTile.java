package sky7.board.cellContents.robots;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IMoving;

public class RobotTile implements IMoving {
    String textureRef = "robot";
    Texture texture = new Texture("robot1.png");
    int priority = 5;
    int playerNr;
    
    public RobotTile(int playerNr) {
        this.playerNr = playerNr;
    }

    @Override
    public Texture getTexture() {
        return texture;
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
