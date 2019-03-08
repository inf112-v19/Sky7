package sky7.board.cellContents.Active;
import com.badlogic.gdx.graphics.Texture;
import sky7.board.Board;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;

public class SingelConveyorBelt implements IConveyorBelt {
    private DIRECTION fromDirectionOne;
    private DIRECTION fromDirectionTwo; // Can be non, if conveyor belt only has one entry
    private DIRECTION toDirection; // Change robot to face this way when done


    private Texture texture;
    private final int stepsToMove = 1;
    private final int priority = 2;

    public SingelConveyorBelt(DIRECTION fromDirectionOne, DIRECTION fromDirectionTwo, DIRECTION toDirection, String textureName ){
        this.fromDirectionOne = fromDirectionOne;
        this.fromDirectionTwo = fromDirectionTwo;
        this.toDirection = toDirection;




    }




    @Override
    public Texture getTexture() {
        // TODO: Change ICell to use Texture insted of String
        if(texture == null){
            //texture = new Texture("assets/floor/Conv/yellow/" + textureName + ".png"); TODO: fix belts
        }
        return texture;

    }

    @Override
    public int drawPriority() {
        return priority;
    }

    @Override
    public int compareTo(ICell other) {
        return Integer.compare(this.drawPriority(), other.drawPriority());

    }
}
