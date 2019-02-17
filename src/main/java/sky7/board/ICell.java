package sky7.board;

public interface ICell extends Comparable<ICell>{
    
    String getTexture();
    
    int drawPriority(); // TODO Abstract away the priority of each ICell type to a txt file or Enumeration class.
}
