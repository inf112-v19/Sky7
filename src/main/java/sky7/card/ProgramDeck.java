package sky7.card;

import java.util.Collections;
import java.util.Stack;

public class ProgramDeck implements IDeck {
    
    private Stack<ICard> availableCards;
    
    public ProgramDeck() {
        availableCards = new Stack<>();
        // need to generate the specific cards
        
    }

    @Override
    public Stack<ICard> draw(int n) {
        if (n<1) throw new IllegalArgumentException("Cannot draw less than 1 card.");
        if (n>72) throw new IllegalArgumentException("Cannot draw more than 72 cards (8 players * 9 cards)");
        if (n>availableCards.size()) throw new IllegalArgumentException("Attempting to draw more cards than available");
        
        Stack<ICard> drawn = new Stack<>();
        for (int i=0; i<n; i++) {
            drawn.push(availableCards.pop());
        }
        
        return drawn;
    }

    @Override
    public void returnCards(Stack<ICard> cards) {
        for (ICard card : cards) {
            availableCards.push(card);
        }
        
        shuffle();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(availableCards);
    }
}