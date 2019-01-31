package sky7.card;

public class Card implements ICard {

    private int priority, move, rotate;
    
    public Card(int priority, int move, int rotate) {
        this.priority = priority;
        this.move = move;
        this.rotate = rotate;
    }

    @Override
    public int priorityN() {
        return priority;
    }

    @Override
    public int move() {
        return move;
    }

    @Override
    public int rotate() {
        return rotate;
    }

}