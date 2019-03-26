package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Hole implements IInactive{
    private static final int PRIORITY = 1;
    private Texture texture;

    public Hole(){

    }
    @Override
    public Texture getTexture() {
        if (texture == null) {
            texture = new Texture("assets/floor/hole.png");
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
