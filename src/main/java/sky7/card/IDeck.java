package sky7.card;

import java.util.ArrayList;

public interface IDeck {

    /**
     * Draw <b>n</b> cards from this deck
     * @param n number of cards to draw
     * @return a stack of the drawn cards
     */
    ArrayList<ICard> draw (int n);
    
    /**
     * Return used cards to the deck. <br>
     * N of returned cards might be less than drawn, if program cards are burned into some robot's memory
     * 
     * @param cards stack of cards returned
     */
    void returnCards (ArrayList<ICard> cards);
    
    /**
     * Shuffle the deck
     */
    void shuffle();

    /**
     * Get the number of cards currently in the deck
     * 
     * @return the number of cards available
     */
    int nRemainingCards();

}
