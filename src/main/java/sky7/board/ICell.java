package sky7.board;

import com.badlogic.gdx.graphics.Texture;

public interface ICell extends Comparable<ICell>{
    
    Texture getTexture();
    
    int drawPriority(); // TODO Abstract away the priority of each ICell type to a txt file or Enumeration class.
}
