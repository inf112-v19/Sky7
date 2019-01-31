package sky7.card;

public interface ICard {

    /**
     * Get the priority number of the card
     * 
     * @return the priority number
     */
    public int priorityN();
    
    /**
     * Get the movement value of the card
     * 
     * @return the movement value, should be from -1 (reverse) to 3
     */
    public int move();
    
    /**
     * Get the rotation value of the card
     * 
     * @return the rotation value, -90, 90 or 180 (?). Alternatively return -1, 1 or 2 if representing direction as 1-increments
     */
    public int rotate();
}
