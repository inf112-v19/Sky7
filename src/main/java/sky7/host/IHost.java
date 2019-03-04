package sky7.host;

import java.util.Stack;

import sky7.card.ICard;

public interface IHost {
    
    /**
     * 
     */
    void run();
    

    /**
     * Method to be called by players, to inform host of which cards are chosen to play, and in what order
     * 
     * @param pN player number
     * @param registry the chosen cards to play
     * @param discard the discarded cards
     */
    void ready(int pN,  Stack<ICard> registry, Stack<ICard> discard);
}
