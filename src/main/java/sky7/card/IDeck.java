package sky7.card;

import java.util.Stack;

public interface IDeck {

    /**
     * Draw <b>n</b> cards from this deck
     * @param n number of cards to draw
     * @return a stack of the drawn cards
     */
    public Stack<ICard> draw (int n);
    
    /**
     * Return used cards to the deck. <br>
     * N of returned cards might be less than drawn, if program cards are burned into some robot's memory
     * 
     * @param cards stack of cards returned
     */
    public void returnCards (Stack<ICard> cards);
    
    void shuffle();
}
