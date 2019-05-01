package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Hole implements IInactive{
    private final int PRIORITY = 2;
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
    public boolean isVisible() {
        return true;
    }

    @Override
    public int compareTo(ICell other) {
        return Integer.compare(this.drawPriority(), other.drawPriority());
    }

    public static List<AbstractMap.SimpleEntry<String,Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();
        suppliers.add(new AbstractMap.SimpleEntry<>("h", Hole::new));
        return suppliers;
    }
}
