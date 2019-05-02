package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IInactive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Wall implements IInactive {
    private DIRECTION direction;
    private Texture texture;
    private int PRIORITY;

    public Wall(DIRECTION direction) {
        this.direction = direction;
        
        switch (direction) {
            case NORTH:
                PRIORITY = 6;
                break;
                
            case EAST:
                PRIORITY = 7;
                break;
    
            case SOUTH:
                PRIORITY = 8;
                break;
                
            case WEST:
                PRIORITY = 9;
                break;
    
            default:
                throw new IllegalStateException("The direction arguement of the wall is not in a valid state");
        }
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            switch (direction) {
                case NORTH:
                    texture = new Texture("assets/wall/WallN.png");
                    break;
                    
                case EAST:
                    texture = new Texture("assets/wall/WallE.png");
                    break;

                case SOUTH:
                    texture = new Texture("assets/wall/WallS.png");
                    break;
                    
                case WEST:
                    texture = new Texture("assets/wall/WallW.png");
                    break;

                default:
                    throw new IllegalStateException("The direction arguement of the wall is not in a valid state");
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

    /**
     * tells where the wall is placed in the cell.
     *
     * @return in what direction this wall is placed
     */
    public DIRECTION getDirection() {
        return direction;
    }

    public boolean canGoHere(DIRECTION incomingDir) {
        return incomingDir.reverse() == direction;
    }


    public static List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();
        final char[] dirSym = {'N', 'S', 'E', 'W'};
        int maxNrOfDirections = 4;
        for (int i = 0; i < maxNrOfDirections; i++) {
            final int a = i;
            suppliers.add(new AbstractMap.SimpleEntry<>("w" + dirSym[i], () -> new Wall(DIRECTION.values()[a])));
        }
        return suppliers;
    }

}