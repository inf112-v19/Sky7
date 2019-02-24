package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class StartPosition implements IInactive{
    private int number;
    private int priority;
    private Texture texture;

    public StartPosition(int number){

        this.number = number;
        texture = new Texture(""); //TODO add start position pictures
    }


    @Override
    public Texture getTexture() {
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
