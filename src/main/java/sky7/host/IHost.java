package sky7.host;

import java.util.Stack;

import sky7.card.ICard;

public interface IHost {
    
    /**
     * 
     */
    void run();
    
    /**
     * @param pN
     * @param registry
     */
    void ready(int pN,  Stack<ICard> registry, Stack<ICard> discard);
}
