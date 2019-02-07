package sky7.board;

public interface ICell extends Comparable<ICell>{
    
    String getTexture();
    
    int drawPriority();
}
