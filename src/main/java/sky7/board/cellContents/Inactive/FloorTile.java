package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class FloorTile implements IInactive {

    String textureRef = "floor";
    private Texture texture;
    private static final int PRIORITY = 1;

    public FloorTile(){
        
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            texture = new Texture("assets/floor/plain.png");
        }
        
        return texture;
    }

    @Override
    public int drawPriority() {
        return this.PRIORITY;
    }

    @Override
    public int compareTo(ICell other) {
        return Integer.compare(this.drawPriority(), other.drawPriority());
    }

}
