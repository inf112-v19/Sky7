package sky7.board.cellContents.Inactive;

import sky7.board.cellContents.IInactive;

public class FloorTile implements IInactive {
    String texture = "assets/Floor.png";

    @Override
    public String getTexture() {
        return texture;
    }

}
