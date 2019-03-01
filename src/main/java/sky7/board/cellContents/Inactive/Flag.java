package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

public class Flag implements IInactive {
    private int flagNumber;
    private int priority;
    private Texture texture;

    public Flag(int flagNumber) {
        this.flagNumber = flagNumber;
        switch (flagNumber) {
            case 1:
                texture = new Texture("flags/Flag1.png");
                break;
            case 2:
                texture = new Texture("flags/Flag2.png");
                break;
            case 3:
                texture = new Texture("flags/Flag3.png");
                break;
            case 4:
                texture = new Texture("flags/Flag4.png");
                break;
            default:
                throw new IllegalArgumentException("unknown flag number");
        }
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
