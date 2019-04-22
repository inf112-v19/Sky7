package sky7.board.cellContents.robots;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Vector2;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.IMoving;

import java.util.Objects;

public class RobotTile implements IMoving {
    String textureRef = "robot";
    Texture texture;
    private static final int PRIORITY = 11;
    int playerNr;
    DIRECTION dir;
    private Vector2 archiveMarker;
    
    public RobotTile(int playerNr) {
        this.playerNr = playerNr;
        this.dir = DIRECTION.NORTH;
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            texture = new Texture("assets/robots/Robot" + playerNr + ".png");
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
        if(other == this) return 0;
        if(other instanceof RobotTile ){
            return -1;
        } else {
            return Integer.compare(this.drawPriority(), other.drawPriority());
        }
    }

    @Override
    public DIRECTION getOrientation() {
        return this.dir;
    }

    @Override
    public void rotateCCW() {
        switch (dir) {
        case NORTH:
            dir = DIRECTION.WEST;
            break;
        case EAST:
            dir = DIRECTION.NORTH;
            break;
        case SOUTH:
            dir = DIRECTION.EAST;
            break;
        case WEST:
            dir = DIRECTION.SOUTH;
            break;
        default:
            throw new IllegalStateException("Found no orientation for robot " + playerNr);
        }
    }
    
    @Override
    public void rotateCW() {
        switch (dir) {
        case NORTH:
            dir = DIRECTION.EAST;
            break;
        case EAST:
            dir = DIRECTION.SOUTH;
            break;
        case SOUTH:
            dir = DIRECTION.WEST;
            break;
        case WEST:
            dir = DIRECTION.NORTH;
            break;
        default:
            throw new IllegalStateException("Found no orientation for robot " + playerNr);
        }
    }
    
    @Override
    public void rotate180() {
        dir = dir.reverse();
    }

    public int getId() {
        return playerNr;
    }

    public Vector2 getArchiveMarker() {
        return archiveMarker;
    }

    public void setArchiveMarker(Vector2 archiveMarker) {
        this.archiveMarker = archiveMarker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RobotTile robotTile = (RobotTile) o;
        return playerNr == robotTile.playerNr &&
                Objects.equals(textureRef, robotTile.textureRef) &&
                Objects.equals(texture, robotTile.texture) &&
                dir == robotTile.dir &&
                Objects.equals(archiveMarker, robotTile.archiveMarker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textureRef, texture, playerNr, dir, archiveMarker);
    }
}
