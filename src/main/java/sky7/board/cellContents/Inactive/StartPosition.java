package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IInactive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class StartPosition implements IInactive {
    private int startNumber;
    private static final int PRIORITY = 3;
    private Texture texture;

    public StartPosition(int startNumber) {
        this.startNumber = startNumber;
    }


    @Override
    public Texture getTexture() {
        if (texture == null) {
            switch (startNumber){
                case 1: texture = new Texture("assets/startposition/1.png"); break;
                case 2: texture = new Texture("assets/startposition/2.png"); break;
                case 3: texture = new Texture("assets/startposition/3.png"); break;
                case 4: texture = new Texture("assets/startposition/4.png"); break;
                case 5: texture = new Texture("assets/startposition/5.png"); break;
                case 6: texture = new Texture("assets/startposition/6.png"); break;
                case 7: texture = new Texture("assets/startposition/7.png"); break;
                case 8: texture = new Texture("assets/startposition/8.png"); break;

            }
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

    public static List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();
        int maxNrOfPlayer = 8;
        for (int i = 0; i < maxNrOfPlayer; i++) {
            final int pos = i;
            suppliers.add(new AbstractMap.SimpleEntry<>("s" + i + 1, () -> new StartPosition(pos)));
        }
        return suppliers;
    }
}
