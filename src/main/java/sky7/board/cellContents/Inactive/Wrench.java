package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Wrench implements IInactive {
    private int type;
    private final static int PRIORITY = 2;
    private Texture texture;

    public Wrench(int type) {
        this.type = type;

    }

    @Override
    public Texture getTexture() {
        if(texture == null){
            switch (type){
                case 1: texture = new Texture("assets/wrench/repair1.png"); break;
                case 2: texture = new Texture("assets/wrench/repair2.png"); break;
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
    public boolean isVisible() {
        return true;
    }

    @Override
    public int compareTo(ICell other) {
        return Integer.compare(this.drawPriority(), other.drawPriority());
    }

    public static List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();
        suppliers.add(new AbstractMap.SimpleEntry<>("t1", () -> new Wrench(1)));
        suppliers.add(new AbstractMap.SimpleEntry<>("t2", () -> new Wrench(2)));
        return suppliers;
    }

    public int getType(){
        return type;
    }
}
