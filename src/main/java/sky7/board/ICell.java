package sky7.board;

public interface ICell extends Comparable<ICell>{
    public String getTexture();
    public int drawPriority();
}
