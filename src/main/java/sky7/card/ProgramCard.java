package sky7.card;

public class ProgramCard implements IProgramCard {

    private int priority, move, rotate;
    
    /**
     * @param priority Card priority number
     * @param move Move value, positive forward, negative to reverse
     * @param rotate rotation value, positive clockwise, negative counter-clockwise
     */
    public ProgramCard(int priority, int move, int rotate) {
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



    @Override
    public String toString() {
        return super.toString();//TODO return a string representation of the card
    }

}