package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class StartPosition implements IInactive{
    private int number;
    private static final int PRIORITY = 4;
    private Texture texture;

    public StartPosition(int number){

        this.number = number;

    }


    @Override
    public Texture getTexture() {
        if(texture == null){
            texture = new Texture("");//TODO add start position pictures
        }
        return texture;
    }

    @Override
    public int drawPriority() {
        return PRIORITY;
    }

    @Override
    public int compareTo(ICell other) {

        return Integer.compare(this.drawPriority(), other.drawPriority());
    }
}
