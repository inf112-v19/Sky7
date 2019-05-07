package sky7.board.cellContents.Active;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IActive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CogWheel implements IActive {

    private int rotDirection; // -1 counterclockwise, 1 clockwise
    private final int PRIORITY = 3;
    private Texture texture;

    public CogWheel(int rotDirection) {
        this.rotDirection = rotDirection;
    }


    @Override
    public Texture getTexture() {
        if (texture == null) {
            if (rotDirection == -1) {
                texture = new Texture("assets/cogwheel/CogRed.png");
            } else if (rotDirection == 1) {
                texture = new Texture("assets/cogwheel/CogGreen.png");
            } else {
                throw new IllegalStateException("Cog invalid rotation value");
            }
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

    /**
     * in which way this rotate
     * @return either rotate clockwise(1) or counterclockwise(-1)
     */
    public int getRotation() {
        return this.rotDirection;
    }

    public static List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();
        suppliers.add(new AbstractMap.SimpleEntry<>("cC", () -> new CogWheel(1)));
        suppliers.add(new AbstractMap.SimpleEntry<>("cA", () -> new CogWheel(-1)));
        return suppliers;
    }
}
