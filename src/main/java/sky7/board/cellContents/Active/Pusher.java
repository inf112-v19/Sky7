package sky7.board.cellContents.Active;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IActive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;


public class Pusher implements IActive {
    private final DIRECTION direction;
    private final boolean oddPhased;
    private Texture texture;
    private final int PRIORITY = 6;

    public Pusher(DIRECTION direction, boolean oddPhased) {
        this.direction = direction;
        this.oddPhased = oddPhased; //oddPhased is 1 if it is oddPhased else 0

    }

    /**
     * Check if the pusher activates in the current phase.
     *
     * @param phase the current phase
     * @return true if the pusher activates in phase, false otherwise.
     */
    boolean doActivate(int phase) {
        assert phase > 0 && phase < 5;
        return phase % 2 == 1 && oddPhased;
    }

    @Override
    public Texture getTexture() {//TODO change image 8.png , this is the same as 1.png
        if (texture == null) {
            if (!oddPhased) {
                switch (direction) {
                    case NORTH:
                        texture = new Texture("assets/pusher/3.png");
                        break;
                    case SOUTH:
                        texture = new Texture("assets/pusher/1.png");
                        break;
                    case EAST:
                        texture = new Texture("assets/pusher/4.png");
                        break;
                    case WEST:
                        texture = new Texture("assets/pusher/2.png");
                        break;
                    default:
                        throw new IllegalStateException("The direction argument of the pusher is not in a valid state");

                }
            } else {
                switch (direction) {
                    case NORTH:
                        texture = new Texture("assets/pusher/8.png");
                        break;
                    case SOUTH:
                        texture = new Texture("assets/pusher/6.png");
                        break;
                    case EAST:
                        texture = new Texture("assets/pusher/9.png");
                        break;
                    case WEST:
                        texture = new Texture("assets/pusher/7.png");
                        break;
                    default:
                        throw new IllegalStateException("The direction argument of the pusher is not in a valid state");
                }
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

    /**
     * return the direction of this pusher
     *
     * @return
     */
    public DIRECTION getDirection() {
        return direction;
    }

    public static List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();

        final char[] dirSym = {'N', 'S', 'E', 'W'};
        final char[] phase = {'P', 'O'};
        int maxNrOfDirections = 4;
        for (int i = 0; i < maxNrOfDirections; i++) {
            final int a = i;
            for (int j = 0; j < phase.length; j++) {
                final int b = j;
                suppliers.add(new AbstractMap.SimpleEntry<>("p" + dirSym[i] + phase[j], () -> new Pusher(DIRECTION.values()[a], phase[b] == 'O')));
            }
        }
        return suppliers;
    }
}
