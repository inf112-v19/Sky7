package sky7.card;

public class ProgramCard implements IProgramCard, Comparable<ProgramCard> {

    private int priority, move, rotate;
    private boolean moveType;
    
    /**
     * @param priority Card priority number
     * @param move Move value, positive forward, negative to reverse
     * @param rotate rotation value, positive clockwise, negative counter-clockwise
     */
    public ProgramCard(int priority, int move, int rotate) {
        this.priority = priority;
        this.move = move;
        this.rotate = rotate;
        
        if (move > 0)
            moveType = true;
        else
            moveType = false;
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

    @Override
    public int compareTo(ProgramCard other) {
        return Integer.compare(this.priority, other.priorityN());
    }

    @Override
    public String toString() {
        return super.toString();//TODO return a string representation of the card
    }

    @Override
    public boolean moveType() {
        // TODO Auto-generated method stub
        return false;
    }

}