package sky7.board;

import com.badlogic.gdx.graphics.Texture;

public interface ICell extends Comparable<ICell>{

    /**
     * @return texture for this cell
     */
    Texture getTexture();

    /**
     * the cell with the lowest priority will be draw first
     *
     * @return priority of the cell
     */
    int drawPriority(); // TODO Abstract away the priority of each ICell type to a txt file or Enumeration class.
}
