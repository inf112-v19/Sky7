package sky7.board.cellContents.Active;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IActive;

public class Belt implements IActive{
    private int direction;
    private int type;
    private int priority;

    public Belt(int direction, int type){

        this.direction = direction;
        this.type = type;
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
