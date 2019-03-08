package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Wrench implements IInactive {
    private int type;
    private final static int PRIORITY = 3;
    private Texture texture;

    public Wrench(int type) {
        this.type = type;

    }

    @Override
    public Texture getTexture() {
        if(texture == null){
            switch (type){
                case 1: texture = new Texture("wrench/repair1.png"); break;
                case 2: texture = new Texture("wrench/repair2.png"); break;
                default: throw new IllegalStateException("unknown wrench type");
            }
        }
        return texture;
    }

    @Override
    public int drawPriority() {
        return PRIORITY;
    }

    @Override
    public int compareTo(ICell o) {
        return 0;
    }
}
