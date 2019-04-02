package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FloorTile implements IInactive {

    String textureRef = "floor";
    private Texture texture;
    private static final int PRIORITY = 2;

    public FloorTile(){
        
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            texture = new Texture("assets/floor/plain.png");
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

    public static List<AbstractMap.SimpleEntry<String,Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();
        suppliers.add(new AbstractMap.SimpleEntry<>("f", FloorTile::new));
        return suppliers;
    }
}
