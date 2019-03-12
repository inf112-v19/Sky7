package sky7.board.cellContents.Active;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IActive;

public class CogWheel implements IActive {

    private int rotDirection; // -1 counterclockwise, 1 clockwise, 2 180
    private static final int PRIORITY = 3;
    private Texture texture;

    public CogWheel(int rotDirection) {
        this.rotDirection = rotDirection;
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            if(rotDirection == -1) {
                texture = new Texture("assets/cogwheel/CogRed.png");
            } else if (rotDirection == 1){
                texture = new Texture("assets/cogwheel/CogGreen.png");
            } else {
                throw new IllegalStateException("The rotaion Direction has invalid value, has to be -1, 1, or 2. Was " + rotDirection);
            }
        }
        return texture;
    }

    @Override
    public int drawPriority() {
        return this.PRIORITY;
    }

    @Override
    public int compareTo(ICell o) {
        return 0;
    }
    
    public int getRotation() {
        return this.rotDirection;
    }
}
