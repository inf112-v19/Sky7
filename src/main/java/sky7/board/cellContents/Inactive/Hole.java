package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Hole implements IInactive{
    private int priority;
    private Texture texture;

    public Hole(){
        texture = new Texture("floor/hole.png");
    }
    @Override
    public Texture getTexture() {
        return texture;
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
