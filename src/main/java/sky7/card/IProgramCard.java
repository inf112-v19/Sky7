package sky7.card;

public interface IProgramCard extends ICard {

    /**
     * Get the priority number of the card
     * 
     * @return the priority number
     */
    int priorityN();
    
    /**
     * Get the movement value of the card
     * 
     * @return the movement value, should be from -1 (reverse) to 3
     */
    int move();
    
    /**
     * Get the rotation value of the card
     * 
     * @return the rotation value  (-1, 1 or 2)
     */
    int rotate();
}
